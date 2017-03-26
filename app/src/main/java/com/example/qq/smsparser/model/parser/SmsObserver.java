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
import com.example.qq.smsparser.model.db.DbutilHelper;
import com.example.qq.smsparser.model.db.MySQLiteHelper;
import com.example.qq.smsparser.entity.SmsMessage;

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

    public SmsObserver(Context context, Handler handler,MySQLiteHelper sqLiteHelper) {
        super(handler);
        this.context=context;
        this.handler=handler;
        this.sqLiteHelper=sqLiteHelper;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        //每当有新短信到来时，使用我们获取短消息的方法
        Log.e("TestService","SmsObserver:onChange()");
        getSmsFromPhone();
    }

    /**
     * 得到最近收到的短信，跟数据库的帮工号码比较，得到帮工的短信
     */
    public void getSmsFromPhone() {
        Log.e("TestService","SmsObserver:getSmsFromPhone()");
        ContentResolver cr = context.getContentResolver();
//        String[] projection = new String[] { "_id", "address", "body","date"};
//        String where = "date>"+ (System.currentTimeMillis() - 10* 1000);//10秒内收到的短信
//        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        Cursor cur = cr.query(Uri.parse("content://sms"),
                new String[] { "_id", "address", "read", "body", "thread_id" ,"date"},
                "date>?", new String[] { "10*1000" }, "date desc");
        Log.e("TestService","SmsObserver:getSmsFromPhone()：搜索到的短信数量是:"+cur.getCount());

        if (cur==null) return;


        //TODO 解析短信，一共有三种类型的短信
        Message message=new Message();
        Bundle bundle=new Bundle();

        if (cur.moveToNext()) {//只搜索一条短信
            int id=cur.getInt(cur.getColumnIndex("_id"));
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String body = cur.getString(cur.getColumnIndex("body"));//短信具体内容
            String date=cur.getString(cur.getColumnIndex("date"));
            //TODO 短信检索就先利用号码进行过滤，然后判断前两个字符来选出 订货短信或者付款短信，就不存入数据库了。
            SmsMessage smsMessage=new SmsMessage();
            String type=body.substring(0,1);
            String content=body.substring(3,body.length()-1);

            Log.e("TestService","SmsObserver:搜索到的短信号码是:"+number);
            if(number.equals(Configs.SMS_ORDER_AND_PAY_NUMBER)){
                if(type.equals("订货")){
                    smsMessage.setType(0);
                }else if(type.equals("付款")){
                    smsMessage.setType(1);
                }
            }else{
                List<String> helper=DbutilHelper.getInstance().getHelperPhone(sqLiteHelper.getWritableDatabase());
                if(helper.contains(number)) {
                    if(type.equals("发货")){
                        smsMessage.setType(2);
                    }
                }
            }
            smsMessage.setBody(content);
            smsMessage.setId(id);
            smsMessage.setNumber(number);
            smsMessage.setTime(date);

            bundle.putSerializable("sms",smsMessage);
            message.setData(bundle);
            message.what=0;
            handler.sendMessage(message);
        }
    }
}

