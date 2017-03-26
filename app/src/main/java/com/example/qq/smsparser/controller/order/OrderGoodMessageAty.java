package com.example.qq.smsparser.controller.order;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qq.smsparser.R;

/**
 * 订货信息的展示界面
 */
public class OrderGoodMessageAty extends Activity implements View.OnClickListener{

    //标题栏控件初始化
    private LinearLayout title_left;
    private TextView title_middle;
    private Button title_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_good_message);

        initUI();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_leftImageBtn:
                onBackPressed();
                break;
        }
    }
}
