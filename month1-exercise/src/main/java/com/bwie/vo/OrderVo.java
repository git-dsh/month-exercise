package com.bwie.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: day0603_exercise
 * @author: 段帅虎
 * @description:
 * @create: 2024-06-07 11:39
 */
@Data
public class OrderVo implements Serializable {
    //分页
    private Integer pageNum;
    private Integer pageSize;
    //订单编号
    private String orderId;
    //es区间查询
    private String minTime;
    private String maxTime;
    //订单状态
    private String orderStatus;
    //发件人姓名
    private String shippedName;
    //发件人电话
    private String shippedMobile;
    //发件人地址
    private Integer shippedAddress;
    //收件人姓名
    private String receivingName;
    //收件人电话
    private String receivingMobile;
    //收件人地址
    private Integer receivingAddress;

}
