package com.bwie.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: day0603_exercise
 * @author: 段帅虎
 * @description:
 * @create: 2024-06-07 10:10
 */
public enum OrderStatusEnum implements Serializable {
    WAIT("待取件"),
    TRANSPORT("运输中"),
    DELIVERING("派送中"),
    SIGNED("已签收"),
    CANCELED("已取消");
    private String orderStatus;

    OrderStatusEnum(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getOrderStatus() {
        return orderStatus;
    }
    public static List<String> getOrderStatusList(){
        List<OrderStatusEnum> list = Arrays.stream(OrderStatusEnum.values()).collect(Collectors.toList());
        ArrayList<String> orderStatusList = new ArrayList<>();
        for (OrderStatusEnum orderStatusEnum : list) {
            String orderStatus1 = orderStatusEnum.getOrderStatus();
            orderStatusList.add(orderStatus1);
        }
        return orderStatusList;
    }
}
