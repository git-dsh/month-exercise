package com.bwie.service;

import com.bwie.pojo.TbJoiner;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bwie.util.R;
import com.bwie.vo.PageVo;
import freemarker.template.TemplateException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* @author dsh
* @description 针对表【tb_joiner】的数据库操作Service
* @createDate 2024-06-03 20:07:20
*/
public interface TbJoinerService extends IService<TbJoiner> {

    R joinerList(PageVo pageVo);

    R saveJoiner(TbJoiner tbJoiner, String token) throws TemplateException, IOException;

    R uploadImg(MultipartFile file) throws IOException;

    R uploadVideo(MultipartFile file) throws IOException;

    R voteJoiner(Integer joinerId, String token);
}
