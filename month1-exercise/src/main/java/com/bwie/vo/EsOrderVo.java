package com.bwie.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表
 * @TableName tb_order
 */
@Data
@Document(indexName = "es_order",shards = 1,replicas = 1)
public class EsOrderVo implements Serializable {
    /**
     * 
     */
    @Id
    private Integer id;

    /**
     * 
     */
    @Field(type = FieldType.Keyword)
    private String orderId;

    /**
     * 
     */
    @Field(type = FieldType.Date,format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 
     */
    @Field(type = FieldType.Keyword)
    private String orderStatus;

    /**
     * 
     */
    @Field(type = FieldType.Keyword)
    private String shippedName;

    /**
     * 
     */
    @Field(type = FieldType.Keyword)
    private String shippedMobile;

    /**
     * 
     */
    @Field(type = FieldType.Keyword)
    private String shippedAddress;

    /**
     * 
     */
    @Field(type = FieldType.Keyword,index = false)
    private String shippedAddressDetails;

    /**
     * 
     */
    @Field(type = FieldType.Keyword)
    private String receivingName;

    /**
     * 
     */
    @Field(type = FieldType.Keyword)
    private String receivingMobile;

    /**
     * 
     */
    @Field(type = FieldType.Keyword)
    private String receivingAddress;

    /**
     * 
     */
    @Field(type = FieldType.Keyword,index = false)
    private String receivingAddressDetails;

    /**
     * 
     */
    @Field(type = FieldType.Double,index = false)
    private BigDecimal orderFreight;

    /**
     * 
     */
    @Field(type = FieldType.Keyword,index = false)
    private String payMethod;

    /**
     * 
     */
    @Field(type = FieldType.Keyword,index = false)
    private String payStatus;
}