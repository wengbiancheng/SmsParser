package com.example.qq.smsparser.controller.send;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qq.smsparser.MyApplication;
import com.example.qq.smsparser.R;
import com.example.qq.smsparser.controller.MainActivity;
import com.example.qq.smsparser.entity.OrderGood;
import com.example.qq.smsparser.entity.SendMessage;
import com.example.qq.smsparser.model.db.DbutilOrder;

/**
 * 订货信息的展示界面
 */
public class SendGoodMessageAty extends Activity implements View.OnClickListener{

    //标题栏控件初始化
    private LinearLayout title_left;
    private TextView title_middle;
    private Button title_right;

    private String orderId;
    private int sonFragmentFlag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_good_message);

        orderId=getIntent().getStringExtra("orderId");
        sonFragmentFlag=getIntent().getIntExtra("SonFragment",0);
//        initTest();
        initUI();
        initData();

        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                Log.e("TestService", "SendGoodMessageAty的进程名字是:"+appProcess.processName);
            }
        }
    }

    private void initTest(){
        orderId="12345679";
    }

    private void initUI() {
        title_left = (LinearLayout) findViewById(R.id.title_leftImageBtn);
        title_middle = (TextView) findViewById(R.id.title_middleTextView);
        title_right = (Button) findViewById(R.id.title_rightBtn);
        title_left.setOnClickListener(this);
        title_right.setOnClickListener(this);

        title_left.setVisibility(View.VISIBLE);
        title_middle.setText("发货信息");
    }

    private void initData(){
        SendMessage sendMessage= DbutilOrder.getInstance().getSendMessage(orderId,((MyApplication)this.getApplication()).getSQLiteOpenHelper().getReadableDatabase());

        Log.e("initData","初始化数据是:"+sendMessage.toString());
        ((TextView)findViewById(R.id.order_number)).setText(sendMessage.getOrder_id());
        ((TextView)findViewById(R.id.good_id)).setText(sendMessage.getGood_id());
        ((TextView)findViewById(R.id.good_name)).setText(sendMessage.getGood_name());

        ((TextView)findViewById(R.id.buy_name)).setText(sendMessage.getBuyer_name());
        ((TextView)findViewById(R.id.buy_address)).setText(sendMessage.getBuyer_address());
        ((TextView)findViewById(R.id.buy_phone)).setText(sendMessage.getBuyer_phone());
        ((TextView)findViewById(R.id.buy_postcard)).setText(sendMessage.getBuyer_postcard());

        ((TextView)findViewById(R.id.send_name)).setText(sendMessage.getDelivery_name());
        ((TextView)findViewById(R.id.send_time)).setText(sendMessage.getDelivery_time());
        ((TextView)findViewById(R.id.send_price)).setText(sendMessage.getDelivery_price()+"元");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_leftImageBtn:
                Intent intent=new Intent(this, MainActivity.class);
                intent.putExtra("Fragment",1);
                intent.putExtra("SonFragment",sonFragmentFlag);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }
}
