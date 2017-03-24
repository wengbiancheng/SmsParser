package com.example.qq.smsparser.view.order;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.qq.smsparser.view.BaseFragment;
import com.example.qq.smsparser.R;
import com.example.qq.smsparser.view.adapter.SonFragmentPagerAdapter;

/**
 * 订货信息界面的总界面
 */
public class OrderGoodFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup rg_tab = null;
    private ViewPager viewPager;
    private SonFragmentPagerAdapter adapter;

    private RadioButton orderGoodSms,paySms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("Process","OrderGoodFragment:onCreateView");
        View view=inflater.inflate(R.layout.fragment_order_good,null);
        rg_tab= (RadioGroup) view.findViewById(R.id.booklist_rg_tab);
        viewPager= (ViewPager) view.findViewById(R.id.orderGood_viewPager);
        adapter=new SonFragmentPagerAdapter(this.getChildFragmentManager(),false);
        viewPager.setAdapter(adapter);
        rg_tab.setOnCheckedChangeListener(this);

        orderGoodSms= (RadioButton) view.findViewById(R.id.order_good_sms_list);
        paySms= (RadioButton) view.findViewById(R.id.pay_sms_list);

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
