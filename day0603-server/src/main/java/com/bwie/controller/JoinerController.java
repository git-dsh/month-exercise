package com.bwie.controller;

import com.bwie.pojo.TbJoiner;
import com.bwie.service.TbJoinerService;
import com.bwie.util.R;
import com.bwie.vo.PageVo;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @program: day0603_exercise
 * @author: 段帅虎
 * @description:
 * @create: 2024-06-03 20:23
 */
@RestController
@RequestMapping("/server/joiner")
public class JoinerController {
    @Autowired
    TbJoinerService tbJoinerService;
    @PostMapping("/joinerList")
    public R joinerList(@RequestBody PageVo pageVo){
        return tbJoinerService.joinerList(pageVo);
    }
    @PostMapping("/saveJoiner")
    public R saveJoiner(@RequestBody TbJoiner tbJoiner, @RequestHeader("token")String token) throws TemplateException, IOException {
        return tbJoinerService.saveJoiner(tbJoiner,token);
    }
    @PostMapping("/uploadImg")
    public R uploadImg(@RequestPart("file")MultipartFile file) throws IOException {
        return tbJoinerService.uploadImg(file);
    }
    @PostMapping("/uploadVideo")
    public R uploadVideo(@RequestPart("file")MultipartFile file) throws IOException {
        return tbJoinerService.uploadVideo(file);
    }
    @PostMapping("/voteJoiner/{joinerId}")
    public R voteJoiner(@PathVariable("joinerId")Integer joinerId,@RequestHeader("token")String token) throws IOException {
        return tbJoinerService.voteJoiner(joinerId,token);
    }

}
