package com.example.qq.smsparser.model.parser;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.qq.smsparser.entity.OrderGood;
import com.example.qq.smsparser.entity.PayMessage;
import com.example.qq.smsparser.entity.SendMessage;

/**
 * 短信解析类，根据传来的String类型，解析成我们构造的实体类，然后再进行相应的返回操作
 */
public class SmsParserUtil {

    private static SmsParserUtil smsParserUtil =null;

    public static SmsParserUtil getInstance(){
        if(smsParserUtil ==null){
            smsParserUtil =new SmsParserUtil();
            return smsParserUtil;
        }
        return smsParserUtil;
    }

    public OrderGood getOrderData(String content){
        Log.e("TestService","SmsParserUtil:getOrderData()");
        OrderGood orderGood=new OrderGood();
        String[] data=content.split("；");
        for(int i=0;i<data.length;i++){
            String[] item=data[i].split("：");
            if(item[0].equals("订单号")){
                orderGood.setOrder_id(item[1]);
            }else if(item[0].equals("商品号")){
                orderGood.setGood_id(item[1]);
            }else if(item[0].equals("商品名称")){
                orderGood.setGood_name(item[1]);
            }else if(item[0].equals("购买数量")){
                orderGood.setGood_number(Integer.parseInt(item[1]));
            }else if(item[0].equals("商品单价")){
                orderGood.setGood_price(Float.parseFloat(item[1]));
            }else if(item[0].equals("需要支付金额")){
                orderGood.setCost(Float.parseFloat(item[1]));
            }else if(item[0].equals("买家昵称")){
                orderGood.setBuyer_name(item[1]);
            }else if(item[0].equals("买家地址")){
                orderGood.setBuyer_address(item[1]);
            }else if(item[0].equals("买家电话")){
                orderGood.setBuyer_phone(item[1]);
            }else if(item[0].equals("买家邮编")){
                orderGood.setBuyer_postcard(item[1]);
            }
        }
        Log.e("TestService","SmsParserUtil解析得到的订货数据是:"+orderGood.toString());
        return orderGood;
    }

    public PayMessage getPayMessage(String content) {
        Log.e("TestService","SmsParserUtil:getPayMessage()");
        PayMessage paymessage=new PayMessage();
        String[] data=content.split("；");
        for(int i=0;i<data.length;i++) {
            String[] item = data[i].split("：");
            if(item[0].equals("订单号")){
                paymessage.setOrder_id(item[1]);
            }else if(item[0].equals("是否付款")){
                if(item[1].equals("是")){
                    paymessage.setPay(true);
                }else{
                    paymessage.setPay(false);
                }
            }
        }
        Log.e("TestService","SmsParserUtil解析得到的付款数据是:"+paymessage.toString());
        return paymessage;
    }

    public SendMessage getSendMessage(String content) {
        Log.e("TestService","SmsParserUtil:getSendMessage()");
        SendMessage sendMessage=new SendMessage();
        String[] data=content.split("；");
        for(int i=0;i<data.length;i++) {
            String[] item = data[i].split("：");
            if(item[0].equals("订单号")){
                sendMessage.setOrder_id(item[1]);
            }else if(item[0].equals("商品号")){
                sendMessage.setGood_id(item[1]);
            }else if(item[0].equals("帮工号")){
                sendMessage.setHelper_id(Integer.parseInt(item[1]));
            }else if(item[0].equals("商品名称")){
                sendMessage.setGood_name(item[1]);
            }else if(item[0].equals("买家昵称")){
                sendMessage.setBuyer_name(item[1]);
            }else if(item[0].equals("买家地址")){
                sendMessage.setBuyer_address(item[1]);
            }else if(item[0].equals("买家电话")){
                sendMessage.setBuyer_phone(item[1]);
            }else if(item[0].equals("买家邮编")){
                sendMessage.setBuyer_postcard(item[1]);
            }else if(item[0].equals("发货快递")){
                sendMessage.setDelivery_name("申通快递");
            }else if(item[0].equals("发货时间")){
                sendMessage.setDelivery_time(item[1]+":"+item[2]);
            }else if(item[0].equals("发货费用")){
                sendMessage.setDelivery_price(Float.parseFloat(item[1]));
            }else if(item[0].equals("是否发货")){
                sendMessage.setSend(item[1].equals("是")?true:false);
            }
        }
        Log.e("TestService","SmsParserUtil解析得到的发货数据是:"+sendMessage.toString());
        return sendMessage;
    }

    public String getOrderId(String content){
        String[] data1=content.split("---");
        String[] data=data1[0].split("；");
        for(int i=0;i<data.length;i++) {
            String[] item = data[i].split("：");
            if (item[0].equals("订单号")) {
                return item[1];
            }
        }
        return null;
    }
}
