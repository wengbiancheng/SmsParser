package com.example.qq.smsparser.model.parser;

import android.Manifest;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.qq.smsparser.MyApplication;
import com.example.qq.smsparser.ServiceTwo;
import com.example.qq.smsparser.entity.HelperMessage;
import com.example.qq.smsparser.model.db.DbutilHelper;
import com.example.qq.smsparser.model.db.DbutilOrder;
import com.example.qq.smsparser.model.db.MySQLiteHelper;
import com.example.qq.smsparser.entity.OrderGood;
import com.example.qq.smsparser.entity.PayMessage;
import com.example.qq.smsparser.entity.SendMessage;
import com.example.qq.smsparser.entity.SmsMessage;
import com.example.qq.smsparser.model.send.SendToHelperUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 运行在后台的Service，有短信到达的时候触发这个Service进行短信获取，然后
 * 进行相应的跳转到相应的短信
 * 自动化处理的中心
 */
public class SmsService extends Service {

    private SmsObserver smsObserver = null;
    private SmsParserUtil smsParserUtil = null;
    private SendToHelperUtil sendToHelperUtil = null;
    private Uri SMS_INBOX = Uri.parse("content://sms");


    private SmsMessage smsMessage = new SmsMessage();
    private OrderGood orderGood = new OrderGood();
    private PayMessage payMessage = new PayMessage();
    private SendMessage sendMessage = new SendMessage();
    private MySQLiteHelper mySQLiteHelper;

    public final static String TAG = "ServiceLife";
    private Timer timer = new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TestService", "SmsService:onCreate()");
        mySQLiteHelper = ((MyApplication) this.getApplication()).getSQLiteOpenHelper();
        smsObserver = new SmsObserver(this, handler, mySQLiteHelper);
        smsParserUtil = SmsParserUtil.getInstance();
        sendToHelperUtil = SendToHelperUtil.getInstance(this);

        thread.start();
        this.getContentResolver().registerContentObserver(SMS_INBOX, true, smsObserver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "SmsService:onStartCommand");

        return START_STICKY;
    }

