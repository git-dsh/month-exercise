package com.bwie.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单表
 * @TableName procure_order
 */
@TableName(value ="procure_order")
@Data
public class ProcureOrder implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 采购方表id
     */
    private Integer buyerId;

    /**
     * 发货方表id
     */
    private Integer shipperId;

    /**
     * 0待审核，1 要货中，2.已完成 3.已驳回 4.已关闭
     */
    private Integer orderStatus;

    /**
     * 期望交货时间
     */
    private Date deliveryTime;

    /**
     * 创建人id
     */
    private Integer createUserId;

    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 附件地址
     */
    private String fileUrl;

    /**
     * 订单的静态页面地址
     */
    private String orderHtmlUrl;

    /**
     * 1删除 0不是删除
     */
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}