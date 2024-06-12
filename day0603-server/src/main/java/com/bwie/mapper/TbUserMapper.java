package com.bwie.mapper;

import com.bwie.pojo.TbUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author dsh
* @description 针对表【tb_user】的数据库操作Mapper
* @createDate 2024-06-03 21:12:18
* @Entity com.bwie.pojo.TbUser
*/
@Mapper
public interface TbUserMapper extends BaseMapper<TbUser> {

}




