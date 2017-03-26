package com.example.qq.smsparser.controller.send;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.qq.smsparser.controller.BaseFragment;
import com.example.qq.smsparser.R;
import com.example.qq.smsparser.controller.adapter.SonFragmentPagerAdapter;

/**
 * 发货信息展示的总界面
 */
public class SendGoodFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup rg_tab = null;
    private ViewPager viewPager;
    private SonFragmentPagerAdapter adapter;

    private RadioButton SendSms,ReceiveSms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("Process","SendGoodFragment:onCreateView");
        View view=inflater.inflate(R.layout.fragment_send_good,null);
        rg_tab= (RadioGroup) view.findViewById(R.id.booklist_rg_tab);
        viewPager= (ViewPager) view.findViewById(R.id.orderGood_viewPager);
        adapter=new SonFragmentPagerAdapter(this.getChildFragmentManager(),true);
        viewPager.setAdapter(adapter);
        rg_tab.setOnCheckedChangeListener(this);

        SendSms= (RadioButton) view.findViewById(R.id.order_good_sms_list);
        ReceiveSms= (RadioButton) view.findViewById(R.id.pay_sms_list);
        SendSms.setText("发送帮工短信列表");
        ReceiveSms.setText("收到帮工短信列表");

        viewPager.setCurrentItem(0);
        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.order_good_sms_list:
                viewPager.setCurrentItem(0);
                break;
            case R.id.pay_sms_list:
                viewPager.setCurrentItem(1);
                break;
        }
    }
}
