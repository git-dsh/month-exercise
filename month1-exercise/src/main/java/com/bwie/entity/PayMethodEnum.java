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
 * @create: 2024-06-07 09:51
 */
public enum PayMethodEnum implements Serializable {
    CASH("现金"),
    CREDIT_CARD("信用卡"),
    DEBIT_CARD("借记卡"),
    BANK_TRANSFER("银行转账"),
    WALLET("电子钱包");
    private String methodName;

    PayMethodEnum( String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }
    public static List<String> getPayMethodList(){
        List<PayMethodEnum> list = Arrays.stream(PayMethodEnum.values()).collect(Collectors.toList());
        ArrayList<String> payMethodList = new ArrayList<>();
        for (PayMethodEnum payMethodEnum : list) {
            String methodName = payMethodEnum.getMethodName();
            payMethodList.add(methodName);
        }
        return payMethodList;
    }
}
