package com.bwie.utils;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class RabbitmqUtil {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String str) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        String msg = "消息是"+str;
        rabbitTemplate.convertAndSend("PublishSubscribeExchange","",msg,correlationData);
        System.out.println("发送成功 "+new Date()+" "+msg);
    }

}
