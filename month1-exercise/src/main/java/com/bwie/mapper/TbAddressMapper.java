package com.bwie.mapper;

import com.bwie.pojo.TbAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author dsh
* @description 针对表【tb_address(地址信息表)】的数据库操作Mapper
* @createDate 2024-06-07 09:37:21
* @Entity com.bwie.pojo.TbAddress
*/
@Mapper
public interface TbAddressMapper extends BaseMapper<TbAddress> {

}




