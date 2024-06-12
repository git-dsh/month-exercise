package com.bwie.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    /**
     * PublishSubscribeQueueA 队列名称
     * PublishSubscribeExchange 交换机名称
     * @return
     */
    @Bean
    public Queue voteQueue(){
        return new Queue("voteQueue",true);
    }
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("directExchange");
    }
    @Bean
    public Binding bindingVote(){
        return BindingBuilder.bind(voteQueue()).to(directExchange()).with("vote");
    }
}
