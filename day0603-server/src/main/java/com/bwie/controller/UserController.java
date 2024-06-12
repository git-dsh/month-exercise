package com.bwie.controller;

import com.bwie.pojo.TbUser;
import com.bwie.service.TbUserService;
import com.bwie.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: day0603_exercise
 * @author: 段帅虎
 * @description:
 * @create: 2024-06-03 20:11
 */
@RestController
@RequestMapping("/server/user")
public class UserController {
    @Autowired
    TbUserService tbUserService;
    @PostMapping("/login")
    public R login(@RequestBody TbUser tbUser){
        return tbUserService.login(tbUser);
    }
}
