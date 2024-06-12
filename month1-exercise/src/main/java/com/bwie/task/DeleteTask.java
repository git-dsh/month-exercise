package com.bwie.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bwie.mapper.TbShopMapper;
import com.bwie.pojo.TbShop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: day0603_exercise
 * @author: 段帅虎
 * @description:
 * @create: 2024-06-07 16:20
 */
@Component
@Slf4j
public class DeleteTask {
    @Autowired
    TbShopMapper tbShopMapper;
    @Scheduled(cron = "0 0/5 * * * ?")
    public void deleteTask(){
        List<TbShop> tbShopList = tbShopMapper.selectList(new QueryWrapper<TbShop>().lambda().eq(TbShop::getOrderId, 0));
        for (TbShop tbShop : tbShopList) {
            int deleteFlag = tbShopMapper.deleteById(tbShop.getId());
            if(deleteFlag>0){
                log.info("未绑定订单货品信息删除成功！！！！！==肮脏的数据");
            }
        }
    }
}
