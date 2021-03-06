package com.example.qq.smsparser.model.parser;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.qq.smsparser.MyApplication;
import com.example.qq.smsparser.entity.HelperMessage;
import com.example.qq.smsparser.entity.SmsMessage;
import com.example.qq.smsparser.model.db.DbutilHelper;
import com.example.qq.smsparser.model.db.MySQLiteHelper;

/**
 * 获取短信类，通过onChange进行相应的回调，有订货短信、付款短信和帮工短信共三种形式的短信
 * 解析判断是哪种短信，然后交给SmsParser进行相应的处理工作
 */
class SmsObserver extends ContentObserver {

    private Context context;
    private Uri SMS_INBOX = Uri.parse("content://sms/inbox");
    private Handler handler;
    private MySQLiteHelper sqLiteHelper;

    public SmsObserver(Context context, Handler handler, MySQLiteHelper sqLiteHelper) {
        super(handler);
        this.context = context;
        this.handler = handler;
        this.sqLiteHelper = sqLiteHelper;
    }

//    @Override
//    public void onChange(boolean selfChange) {
//        super.onChange(selfChange);
//
//        Log.e("TestService", "触发了SmsObserver:onChange()");
//        getSmsFromPhone();
//
//    }

    /**
     * 高版本的onChange()方法
     * @param selfChange
     * @param uri
     */
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);

        Log.e("TestService", "触发了SmsObserver:onChange()+uri：" + uri.toString());
        if (uri.toString().equals("content://sms/raw") || uri.toString().equals("content://sms/inbox")) {
            return;
        }
        getSmsFromPhone();
        //TODO 存在问题是:当你发送短信息的时候 也会自动调用这个onChange()方法，这显然是我们所不想看到的
    }

    /**
     * 得到最近收到的短信，跟数据库的帮工号码比较，得到帮工的短信
     */
    private synchronized void getSmsFromPhone() {
        Log.e("TestService", "触发了SmsObserver:getSmsFromPhone()方法:进行想要短信内容的查询");
        ContentResolver contentResolver = context.getContentResolver();
        //TODO Android 6.0版本需要显式地请求权限

        String[] projection = new String[]{"_id", "address", "body", "date"};
        Cursor cur = contentResolver.query(SMS_INBOX, projection, "date>?", new String[]{(System.currentTimeMillis() - 30 * 1000)+""}, "_id desc");//TODO 要设置这些短信为已读
        //当使用date desc进行排序的时候，同个电话号码可以得到我们想要的序列，但是当多个电话号码同时到达的时候，会导致了获取到的cursor发生错乱
        //因此我们还是使用_id进行排序，就可以正确地获取先后顺序到达的短信内容了
        Log.e("TestService", "获取短信数量是:" + cur.getCount());

        if (cur == null || cur.getCount() == 0){
            Log.e("TestService", "===========retrun===========");
            return;
        }
        cur.moveToFirst();

        Message message = new Message();
        Bundle bundle = new Bundle();

        int id = cur.getInt(cur.getColumnIndex("_id"));
        String number = cur.getString(cur.getColumnIndex("address"));//手机号
        String body = cur.getString(cur.getColumnIndex("body"));//短信具体内容
        String date = cur.getString(cur.getColumnIndex("date"));
        cur.close();
        SmsMessage smsMessage = new SmsMessage();

        if(body.length()<4){
            Log.e("TestService","该短信不是我们想要的");
            return;
        }
        String type = body.substring(0, 2);
        String content = body.substring(3, body.length());

        if (body.length()>12&&(body.substring(8,10).equals("订货")||body.substring(8,10).equals("付款"))) {

            Log.e("SmsTest","修改之前的电话号码是:"+MyApplication.getServerNumber(context));
            //SharedPreferences存储短信平台的号码，如果有变化，则更新号码
            if(!number.equals(MyApplication.getServerNumber(context))){
                MyApplication.writeServerNumber(number,context);
                Log.e("SmsTest","修改之后的电话号码11是:"+MyApplication.getServerNumber(context));
            }

            Log.e("SmsTest","修改之后的电话号码22是:"+MyApplication.getServerNumber(context));

            type=body.substring(8,10);
            content=body.substring(11,body.length());
            if (type.equals("订货")) {
                smsMessage.setType(0);
            } else if (type.equals("付款")) {
                smsMessage.setType(1);
            }
            Log.e("TestService", "收到的短信的号码是:" + number+";短信内容的前两个字是:"+type);
        } else {
            Log.e("TestService", "收到的短信的号码是:" + number+";短信内容的前两个字是:"+type);
            HelperMessage helperMessage = DbutilHelper.getInstance().getHelperMessage(sqLiteHelper.getWritableDatabase());
            Log.e("TestService","搜索得到的帮工电话号码是:"+helperMessage.getPhone()+";短信的电话号码是:"+number);
            if (helperMessage.getPhone().equals(number)) {
                if (type.equals("发货")) {
                    smsMessage.setType(2);
                }
            }else{
                return;
            }
        }
        smsMessage.setBody(content);
        smsMessage.setId(id);
        smsMessage.setNumber(number);
        smsMessage.setTime(date);

        bundle.putSerializable("sms", smsMessage);
        message.setData(bundle);
        message.what = 0;
        handler.sendMessage(message);
    }
}

