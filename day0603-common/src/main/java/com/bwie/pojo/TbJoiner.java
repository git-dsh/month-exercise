package com.bwie.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName tb_joiner
 */
@TableName(value ="tb_joiner")
@Data
public class TbJoiner implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date birthday;

    /**
     * 
     */
    private String avatar;

    /**
     * 
     */
    private String joinVideo;


    /**
     * 
     */
    private String joinerAddress;
    /**
     * 
     */
    private String joinerHtml;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}