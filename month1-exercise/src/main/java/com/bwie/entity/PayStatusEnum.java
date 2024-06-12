package com.bwie.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: day0603_exercise
 * @author: 段帅虎
 * @description:
 * @create: 2024-06-07 09:43
 */
public enum PayStatusEnum implements Serializable {

    PENDING("待支付"),
    PAID("已支付"),
    CANCELLED("已取消"),
    REFUNDED("已退款"),
    PROCESSING("处理中"),
    FAILED("支付失败");
    private String payName;

    PayStatusEnum(String payName) {
        this.payName = payName;
    }
    public String getPayName() {
        return payName;
    }

    public static List<String> getPayStatusList(){
        List<PayStatusEnum> list = Arrays.stream(PayStatusEnum.values()).collect(Collectors.toList());
        ArrayList<String> PayStatusList = new ArrayList<>();
        for (PayStatusEnum payStatusEnum : list) {
            String payName = payStatusEnum.getPayName();
            PayStatusList.add(payName);
        }
        return PayStatusList;
    }
}
