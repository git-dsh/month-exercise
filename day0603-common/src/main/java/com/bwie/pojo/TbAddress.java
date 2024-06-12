package com.bwie.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName tb_address
 */
@TableName(value ="tb_address")
@Data
public class TbAddress implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String address;

    /**
     * 
     */
    private Integer parentId;

    /**
     * 
     */
    private Integer relatedId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}