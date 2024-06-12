package com.bwie.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: day0603_exercise
 * @author: 段帅虎
 * @description:
 * @create: 2024-06-03 20:26
 */
@Data
public class PageVo implements Serializable {
    private Integer pageNum;
    private Integer pageSize;
}
