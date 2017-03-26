package com.example.qq.smsparser.model.send;

import android.content.Context;
import android.telephony.SmsManager;

import com.example.qq.smsparser.entity.OrderGood;


/**
 * 发送给帮工短信的工具类，主要调用短信接口进行数据的发送操作
 */
public class SendToHelperUtil {

    //TODO 需要从数据库中获取相应的帮工数据，然后进行相应的处理操作
    private static SendToHelperUtil sendToHelperUtil;
    private Context context;

    private SendToHelperUtil(Context context) {
        this.context = context;
    }

    public static SendToHelperUtil getInstance(Context context) {
        if (sendToHelperUtil == null) {
            sendToHelperUtil = new SendToHelperUtil(context);
            return sendToHelperUtil;
        }
        return sendToHelperUtil;
    }

    public void sendSms(OrderGood orderGood) {
//        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
//        sendIntent.setData(Uri.parse("smsto:" + phone));
//        sendIntent.putExtra("sms_body", content);
//        context.startActivity(sendIntent);

        /**
         * 拼接短信内容
         */
        String content="商家信息;订单号:"+orderGood.getOrder_id()+";商品号:"+orderGood.getGood_id()
                +";商品名称:"+orderGood.getGood_name()+";买家昵称:"+orderGood.getBuyer_name()+
                ";买家地址:"+orderGood.getBuyer_address()+";买家电话:"+orderGood.getBuyer_phone()+
                ";买家邮编:"+orderGood.getBuyer_postcard()+";请以下面的格式发回给我信息---"+
                "发货;订单号:订单号:"+orderGood.getOrder_id()+";商品号:"+orderGood.getGood_id()
                +";商品名称:"+orderGood.getGood_name()+";买家昵称:"+orderGood.getBuyer_name()+
                ";买家地址:"+orderGood.getBuyer_address()+";买家电话:"+orderGood.getBuyer_phone()+
                ";买家邮编:"+orderGood.getBuyer_postcard()+";发货快递:(自己补充);发货时间:(自己补充);+发货费用:(自己补充)。";

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(orderGood.getBuyer_phone(), "18814122731", content, null, null);
    }
}
