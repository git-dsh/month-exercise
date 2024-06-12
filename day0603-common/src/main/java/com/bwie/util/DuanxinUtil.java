package com.bwie.util;

import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class DuanxinUtil {

    public static void main(String[] args) {
        sendDuanxin("18801391312","1234");
    }

    public static Boolean sendDuanxin(String mobile,String code){
        String host = "http://dingxin.market.alicloudapi.com";
        String path = "/dx/sendSms";
        String method = "POST";
//      String appcode = "你自己的AppCode"6a18d9667b6c4d66a551cca15df78da6;
//      String appcode = "32sgfe563pdow6p3sm456w2pd1gdd463sh";
        String appcode = "6a18d9667b6c4d66a551cca15df78da6";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", mobile);
        querys.put("param", "code:"+code);
        //模板id,联系客服人员申请成功的模板ID
        querys.put("tpl_id", "TP20010711");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
            //发送过程无异常，返回true
            return true;
        } catch (Exception e) {
            //获取并打印异常
            e.printStackTrace();
            //返回值为假false
            return false;
        }
    }
}
