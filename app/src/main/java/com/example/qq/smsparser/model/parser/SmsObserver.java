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

import com.example.qq.smsparser.Configs;
import com.example.qq.smsparser.entity.SmsMessage;
import com.example.qq.smsparser.model.db.DbutilHelper;
import com.example.qq.smsparser.model.db.MySQLiteHelper;

import java.util.List;

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
//        getSmsFromPhone(null);
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
    private void getSmsFromPhone() {
        Log.e("TestService", "触发了SmsObserver:getSmsFromPhone()方法:进行想要短信内容的查询");
        ContentResolver contentResolver = context.getContentResolver();
        //TODO Android 6.0版本需要显式地请求权限

        String[] projection = new String[]{"_id", "address", "body", "date"};
        String where = "date>" + (System.currentTimeMillis() - 30 * 1000);//30秒内收到的短信
        Cursor cur = contentResolver.query(SMS_INBOX, projection, where, null, "_id desc");//TODO 要设置这些短信为已读
        //当使用date desc进行排序的时候，同个电话号码可以得到我们想要的序列，但是当多个电话号码同时到达的时候，会导致了获取到的cursor发生错乱
        //因此我们还是使用_id进行排序，就可以正确地获取先后顺序到达的短信内容了
        Log.e("TestService", "获取短信数量是:" + cur.getCount());

        if (cur == null || cur.getCount() == 0) return;
        cur.moveToFirst();

        //TODO 解析短信，一共有三种类型的短信
        Message message = new Message();
        Bundle bundle = new Bundle();

        int id = cur.getInt(cur.getColumnIndex("_id"));
        String number = cur.getString(cur.getColumnIndex("address"));//手机号
        String body = cur.getString(cur.getColumnIndex("body"));//短信具体内容
        String date = cur.getString(cur.getColumnIndex("date"));
        cur.close();
        //TODO 短信检索就先利用号码进行过滤，然后判断前两个字符来选出 订货短信或者付款短信，就不存入数据库了。
        SmsMessage smsMessage = new SmsMessage();
        String type = body.substring(0, 1);
        String content = body.substring(3, body.length() - 1);

        Log.e("TestService", "收到的短信的号码是:" + number);
        if (number.equals(Configs.SMS_ORDER_AND_PAY_NUMBER)) {
            if (type.equals("订货")) {
                smsMessage.setType(0);
            } else if (type.equals("付款")) {
                smsMessage.setType(1);
            }
        } else {
            List<String> helper = DbutilHelper.getInstance().getHelperPhone(sqLiteHelper.getWritableDatabase());
            if (helper.contains(number)) {
                if (type.equals("发货")) {
                    smsMessage.setType(2);
                }
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

