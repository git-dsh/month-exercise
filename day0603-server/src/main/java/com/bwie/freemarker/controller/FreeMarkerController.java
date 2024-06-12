package com.bwie.freemarker.controller;

import com.bwie.pojo.TbJoiner;
import com.bwie.service.TbJoinerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @program: day0603_exercise
 * @author: 段帅虎
 * @description:
 * @create: 2024-06-04 11:24
 */
@Controller
@RequestMapping("/server/freemarker")
public class FreeMarkerController {
    @Autowired
    TbJoinerService tbJoinerService;
    @GetMapping("/joinerHtml/{id}")
    public ModelAndView joinerHtml(@PathVariable("id")Integer id){
        ModelAndView modelAndView = new ModelAndView("vote");
        TbJoiner tbJoiner = tbJoinerService.getById(id);
        modelAndView.addObject("tbJoiner",tbJoiner);
        return modelAndView;
    }
}
