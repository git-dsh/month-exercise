package com.bwie.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 货品信息表
 * @TableName tb_shop
 */
@TableName(value ="tb_shop")
@Data
public class TbShop implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer orderId;


    /**
     * 
     */
    private String shopName;

    /**
     * 
     */
    private Integer shopNum;

    /**
     * 
     */
    private String shopUnit;

    /**
     * 
     */
    private String shopType;

    /**
     * 
     */
    private Integer shopWeight;

    /**
     * 
     */
    private Integer shopVolume;

    /**
     * 
     */
    private BigDecimal shopPrice;

    /**
     * 
     */
    private String shopEncode;

    /**
     * 
     */
    private String shopRemark;
    private String shopPhoto;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}