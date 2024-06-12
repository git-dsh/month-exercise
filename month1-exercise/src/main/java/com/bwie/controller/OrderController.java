package com.bwie.controller;

import com.bwie.pojo.TbOrder;
import com.bwie.pojo.TbShop;
import com.bwie.service.TbOrderService;
import com.bwie.util.R;
import com.bwie.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

/**
 * @program: day0603_exercise
 * @author: 段帅虎
 * @description:
 * @create: 2024-06-07 09:42
 */
@RestController
@RequestMapping("/order/order")
public class OrderController {
    @Autowired
    TbOrderService tbOrderService;
    @PostMapping("/orderList")
    public R orderList(@RequestBody OrderVo orderVo) throws ParseException {
        return tbOrderService.orderList(orderVo);
    }
    @PostMapping("/saveOrder")
    public R saveOrder(@RequestBody TbOrder tbOrder,@RequestHeader("token")String token){
        return tbOrderService.saveOrder(tbOrder,token);
    }
    @PostMapping("/addressList")
    public R addressList(){
        return tbOrderService.addressList();
    }
    @PostMapping("/payMethodList")
    public R payMethodList(){
        return tbOrderService.payMethodList();
    }
    @PostMapping("/payStatusList")
    public R payStatusList(){
        return tbOrderService.payStatusList();
    }
    @PostMapping("/saveShop/{orderId}")
    public R saveShop(@PathVariable("orderId")Integer orderId, @RequestBody TbShop tbShop){
        return tbOrderService.saveShop(orderId,tbShop);
    }
    @PostMapping("/selectShopList/{orderId}")
    public R selectShopList(@PathVariable("orderId")Integer orderId){
        return tbOrderService.selectShopList(orderId);
    }
    @PostMapping("/upload")
    public R upload(@RequestPart("file")MultipartFile file) throws IOException {
        return tbOrderService.upload(file);
    }
    @PostMapping("/createIndex")
    public R createIndex()  {
        return tbOrderService.createIndex();
    }
    @PostMapping("/orderStatus")
    public R orderStatus()  {
        return tbOrderService.orderStatus();
    }
    @PostMapping("/saveDriverOrder/{orderId}")
    public R saveDriverOrder(@PathVariable("orderId")Integer orderId,@RequestHeader("token")String token){
        return tbOrderService.saveDriverOrder(orderId,token);
    }
}
