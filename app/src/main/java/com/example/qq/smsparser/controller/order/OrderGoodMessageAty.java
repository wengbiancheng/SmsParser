package com.example.qq.smsparser.controller.order;

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
import com.example.qq.smsparser.model.db.DbutilHelper;
import com.example.qq.smsparser.model.db.DbutilOrder;

/**
 * 订货信息的展示界面
 */
public class OrderGoodMessageAty extends Activity implements View.OnClickListener{

    //标题栏控件初始化
    private LinearLayout title_left;
    private TextView title_middle;
    private Button title_right;

    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_good_message);

        orderId=getIntent().getStringExtra("orderId");
//        initTest();
        initUI();
        initData();

        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                Log.e("TestService", "OrderGoodMessageAty的进程名字是:"+appProcess.processName);
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
        title_middle.setText("订货信息");
    }

    private void initData(){
        OrderGood orderGood=DbutilOrder.getInstance().getOrderGood(orderId,((MyApplication)this.getApplication()).getSQLiteOpenHelper().getReadableDatabase());

        Log.e("initData","初始化数据是:"+orderGood.toString());
        ((TextView)findViewById(R.id.order_number)).setText(orderGood.getOrder_id());
        ((TextView)findViewById(R.id.good_id)).setText(orderGood.getGood_id());
        ((TextView)findViewById(R.id.good_name)).setText(orderGood.getGood_name());
        ((TextView)findViewById(R.id.good_number)).setText(orderGood.getGood_number()+"");
        ((TextView)findViewById(R.id.good_price)).setText(orderGood.getGood_price()+" 元/件");
        ((TextView)findViewById(R.id.good_total_price)).setText(orderGood.getCost()+"元");
        ((TextView)findViewById(R.id.buy_name)).setText(orderGood.getBuyer_name());
        ((TextView)findViewById(R.id.buy_address)).setText(orderGood.getBuyer_address());
        ((TextView)findViewById(R.id.buy_phone)).setText(orderGood.getBuyer_phone());
        ((TextView)findViewById(R.id.buy_postcard)).setText(orderGood.getBuyer_postcard());
        ((TextView)findViewById(R.id.is_pay)).setText(orderGood.getPayMessage().isPay()?"是":"否");
        ((TextView)findViewById(R.id.is_send)).setText(orderGood.isSend()?"是":"否");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_leftImageBtn:
                Intent intent=new Intent(this, MainActivity.class);
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
