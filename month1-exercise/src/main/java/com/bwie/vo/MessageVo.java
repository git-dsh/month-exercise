package com.bwie.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: day0603_exercise
 * @author: 段帅虎
 * @description:
 * @create: 2024-06-07 17:14
 */
@Data
public class MessageVo implements Serializable {
    private String msgId;
    private Integer msgOrderId;
    private Integer msgDriverId;
}
