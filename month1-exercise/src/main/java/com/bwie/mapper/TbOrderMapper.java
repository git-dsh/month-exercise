package com.bwie.mapper;

import com.bwie.pojo.TbOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bwie.vo.EsOrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author dsh
* @description 针对表【tb_order(订单表)】的数据库操作Mapper
* @createDate 2024-06-07 09:37:21
* @Entity com.bwie.pojo.TbOrder
*/
@Mapper
public interface TbOrderMapper extends BaseMapper<TbOrder> {

    List<EsOrderVo> selectOrderList(@Param("updateTime") Date updateTime);
}




