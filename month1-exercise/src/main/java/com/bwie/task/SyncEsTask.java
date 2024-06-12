package com.bwie.task;

import com.bwie.mapper.TbOrderMapper;
import com.bwie.vo.EsOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: day0603_exercise
 * @author: 段帅虎
 * @description:
 * @create: 2024-06-07 14:02
 */
@Component
public class SyncEsTask {
    @Autowired
    TbOrderMapper tbOrderMapper;
    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Scheduled(cron = "0/30 * * * * ?")
    public void syncEsTask(){
        String key = "KEY_ES_ORDER";
        Date updateTime = null;
        String s = stringRedisTemplate.opsForValue().get(key);
        if(s==null){
            List<EsOrderVo> esOrderVoList =  tbOrderMapper.selectOrderList(updateTime);
            if(esOrderVoList.size()>0&&esOrderVoList!=null){
                elasticsearchRestTemplate.save(esOrderVoList);
                EsOrderVo esOrderVo = esOrderVoList.get(esOrderVoList.size() - 1);
                Date createTime = esOrderVo.getCreateTime();
                stringRedisTemplate.opsForValue().set(key,createTime.getTime()+"",1, TimeUnit.MINUTES);
            }
        }else{
            updateTime = new Date(Long.valueOf(s));
            List<EsOrderVo> esOrderVoList =  tbOrderMapper.selectOrderList(updateTime);
            if(esOrderVoList.size()>0&&esOrderVoList!=null){
                elasticsearchRestTemplate.save(esOrderVoList);
                EsOrderVo esOrderVo = esOrderVoList.get(esOrderVoList.size() - 1);
                Date createTime = esOrderVo.getCreateTime();
                stringRedisTemplate.opsForValue().set(key,createTime.getTime()+"",1, TimeUnit.MINUTES);
            }
        }
    }
}
