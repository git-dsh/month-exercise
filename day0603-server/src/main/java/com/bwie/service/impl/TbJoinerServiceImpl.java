package com.bwie.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bwie.mapper.TbJoinerMapper;
import com.bwie.mapper.TbVoteMapper;
import com.bwie.pojo.TbJoiner;
import com.bwie.pojo.TbVote;
import com.bwie.service.TbJoinerService;
import com.bwie.util.R;
import com.bwie.utils.FastFileUtil;
import com.bwie.utils.FreemarkerUtil;
import com.bwie.utils.OSSFileUtil;
import com.bwie.vo.MessageVo;
import com.bwie.vo.PageVo;
import com.rabbitmq.client.Channel;
import freemarker.template.TemplateException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
* @author dsh
* @description 针对表【tb_joiner】的数据库操作Service实现
* @createDate 2024-06-03 20:07:20
*/
@Service
@Slf4j
public class TbJoinerServiceImpl extends ServiceImpl<TbJoinerMapper, TbJoiner>
    implements TbJoinerService{
    @Autowired
    TbJoinerMapper tbJoinerMapper;
    @Autowired
    OSSFileUtil ossFileUtil;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    TbVoteMapper tbVoteMapper;
    @Autowired
    FreemarkerUtil freemarkerUtil;
    @Autowired
    FastFileUtil fastFileUtil;
    @PostConstruct
    public void initRabbitMq(){
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("回退消息是："+new String(message.getBody()));
                log.info("回退replyCode是："+replyCode);
                log.info("回退replyText是："+replyText);
                log.info("回退exchange是："+exchange);
                log.info("回退routingKey是："+routingKey);
                if(replyCode==200){
                    log.info("消息发送成功");
                }else{
                    rabbitTemplate.convertAndSend(exchange,routingKey,message);
                }
            }
        });
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                String id = correlationData.getId();
                if(ack){
                    log.debug("消息发送成功-----");
                }else{
                    log.info("消息发送失败，消息ID={},失败原因={}",id,cause);
                    Message returnedMessage = correlationData.getReturnedMessage();
                    String s = new String(returnedMessage.getBody());
                    rabbitTemplate.convertAndSend("directExchange","vote",s);
                }
            }
        });
    }

    @Override
    public R joinerList(PageVo pageVo) {
        Page<TbJoiner> page = new Page<>(pageVo.getPageNum(), pageVo.getPageSize());
        Page<TbJoiner> page1 = tbJoinerMapper.selectPage(page, null);
        return R.success(page1);
    }

    @Override
    @Transactional
    @SneakyThrows
    public R saveJoiner(TbJoiner tbJoiner, String token) throws TemplateException, IOException {
        Claims bwie = Jwts.parser().setSigningKey("bwie").parseClaimsJws(token).getBody();
        String userRole = bwie.get("userRole", String.class);
        if(userRole.equals("admin")){
            int insert = tbJoinerMapper.insert(tbJoiner);
            if(insert>0){
                Map<String, Object> map = BeanUtil.beanToMap(tbJoiner);
                String path = this.getClass().getResource("/").getPath();
                String html = freemarkerUtil.createHtml((HashMap<String, Object>) map, "vote", path+"vote2111a.html");
                if(StringUtil.isEmpty(html)){
                    throw new RemoteException("生成html失败");
                }
                File file = FileUtil.file(html);
                String htmlUrl = fastFileUtil.uploadFile(file);
                tbJoiner.setJoinerHtml(htmlUrl);
                tbJoinerMapper.updateById(tbJoiner);
            }
            TbJoiner tbJoiner1 = tbJoinerMapper.selectById(tbJoiner.getId());
            stringRedisTemplate.opsForValue().set("TbJoiner"+tbJoiner1.getId(), JSON.toJSONString(tbJoiner1));
            return R.success();
        }else{
            return R.error(500,"不是管理员无法操作");
        }

    }

    @Override
    public R uploadImg(MultipartFile file) throws IOException {
        String url = ossFileUtil.uploadFile(file);
        return R.success(url);
    }

    @Override
    public R uploadVideo(MultipartFile file) throws IOException {
        String url = ossFileUtil.uploadFileByBreakingPoint(file);
        return R.success(url);
    }

    @Override
    public R voteJoiner(Integer joinerId, String token) {
        Claims bwie = Jwts.parser().setSigningKey("bwie").parseClaimsJws(token).getBody();
        Integer userId = bwie.get("id", Integer.class);
        TbVote tbVote1 = tbVoteMapper.selectById(joinerId);
        if(tbVote1==null){
            TbVote tbVote = new TbVote();
            tbVote.setJoinerId(joinerId);
            tbVote.setUserId(userId);
            MessageVo messageVo = new MessageVo();
            String msgId = "MSG"+UUID.randomUUID().toString();
            stringRedisTemplate.opsForValue().set(msgId,msgId,5, TimeUnit.MINUTES);
            messageVo.setMsgId(msgId);
            messageVo.setMsgBody(JSON.toJSONString(tbVote));

            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(UuidUtils.generateUuid());
            String s = JSONUtil.toJsonStr(messageVo);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            Message message = new Message(s.getBytes(),messageProperties);
            correlationData.setReturnedMessage(message);
            rabbitTemplate.convertAndSend("directExchange","vote",JSON.toJSONString(messageVo),correlationData);
            return R.success();
        }else{
            return R.error(500,"一个用户只能对每个候选人投票一次，不能重复投票");
        }
    }
    @Transactional
    @RabbitListener(queues = "voteQueue")
    public void voteQueue(Message message, Channel channel) throws IOException {
        String s = new String(message.getBody());
        MessageVo messageVo = JSON.parseObject(s, MessageVo.class);
        if(!stringRedisTemplate.hasKey(messageVo.getMsgId())){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        }
        String msgBody = messageVo.getMsgBody();
        TbVote tbVote = JSON.parseObject(msgBody, TbVote.class);
        tbVoteMapper.insert(tbVote);
        log.info("投票成功");
    }
}




