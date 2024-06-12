package com.bwie.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

/**
 * 订单表
 * @TableName tb_order
 */
@TableName(value ="tb_order")
@Data
public class TbOrder implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String orderId;

    /**
     * 
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 
     */
    private String orderStatus;

    /**
     * 
     */
    @NotBlank(message = "发件人姓名不能为空")
    private String shippedName;

    /**
     * 
     */
    @NotBlank(message = "发件人电话不能为空")
    private String shippedMobile;

    /**
     * 
     */
    @NotBlank(message = "发件人地址不能为空")
    private String shippedAddress;

    /**
     * 
     */
    private String shippedAddressDetails;

    /**
     * 
     */
    @NotBlank(message = "收件人姓名不能为空")
    private String receivingName;

    /**
     * 
     */
    @NotBlank(message = "收件人电话不能为空")
    private String receivingMobile;

    /**
     * 
     */
    @NotBlank(message = "收件人地址不能为空")
    private String receivingAddress;

    /**
     * 
     */
    private String receivingAddressDetails;

    /**
     * 
     */
    @DecimalMin("0.01")
    private BigDecimal orderFreight;

    /**
     * 
     */
    private String payMethod;

    /**
     * 
     */
    private String payStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}