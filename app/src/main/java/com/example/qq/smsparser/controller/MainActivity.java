package com.example.qq.smsparser.controller;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.qq.smsparser.R;
import com.example.qq.smsparser.entity.OrderGood;
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

    //帮工数据是否修改的标志位
    private boolean flag=false;
    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = MainFragmentController.getInstance(this, R.id.fl_content);

        initUI();

        //申请相应的权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS}, 0);
        }

        //TODO 进行后台的测试工作，测试成功后再开启新的进程变成后台运行的
        initTestService();
    }


    /**
     * 测试后台数据
     */
    private void initTestService() {
        Log.e("TestService", "startService");
        Intent intent = new Intent(MainActivity.this, SmsService.class);
        this.startService(intent);
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

        controller.showFragment(0);
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
    protected void onDestroy() {
        super.onDestroy();
        MainFragmentController.onDestroy();
    }

}