//    private void initOrderTestData() {
//        int orderId = 12345679;
//        float cost = 10.5f;
//        for (int i = 0; i < 4; i++) {
//            String content = "订单号:" + orderId + ";商品号:87654322,123456;商品名称:蓝天水杯,鸿天纸巾;" +
//                    "购买数量:3,5;商品单价:15.5,20.0;需要支付金额:" + cost + ";买家昵称:小明买家;" +
//                    "买家地址:湖南省长沙市;买家电话:456123789;买家邮编:126456";
//            SmsMessage smsMessage1 = new SmsMessage();
//            smsMessage1.setBody(content);
//            smsMessage1.setId(1);
//            smsMessage1.setNumber("+8618814122731");
//            smsMessage1.setTime(System.currentTimeMillis() + "");
//            smsMessage1.setType(0);
//
//            Message message = new Message();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("sms", smsMessage1);
//            message.setData(bundle);
//            message.what = 0;
//            handler.sendMessage(message);
//
//            orderId++;
//            cost = cost + 5;
//        }
//    }
//    private void initPayTestData() {
//        int orderId = 12345679;
//        for (int i = 0; i < 4; i++) {
//            String content = "订单号:" + orderId + ";是否付款:是";
//
//            SmsMessage smsMessage1 = new SmsMessage();
//            smsMessage1.setBody(content);
//            smsMessage1.setId(1);
//            smsMessage1.setNumber("+8618814122731");
//            smsMessage1.setTime(System.currentTimeMillis() + "");
//            smsMessage1.setType(1);
//
//            Message message = new Message();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("sms", smsMessage1);
//            message.setData(bundle);
//            message.what = 0;
//            handler.sendMessage(message);
//
//            orderId++;
//        }
//    }
//    private void initSendTestData() {
//        int orderId = 12345679;
//        int month = 1;
//        for (int i = 0; i < 4; i++) {
//            String content = "订单号:" + orderId + ";商品号:87654322,123456;帮工号:1;商品名称:蓝天水杯,鸿天纸巾;" +
//                    "买家昵称:小明买家;买家地址:湖南省长沙市;买家电话:456123789;买家邮编:126456;" +
//                    "发货快递:申通快递;发货时间:2017-0" + month + "-01 15:36;发货费用:5.5;是否发货:是";
//            SmsMessage smsMessage1 = new SmsMessage();
//            smsMessage1.setBody(content);
//            smsMessage1.setId(1);
//            smsMessage1.setNumber("+8618814122731");
//            smsMessage1.setTime(System.currentTimeMillis() + "");
//            smsMessage1.setType(2);
//
//            Message message = new Message();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("sms", smsMessage1);
//            message.setData(bundle);
//            message.what = 0;
//            handler.sendMessage(message);
//
//            orderId++;
//            month++;
//        }
//    }

    /**
     * 每当有短信到来的时候，就会发送到Handler中来
     */
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    smsMessage = (SmsMessage) message.getData().getSerializable("sms");
                    Log.e("TestService", "SmsService:handler得到的数据是:" + smsMessage.getBody() + ";smsType:" + smsMessage.getType());

                    if (smsMessage.getType() == 0) {
                        orderGood = smsParserUtil.getOrderData(smsMessage.getBody());
                        saveOrderMessage(orderGood);
                    } else if (smsMessage.getType() == 1) {
                        payMessage = smsParserUtil.getPayMessage(smsMessage.getBody());
                        //TODO 第一个有问题的地方
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int result=updateOrderMessage(payMessage);
                                if(result==1){
                                    OrderGood orderGood = DbutilOrder.getInstance().getOrderGood(payMessage.getOrder_id(), mySQLiteHelper.getReadableDatabase());
                                    HelperMessage helperMessage= DbutilHelper.getInstance().getHelperMessage(mySQLiteHelper.getReadableDatabase());
                                    if(helperMessage!=null){
                                        sendSmsToHelper(orderGood,helperMessage);
                                    }else{
                                        Log.e("TestService","未选择帮工,无法发送短信");
                                    }
                                }
                            }
                        }).start();

                    } else if (smsMessage.getType() == 2) {
                        sendMessage = smsParserUtil.getSendMessage(smsMessage.getBody());
                        updateOrderMessage(sendMessage);
                    }
                    break;
                default:
                    Log.e("TestService", "SmsService:handleMessage():default");
                    break;
            }
            return true;
        }
    });

    //TODO 短信解析完成后，我们应该新建一个方法调用Dbutils提供的接口进行数据的存取，注意是多种数据进行存取的
    private void saveOrderMessage(OrderGood orderGood) {
        long result = DbutilOrder.getInstance().saveOrderMessage(orderGood, mySQLiteHelper.getWritableDatabase());
        Log.e("TestService", "SmsService:saveOrderMessage():result:" + result);
    }

    private int updateOrderMessage(PayMessage payMessage) {
        int result = DbutilOrder.getInstance().updateOrderMessage(payMessage, mySQLiteHelper.getReadableDatabase(), mySQLiteHelper.getWritableDatabase());
        Log.e("TestService", "SmsService:updateOrderMessage(PayMessage):result:" + result);
        return result;
    }

    private void updateOrderMessage(SendMessage sendMessage) {
        int result = DbutilOrder.getInstance().updateOrderMessage(sendMessage, mySQLiteHelper.getReadableDatabase(), mySQLiteHelper.getWritableDatabase());
        Log.e("TestService", "SmsService:updateOrderMessage(SendMessage):result:" + result);
    }

    //TODO 短信解析完成后，我们应该新建一个方法调用SendToHelper的短信接口，进行帮工短信的发送（注意多线程）
    private void sendSmsToHelper(OrderGood orderGood,HelperMessage helperMessage) {
        Log.e("TestService", "要发送的电话号码是:"+helperMessage.getPhone());
        Log.e("TestService", "调用了sendSmsToHelper方法:要发送的数据是:"+orderGood.toString());
        sendToHelperUtil.sendSms(orderGood,helperMessage,this.getContentResolver());
    }

    private Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    boolean b = MyApplication.isServiceWorked(SmsService.this, "com.example.qq.smsparser.ServiceTwo");
                    Log.e(TAG, "ServiceTwo的存活情况是:"+b);
                    if(!b) {
                        Intent service = new Intent(SmsService.this, ServiceTwo.class);
                        startService(service);
                    }
                }
            };
            timer.schedule(task, 0, 10*60*1000);
        }
    });

    @Override
    public void onDestroy() {
        smsObserver = null;
        smsParserUtil = null;
        sendToHelperUtil = null;
        getContentResolver().unregisterContentObserver(smsObserver);

        timer.cancel();
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "SmsService onDestroy");
        super.onDestroy();
    }
}