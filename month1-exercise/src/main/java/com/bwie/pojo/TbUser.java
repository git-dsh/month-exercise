package com.bwie.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户登录表
 * @TableName tb_user
 */
@TableName(value ="tb_user")
@Data
public class TbUser implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 
     */
    private String account;

    /**
     * 
     */
    private String password;
    @TableField(exist = false)
    private String token;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}