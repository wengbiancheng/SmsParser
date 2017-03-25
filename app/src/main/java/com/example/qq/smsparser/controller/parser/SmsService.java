package com.example.qq.smsparser.controller.parser;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.qq.smsparser.MyApplication;
import com.example.qq.smsparser.controller.db.DbutilOrder;
import com.example.qq.smsparser.controller.db.DbutilHelper;
import com.example.qq.smsparser.controller.db.MySQLiteHelper;
import com.example.qq.smsparser.entity.OrderGood;
import com.example.qq.smsparser.entity.PayMessage;
import com.example.qq.smsparser.entity.SendMessage;
import com.example.qq.smsparser.entity.SmsMessage;
import com.example.qq.smsparser.controller.send.SendToHelperUtil;

/**
 * 运行在后台的Service，有短信到达的时候触发这个Service进行短信获取，然后
 * 进行相应的跳转到相应的短信
 * 自动化处理的中心
 */
public class SmsService extends Service {

    private SmsObserver smsObserver=null;
    private SmsParserUtil smsParserUtil =null;
    private SendToHelperUtil sendToHelperUtil =null;

    private SmsMessage smsMessage=new SmsMessage();
    private OrderGood orderGood=new OrderGood();
    private PayMessage payMessage=new PayMessage();
    private SendMessage sendMessage=new SendMessage();
    private MySQLiteHelper mySQLiteHelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TestService","SmsService:onCreate()");
        mySQLiteHelper=((MyApplication)this.getApplication()).getSQLiteOpenHelper();
        smsObserver=new SmsObserver(this,handler,mySQLiteHelper);
        smsParserUtil = SmsParserUtil.getInstance(this,handler);
        sendToHelperUtil = SendToHelperUtil.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TestService","SmsService:onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("TestService","SmsService:onDestroy()");
        smsObserver=null;
        smsParserUtil =null;
        sendToHelperUtil =null;
        super.onDestroy();
    }

    /**
     * 每当有短信到来的时候，就会发送到Handler中来
     */
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 0:
                    Log.e("TestService","SmsService:handleMessage()");
                    smsMessage= (SmsMessage) message.getData().getSerializable("sms");
                    if(smsMessage.getType()==0){
                        orderGood= smsParserUtil.getOrderData(smsMessage.getBody());
                        saveOrderMessage(orderGood);
                    }else if(smsMessage.getType()==1){
                        //TODO 得到了付款短信，这个时候应该只是更新下数据库信息就好了
                        payMessage= smsParserUtil.getPayMessage(smsMessage.getBody());
                        updateOrderMessage(payMessage);
                        //TODO 应该发送短信给帮工，而发送给帮工的话，就得需要购买者的一些信息了
                        OrderGood orderGood=DbutilOrder.getInstance().getOrderGood(payMessage.getOrder_id(),mySQLiteHelper.getReadableDatabase());
                        sendSmsToHelper(orderGood);
                    }else if(smsMessage.getType()==2){
                        sendMessage= smsParserUtil.getSendMessage(smsMessage.getBody());
                        updateOrderMessage(sendMessage);
                    }
                    break;
            }
            return true;
        }
    });

    //TODO 短信解析完成后，我们应该新建一个方法调用Dbutils提供的接口进行数据的存取，注意是多种数据进行存取的
    private void saveOrderMessage(OrderGood orderGood){
        Log.e("TestService","SmsService:saveOrderMessage()");
        DbutilOrder.getInstance().saveOrderMessage(orderGood,mySQLiteHelper.getWritableDatabase());
    }

    private void updateOrderMessage(PayMessage payMessage){
        Log.e("TestService","SmsService:updateOrderMessage(PayMessage)");
        DbutilOrder.getInstance().updateOrderMessage(payMessage,mySQLiteHelper.getReadableDatabase(),mySQLiteHelper.getWritableDatabase());
    }

    private void updateOrderMessage(SendMessage sendMessage){
        Log.e("TestService","SmsService:updateOrderMessage(SendMessage)");
        DbutilOrder.getInstance().updateOrderMessage(sendMessage,mySQLiteHelper.getReadableDatabase(),mySQLiteHelper.getWritableDatabase());
    }

    //TODO 短信解析完成后，我们应该新建一个方法调用SendToHelper的短信接口，进行帮工短信的发送（注意多线程）
    private void sendSmsToHelper(OrderGood orderGood){
        Log.e("TestService","SmsService:sendSmsToHelper()");
        sendToHelperUtil.sendSms(orderGood);
    }
}