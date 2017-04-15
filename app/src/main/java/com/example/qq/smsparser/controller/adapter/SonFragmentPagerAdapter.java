package com.example.qq.smsparser.controller.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.qq.smsparser.controller.utils.OrderGoodFragmentControl;
import com.example.qq.smsparser.controller.utils.SendGoodFragmentControl;

/**
 * OrderGoodFragment和SendGoodFragment的ViewPager的适配器
 */
public class SonFragmentPagerAdapter extends FragmentPagerAdapter {

    public static final String[] TITLES = new String[] {"订货短信列表","付款短信列表"};
    public static final String[] TITLES2 = new String[] {"发送帮工短信列表","收到帮工短信列表"};
    private boolean flag=false;

    public SonFragmentPagerAdapter(FragmentManager fm,boolean flag)
    {
        super(fm);


        this.flag=flag;
    }

    @Override
    public Fragment getItem(int arg0){
        int position=arg0%TITLES.length;
        return flag? SendGoodFragmentControl.getInstance().getFragment(position):
                OrderGoodFragmentControl.getInstance().getFragment(position);
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return flag?TITLES2[position % TITLES2.length]:TITLES[position % TITLES.length];
    }

    @Override
    public int getCount()
    {
        return flag?TITLES2.length:TITLES.length;
    }

}
