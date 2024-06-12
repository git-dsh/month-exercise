package com.bwie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bwie.pojo.TbAddress;
import com.bwie.service.TbAddressService;
import com.bwie.mapper.TbAddressMapper;
import org.springframework.stereotype.Service;

/**
* @author dsh
* @description 针对表【tb_address(地址信息表)】的数据库操作Service实现
* @createDate 2024-06-07 09:37:21
*/
@Service
public class TbAddressServiceImpl extends ServiceImpl<TbAddressMapper, TbAddress>
    implements TbAddressService{

}




