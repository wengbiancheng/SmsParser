package com.example.qq.smsparser.model.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.qq.smsparser.Configs;
import com.example.qq.smsparser.entity.SmsMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库的操作类，主要进行短信数据的创建和销毁，数据库的增删查改等操作
 */
public class DbutilSms {

    private Uri SMS_INBOX = Uri.parse("content://sms/inbox");
    private Uri SMS_OUTBOX=Uri.parse("content://sms/sent");
    private static DbutilSms dbutils=null;

    public static DbutilSms getInstance(){
        if(dbutils==null){
            dbutils=new DbutilSms();
            return dbutils;
        }
        return dbutils;
    }

    public List<SmsMessage> getOrderSmsList(Context context){
        List<SmsMessage> list=new ArrayList<>();

        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[] { "_id", "address", "body","date"};

        Cursor cur = cr.query(SMS_INBOX, projection, "address=?", new String[]{Configs.SMS_SERVER_NUMBER}, "date desc");
        if (cur==null) return null;

        Log.e("SmsTest","得到的订货短信的cur数量是:"+cur.getCount());
        cur.moveToFirst();
        int count=cur.getCount();
        for(int i=0;i<count;i++){
            int id=cur.getInt(cur.getColumnIndex("_id"));
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String body = cur.getString(cur.getColumnIndex("body"));//短信具体内容
            String date=cur.getString(cur.getColumnIndex("date"));

            String type = body.substring(0, 2);
            if(type.equals("订货")){
                SmsMessage smsMessage=new SmsMessage();

                smsMessage.setBody(body);
                smsMessage.setId(id);
                smsMessage.setNumber(number);
                smsMessage.setTime(date);
                smsMessage.setType(0);
                list.add(smsMessage);
            }
            cur.moveToNext();
        }
        Log.e("SmsTest","得到的订货短信的数量是:"+list.size());
        return list;
    }

    public List<SmsMessage> getPaySmsList(Context context){
        List<SmsMessage> list=new ArrayList<>();

        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[] { "_id", "address", "body","date"};

        Cursor cur = cr.query(SMS_INBOX, projection, "address=?", new String[]{Configs.SMS_SERVER_NUMBER}, "date desc");
        if (cur==null) return null;

        cur.moveToFirst();
        int count=cur.getCount();
        for(int i=0;i<count;i++){
            int id=cur.getInt(cur.getColumnIndex("_id"));
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String body = cur.getString(cur.getColumnIndex("body"));//短信具体内容
            String date=cur.getString(cur.getColumnIndex("date"));

            String type = body.substring(0, 2);
            if(type.equals("付款")){
                SmsMessage smsMessage=new SmsMessage();

                smsMessage.setBody(body);
                smsMessage.setId(id);
                smsMessage.setNumber(number);
                smsMessage.setTime(date);
                smsMessage.setType(1);
                list.add(smsMessage);
            }
            cur.moveToNext();
        }
        Log.e("SmsTest","得到的付款短信的数量是:"+list.size());
        return list;
    }

    public List<SmsMessage> getHelperSmsList(Context context,String phone){
        List<SmsMessage> list=new ArrayList<>();

        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[] { "_id", "address", "body","date"};

        Cursor cur = cr.query(SMS_INBOX, projection, "address=?", new String[]{phone}, "date desc");
        if (cur==null) return null;

        cur.moveToFirst();
        int count=cur.getCount();
        for(int i=0;i<count;i++){
            int id=cur.getInt(cur.getColumnIndex("_id"));
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String body = cur.getString(cur.getColumnIndex("body"));//短信具体内容
            String date=cur.getString(cur.getColumnIndex("date"));

            String type = body.substring(0, 2);
            if(type.equals("发货")){
                SmsMessage smsMessage=new SmsMessage();

                smsMessage.setBody(body);
                smsMessage.setId(id);
                smsMessage.setNumber(number);
                smsMessage.setTime(date);
                smsMessage.setType(2);
                list.add(smsMessage);
            }
            cur.moveToNext();
        }
        Log.e("SmsTest","得到的发货短信的数量是:"+list.size());
        return list;
    }

    public List<SmsMessage> getSendHelperSmsList(Context context,String phone){
        List<SmsMessage> list=new ArrayList<>();

        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[] { "_id", "address", "body","date"};

        //TODO 第二个有问题的地方
        Cursor cur = cr.query(SMS_OUTBOX, projection, "address=?", new String[]{phone}, "date desc");
        if (cur==null) return null;

        cur.moveToFirst();
        int count=cur.getCount();
        for(int i=0;i<count;i++){
            int id=cur.getInt(cur.getColumnIndex("_id"));
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String body = cur.getString(cur.getColumnIndex("body"));//短信具体内容
            String date=cur.getString(cur.getColumnIndex("date"));

            String type = body.substring(0, 2);
            if(type.equals("帮工")){
                SmsMessage smsMessage=new SmsMessage();

                smsMessage.setBody(body);
                smsMessage.setId(id);
                smsMessage.setNumber(number);
                smsMessage.setTime(date);
                smsMessage.setType(2);
                list.add(smsMessage);
            }
            cur.moveToNext();
        }
        Log.e("SmsTest","发送给帮工的数量是:"+list.size());
        return list;
    }
}
