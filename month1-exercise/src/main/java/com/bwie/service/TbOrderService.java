package com.bwie.service;

import com.bwie.pojo.TbOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bwie.pojo.TbShop;
import com.bwie.util.R;
import com.bwie.vo.OrderVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

/**
* @author dsh
* @description 针对表【tb_order(订单表)】的数据库操作Service
* @createDate 2024-06-07 09:37:21
*/
public interface TbOrderService extends IService<TbOrder> {

    R orderList(OrderVo orderVo) throws ParseException;

    R saveOrder(TbOrder tbOrder, String token);

    R addressList();


    R payMethodList();


    R payStatusList();

    R saveShop(Integer orderId, TbShop tbShop);

    R selectShopList(Integer orderId);

    R upload(MultipartFile file) throws IOException;

    R createIndex();

    R orderStatus();

    R saveDriverOrder(Integer orderId, String token);
}
