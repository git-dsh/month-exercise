package com.bwie.service;

import com.bwie.pojo.TbUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bwie.util.R;

/**
* @author dsh
* @description 针对表【tb_user】的数据库操作Service
* @createDate 2024-06-03 20:07:20
*/
public interface TbUserService extends IService<TbUser> {

    R login(TbUser tbUser);
}
