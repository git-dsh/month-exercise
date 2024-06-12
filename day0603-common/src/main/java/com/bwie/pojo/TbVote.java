package com.bwie.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName tb_vote
 */
@TableName(value ="tb_vote")
@Data
public class TbVote implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer joinerId;

    /**
     * 
     */
    private Integer userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}