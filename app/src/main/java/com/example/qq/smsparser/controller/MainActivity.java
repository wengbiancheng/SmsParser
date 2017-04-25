package com.example.qq.smsparser.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.qq.smsparser.MyApplication;
import com.example.qq.smsparser.R;
import com.example.qq.smsparser.ServiceTwo;
import com.example.qq.smsparser.model.parser.SmsService;
import com.example.qq.smsparser.controller.utils.MainFragmentController;

/**
 * 主界面，主要用来合并几个零散的界面
 */
public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {

    private MainFragmentController controller;

    //底部类
    private RadioGroup rg_tab;
    private RadioButton rb_order, rb_send, rb_sale, rb_helper;

    //标题栏控件初始化
    private LinearLayout title_left;
    private TextView title_middle;
    private Button title_right;

    private Uri SMS_INBOX = Uri.parse("content://sms/inbox");

    private int FragmentFlag=0;
    public  int SonFragmentFlag=0;

    //帮工数据是否修改的标志位
    private boolean flag=false;
    private boolean flag1=false;
    public boolean isFlag() {
        return flag||flag1;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public void setFlag1(boolean flag1){
        this.flag1=flag1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentFlag=getIntent().getIntExtra("Fragment",0);
        SonFragmentFlag=getIntent().getIntExtra("SonFragment",0);

        controller = MainFragmentController.getInstance(this, R.id.fl_content);
        Log.e("Process1","MainActivity：OnCreate()");
        initUI();

        //申请相应的权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS}, 0);
        }

        initService();
    }


    /**
     * 后台开启
     */
    private void initService() {
        Intent serviceOne = new Intent();
        serviceOne.setClass(MainActivity.this, SmsService.class);
        startService(serviceOne);

        Intent serviceTwo = new Intent();
        serviceTwo.setClass(MainActivity.this, ServiceTwo.class);
        startService(serviceTwo);
    }

    private void initUI() {
        rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
        rb_order = (RadioButton) findViewById(R.id.main_rb_order);
        rb_send = (RadioButton) findViewById(R.id.main_rb_send);
        rb_sale = (RadioButton) findViewById(R.id.main_rb_sale);
        rb_helper = (RadioButton) findViewById(R.id.main_rb_helper);

        rg_tab.setOnCheckedChangeListener(this);

        title_left = (LinearLayout) findViewById(R.id.title_leftImageBtn);
        title_middle = (TextView) findViewById(R.id.title_middleTextView);
        title_right = (Button) findViewById(R.id.title_rightBtn);
        title_middle.setText("订货信息列表");

        if(FragmentFlag==0){
            controller.showFragment(0);
        }else{
            if(FragmentFlag==1){
                ((RadioButton) findViewById(R.id.main_rb_send)).setChecked(true);
            }else if(FragmentFlag==2){
                ((RadioButton) findViewById(R.id.main_rb_sale)).setChecked(true);
            }else if(FragmentFlag==3){
                ((RadioButton) findViewById(R.id.main_rb_helper)).setChecked(true);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_rb_order:
                controller.showFragment(0);
                title_middle.setText("订货信息列表");
                break;
            case R.id.main_rb_send:
                controller.showFragment(1);
                title_middle.setText("发货信息列表");
                break;
            case R.id.main_rb_sale:
                controller.showFragment(2);
                title_middle.setText("销售数据展示");
                break;
            case R.id.main_rb_helper:
                controller.showFragment(3);
                title_middle.setText("帮工数据列表");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {

        MainFragmentController.onDestroy();
        finish();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainFragmentController.onDestroy();
    }

}
