package com.example.qq.smsparser.model.send;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.qq.smsparser.Configs;
import com.example.qq.smsparser.entity.HelperMessage;
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

    public void sendSms(OrderGood orderGood, HelperMessage helperMessage,ContentResolver contentResolver) {
        /**
         * 拼接短信内容
         */
        try {
            String content = "帮工:订单号:" + orderGood.getOrder_id() + ";商品号:" + orderGood.getGood_id()
                    + ";商品名称:" + orderGood.getGood_name()+";商品数量:"+orderGood.getGood_number()+";买家昵称:" + orderGood.getBuyer_name() +
                    ";买家地址:" + orderGood.getBuyer_address() + ";买家电话:" + orderGood.getBuyer_phone()+
                    ";买家邮编:" + orderGood.getBuyer_postcard()+";你的帮工号是:"+ helperMessage.getId();
//                    + ";请以下面的格式发回给我信息---" +
//                    "发货;订单号:订单号:" + orderGood.getOrder_id() + ";商品号:" + orderGood.getGood_id()+ ";帮工号:" + helperMessage.getId()
//                    + ";商品名称:" + orderGood.getGood_name() + ";买家昵称:" + orderGood.getBuyer_name() +
//                    ";买家地址:" + orderGood.getBuyer_address() + ";买家电话:" + orderGood.getBuyer_phone() +
//                    ";买家邮编:" + orderGood.getBuyer_postcard() + ";发货快递:(自己补充);发货时间:(自己补充);发货费用:(自己补充);是否发货:(是或者不是)";
            SmsManager sms = SmsManager.getDefault();
            if (content.length() > 70) {
                //拆分短信
                ArrayList<String> phoneList = sms.divideMessage(content);
                //发送短信
                sms.sendMultipartTextMessage(helperMessage.getPhone(), null, phoneList, null, null);
            } else {
                //不超过70字时使用sendTextMessage发送
                sms.sendTextMessage(helperMessage.getPhone(), null, content, null, null);
            }

            ContentValues values = new ContentValues();
            values.put("date", System.currentTimeMillis());
            values.put("read", 1);
            values.put("type", 2);
            values.put("address",helperMessage.getPhone());
            values.put("body", content);
            contentResolver.insert(Uri.parse("content://sms/sent"), values);

            Log.e("TestService", "SmsService:sendSms()发送短信成功;号码是:"+helperMessage.getPhone());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TestService", "SmsService:sendSms()发送短信失败");
        }
    }
}
