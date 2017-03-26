package com.example.qq.smsparser.model.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.qq.smsparser.Configs;
import com.example.qq.smsparser.entity.SmsMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库的操作类，主要进行短信数据的创建和销毁，数据库的增删查改等操作
 */
public class DbutilSms {

    private Uri SMS_INBOX = Uri.parse("content://sms/");


    public List<SmsMessage> getOrderSmsList(Context context){
        List<SmsMessage> list=new ArrayList<>();

        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[] { "_id", "address", "body","date"};
        String where =" address="+ Configs.SMS_ORDER_AND_PAY_NUMBER;

        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        if (cur==null) return null;

        cur.moveToFirst();
        int count=cur.getCount();
        for(int i=0;i<count;i++){
            int id=cur.getInt(cur.getColumnIndex("_id"));
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String body = cur.getString(cur.getColumnIndex("body"));//短信具体内容
            String date=cur.getString(cur.getColumnIndex("date"));

            String type=body.substring(0,1);
            if(type.equals("订货")){
                SmsMessage smsMessage=new SmsMessage();

                smsMessage.setBody(body);
                smsMessage.setId(id);
                smsMessage.setNumber(number);
                smsMessage.setTime(date);
                smsMessage.setType(0);
                list.add(smsMessage);
            }
        }
        return list;
    }

    public List<SmsMessage> getPaySmsList(Context context){
        List<SmsMessage> list=new ArrayList<>();

        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[] { "_id", "address", "body","date"};
        String where =" address="+ Configs.SMS_ORDER_AND_PAY_NUMBER;

        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        if (cur==null) return null;

        cur.moveToFirst();
        int count=cur.getCount();
        for(int i=0;i<count;i++){
            int id=cur.getInt(cur.getColumnIndex("_id"));
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String body = cur.getString(cur.getColumnIndex("body"));//短信具体内容
            String date=cur.getString(cur.getColumnIndex("date"));

            String type=body.substring(0,1);
            if(type.equals("付款")){
                SmsMessage smsMessage=new SmsMessage();

                smsMessage.setBody(body);
                smsMessage.setId(id);
                smsMessage.setNumber(number);
                smsMessage.setTime(date);
                smsMessage.setType(1);
                list.add(smsMessage);
            }
        }
        return list;
    }

    public List<SmsMessage> getHelperSmsList(Context context){
        List<SmsMessage> list=new ArrayList<>();

        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[] { "_id", "address", "body","date"};
        String where =" address="+ Configs.SMS_ORDER_AND_PAY_NUMBER;

        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        if (cur==null) return null;

        cur.moveToFirst();
        int count=cur.getCount();
        for(int i=0;i<count;i++){
            int id=cur.getInt(cur.getColumnIndex("_id"));
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String body = cur.getString(cur.getColumnIndex("body"));//短信具体内容
            String date=cur.getString(cur.getColumnIndex("date"));

            String type=body.substring(0,1);
            if(type.equals("发货")){
                SmsMessage smsMessage=new SmsMessage();

                smsMessage.setBody(body);
                smsMessage.setId(id);
                smsMessage.setNumber(number);
                smsMessage.setTime(date);
                smsMessage.setType(2);
                list.add(smsMessage);
            }
        }
        return list;
    }
}
