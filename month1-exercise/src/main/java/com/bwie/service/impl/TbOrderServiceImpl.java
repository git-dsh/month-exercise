package com.bwie.service.impl;

import cn.hutool.db.PageResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bwie.entity.OrderStatusEnum;
import com.bwie.entity.PayMethodEnum;
import com.bwie.entity.PayStatusEnum;
import com.bwie.mapper.TbAddressMapper;
import com.bwie.mapper.TbDriverMapper;
import com.bwie.mapper.TbShopMapper;
import com.bwie.pojo.TbAddress;
import com.bwie.pojo.TbDriver;
import com.bwie.pojo.TbOrder;
import com.bwie.pojo.TbShop;
import com.bwie.service.TbOrderService;
import com.bwie.mapper.TbOrderMapper;
import com.bwie.util.R;
import com.bwie.utils.FastFileUtil;
import com.bwie.utils.FreemarkerUtil;
import com.bwie.utils.OSSFileUtil;
import com.bwie.vo.EsOrderVo;
import com.bwie.vo.MessageVo;
import com.bwie.vo.OrderVo;
import com.rabbitmq.client.Channel;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
* @author dsh
* @description 针对表【tb_order(订单表)】的数据库操作Service实现
* @createDate 2024-06-07 09:37:21
*/
@Service
@Slf4j
public class TbOrderServiceImpl extends ServiceImpl<TbOrderMapper, TbOrder>
    implements TbOrderService{
    @Autowired
    TbOrderMapper tbOrderMapper;
    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    TbAddressMapper tbAddressMapper;
    @Autowired
    TbShopMapper tbShopMapper;
    @Autowired
    OSSFileUtil ossFileUtil;
    @Autowired
    FastFileUtil fastFileUtil;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    TbDriverMapper tbDriverMapper;
    @Autowired
    RedissonClient redissonClient;
    @Override
    public R orderList(OrderVo orderVo) throws ParseException {
        int pageNum = 0;
        if(orderVo.getPageNum()>0){
            pageNum = orderVo.getPageNum()-1;
        }
        PageRequest pageRequest = PageRequest.of(pageNum, orderVo.getPageSize());
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if(!StringUtils.isEmpty(orderVo.getOrderId())){
            boolQueryBuilder.must(QueryBuilders.termQuery("orderId",orderVo.getOrderId()));
        }
        if(!StringUtils.isEmpty(orderVo.getOrderStatus())){
            boolQueryBuilder.must(QueryBuilders.termQuery("orderStatus",orderVo.getOrderStatus()));
        }
        if(!StringUtils.isEmpty(orderVo.getReceivingName())){
            boolQueryBuilder.must(QueryBuilders.termQuery("receivingName",orderVo.getReceivingName()));
        }
        if(!StringUtils.isEmpty(orderVo.getShippedName())){
            boolQueryBuilder.must(QueryBuilders.termQuery("shippedName",orderVo.getShippedName()));
        }
        if(!StringUtils.isEmpty(orderVo.getReceivingMobile())){
            boolQueryBuilder.must(QueryBuilders.termQuery("receivingMobile",orderVo.getReceivingMobile()));
        }
        if(!StringUtils.isEmpty(orderVo.getShippedMobile())){
            boolQueryBuilder.must(QueryBuilders.termQuery("shippedMobile",orderVo.getShippedMobile()));
        }
        if(!StringUtils.isEmpty(orderVo.getReceivingAddress())){
            boolQueryBuilder.must(QueryBuilders.termQuery("receivingAddress",orderVo.getReceivingAddress()));
        }
        if(!StringUtils.isEmpty(orderVo.getShippedAddress())){
            boolQueryBuilder.must(QueryBuilders.termQuery("shippedAddress",orderVo.getShippedAddress()));
        }
        if(!StringUtils.isEmpty(orderVo.getMinTime())&&StringUtils.isEmpty(orderVo.getMaxTime())){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("createTime")
                    .format("yyyy-MM-dd HH:mm:ss")
                    .timeZone("GMT+8")
                    .gte(orderVo.getMinTime())
                    .lte(orderVo.getMaxTime())
            );
        }
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        List<String> list = Arrays.asList("orderId","receivingAddress","shippedAddress","createTime");
        for (String fieldName : list) {
            highlightBuilder.field(fieldName).preTags("<font color='red'>").postTags("</font>");
        }
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder)
                .withHighlightBuilder(highlightBuilder)
                .withPageable(pageRequest);
        SearchHits<EsOrderVo> search = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), EsOrderVo.class);
        List<SearchHit<EsOrderVo>> hits = search.getSearchHits();
        ArrayList<EsOrderVo> esOrderVos = new ArrayList<>();
        for (SearchHit<EsOrderVo> hit : hits) {
            EsOrderVo esOrderVo = hit.getContent();

            List<String> orderId = hit.getHighlightField("orderId");
            if(!orderId.isEmpty()&&orderId!=null){
                String s1 = orderId.get(0);
                esOrderVo.setOrderId(s1);
            }

            List<String> receivingAddress = hit.getHighlightField("receivingAddress");
            if(!receivingAddress.isEmpty()&&receivingAddress!=null){
                String s2 = receivingAddress.get(0);
                esOrderVo.setReceivingAddress(s2);
            }

            List<String> shippedAddress = hit.getHighlightField("shippedAddress");
            if(!shippedAddress.isEmpty()&&shippedAddress!=null){
                String s3 = shippedAddress.get(0);
                esOrderVo.setShippedAddress(s3);
            }

            List<String> createTime = hit.getHighlightField("createTime");
            if(!createTime.isEmpty()&&createTime!=null){
                String s4 = createTime.get(0);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date time = simpleDateFormat.parse(s4);
                esOrderVo.setCreateTime(time);
            }
            esOrderVos.add(esOrderVo);
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total",search.getTotalHits());
        hashMap.put("pageNum",pageNum+1);
        hashMap.put("pageSize",orderVo.getPageSize());
        hashMap.put("records",esOrderVos);
        return R.success(hashMap);
    }

    @Override
    @Transactional
    public R saveOrder(TbOrder tbOrder,String token) {
        String redisKey = "ORDERID";
        Jedis jedis = new Jedis("192.168.56.131",6379);
        String s1 = jedis.get(redisKey);
        if(StringUtils.isEmpty(s1)){
            jedis.set(redisKey,"0");
        }
        Long orderId = jedis.incr(redisKey);
        String onlyOrderId = "BJYJS"+orderId;
        tbOrder.setOrderId(onlyOrderId);
        tbOrder.setCreateTime(new Date());
        int insertFlag = tbOrderMapper.insert(tbOrder);
        if(insertFlag>0){
            log.debug("(订单创建成功，已通知司机抢单");
            return R.success();
        }else{
            tbOrderMapper.deleteById(tbOrder.getId());
            log.debug("(订单创建错误，删除订单，同时删除数据库数据");
            return R.error(500,"添加失败");
        }
    }

    @RabbitListener(queues = "driverMessageQueue")
    public void listenerOrderMessage(Message message, Channel channel) throws IOException {
        String s = new String(message.getBody());
        MessageVo messageVo = JSON.parseObject(s, MessageVo.class);
        if(!stringRedisTemplate.hasKey(messageVo.getMsgId())){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        }
        Integer msgOrderId = messageVo.getMsgOrderId();
        Integer msgDriverId = messageVo.getMsgDriverId();
        RLock redissonClientLock = redissonClient.getLock("redisson:order" + msgOrderId);
        if(redissonClientLock.tryLock()){
            try {
                TbDriver driverOrder = tbDriverMapper.selectOne(new QueryWrapper<TbDriver>().lambda().eq(TbDriver::getOrderId, msgOrderId).eq(TbDriver::getDriverId, msgDriverId));
                if(driverOrder==null){
                    TbDriver tbDriver = new TbDriver();
                    tbDriver.setOrderId(msgOrderId);
                    tbDriver.setDriverId(msgDriverId);
                    tbDriver.setCreateTime(new Date());
                    int messageFlag = tbDriverMapper.insert(tbDriver);
                    if(messageFlag>0){
                        //修改订单状态
                        TbOrder tbOrder = new TbOrder();
                        tbOrder.setId(msgOrderId);
                        String orderStatus = OrderStatusEnum.TRANSPORT.getOrderStatus();
                        tbOrder.setOrderStatus(orderStatus);
                        tbOrderMapper.updateById(tbOrder);
                        log.info("接单成功");
                    }else{
                        log.error("接单失败");
                    }
                }else{
                    log.error("订单已被其他人接收");
                }
            } finally {
                if(redissonClientLock.isLocked()&&redissonClientLock.isHeldByCurrentThread()){
                    redissonClientLock.unlock();
                }
            }
        }

    }
    @Override
    public R addressList() {
        List<TbAddress> tbAddresses = tbAddressMapper.selectList(null);
        return R.success(tbAddresses);
    }

    @Override
    public R payMethodList() {
        List<String> payMethodList = PayMethodEnum.getPayMethodList();
        return R.success(payMethodList);
    }

    @Override
    public R payStatusList() {
        List<String> payStatusList = PayStatusEnum.getPayStatusList();
        return R.success(payStatusList);
    }

    @Override
    public R saveShop(Integer orderId, TbShop tbShop) {
        tbShop.setOrderId(orderId);
        int insertFlag = tbShopMapper.insert(tbShop);
        if(insertFlag>0){
            return R.success();
        }else{
            return R.error(500,"添加失败");
        }
    }

    @Override
    public R selectShopList(Integer orderId) {
        List<TbShop> tbShopList = tbShopMapper.selectList(new QueryWrapper<TbShop>().lambda().eq(TbShop::getOrderId, orderId));
        return R.success(tbShopList);
    }

    @Override
    public R upload(MultipartFile file) throws IOException {
        String fastDfsUrl = fastFileUtil.uploadFile(file);
        if(fastDfsUrl!=null&&StringUtils.isEmpty(fastDfsUrl)){
            String ossUrl = ossFileUtil.uploadFileByBreakingPoint(file);
            if(ossUrl!=null&&StringUtils.isEmpty(ossUrl)){
                log.info("oss存储图片备份成功");
            }
        }
        return R.success(fastDfsUrl);
    }

    @Override
    public R createIndex() {
        IndexOperations indexOps = elasticsearchRestTemplate.indexOps(EsOrderVo.class);
        if(indexOps.exists()){
            indexOps.delete();
        }
        boolean createStatus = indexOps.create();
        if(!createStatus){
            return R.error(500,"创建失败");
        }
        Document mapping = indexOps.createMapping();
        indexOps.putMapping(mapping);
        return R.success("创建成功");
    }

    @Override
    public R orderStatus() {
        List<String> orderStatusList = OrderStatusEnum.getOrderStatusList();
        return R.success(orderStatusList);
    }

    @Override
    public R saveDriverOrder(Integer orderId, String token) {
        Claims bwie = Jwts.parser().setSigningKey("bwie").parseClaimsJws(token).getBody();
        Integer userId = bwie.get("id", Integer.class);
        MessageVo messageVo = new MessageVo();
        String key = "MSGID"+UUID.randomUUID().toString();
        messageVo.setMsgId(key);
        messageVo.setMsgOrderId(orderId);
        messageVo.setMsgDriverId(userId);
        stringRedisTemplate.opsForValue().set(key,JSON.toJSONString(messageVo),5, TimeUnit.MINUTES);
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UuidUtils.generateUuid());
        String s = JSON.toJSONString(messageVo);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        Message message = new Message(s.getBytes(), messageProperties);
        correlationData.setReturnedMessage(message);
        rabbitTemplate.convertAndSend("directExchange","driver",s,correlationData);
        return R.success();
    }
}




