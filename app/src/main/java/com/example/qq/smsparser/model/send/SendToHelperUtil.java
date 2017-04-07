package com.example.qq.smsparser.model.send;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;
import com.example.qq.smsparser.entity.OrderGood;
import java.util.ArrayList;

/**
 * 发送给帮工短信的工具类，主要调用短信接口进行数据的发送操作
 */
public class SendToHelperUtil {

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
        //TODO  发送的电话号码部分需要提取帮工的数据库表数据
        /**
         * 拼接短信内容
         */
        try {
            String content = "帮工:订单号:" + orderGood.getOrder_id() + ";商品号:" + orderGood.getGood_id()
                    + ";商品名称:" + orderGood.getGood_name() + ";买家昵称:" + orderGood.getBuyer_name() +
                    ";买家地址:" + orderGood.getBuyer_address() + ";买家电话:" + orderGood.getBuyer_phone() +
                    ";买家邮编:" + orderGood.getBuyer_postcard() + ";请以下面的格式发回给我信息---" +
                    "发货;订单号:订单号:" + orderGood.getOrder_id() + ";商品号:" + orderGood.getGood_id()
                    + ";商品名称:" + orderGood.getGood_name() + ";买家昵称:" + orderGood.getBuyer_name() +
                    ";买家地址:" + orderGood.getBuyer_address() + ";买家电话:" + orderGood.getBuyer_phone() +
                    ";买家邮编:" + orderGood.getBuyer_postcard() + ";发货快递:(自己补充);发货时间:(自己补充);发货费用:(自己补充);是否发货:(是或者不是)";
            SmsManager sms = SmsManager.getDefault();
            if (content.length() > 70) {
                //拆分短信
                ArrayList<String> phoneList = sms.divideMessage(content);
                //发送短信
                sms.sendMultipartTextMessage("+8615608443963", null, phoneList, null, null);
            } else {
                //不超过70字时使用sendTextMessage发送
                sms.sendTextMessage("+8615608443963", null, content, null, null);
            }
            Log.e("TestService", "SmsService:sendSms()发送短信成功");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TestService", "SmsService:sendSms()发送短信失败");
        }
    }
}
