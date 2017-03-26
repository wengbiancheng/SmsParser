package com.example.qq.smsparser.controller.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.qq.smsparser.controller.send.ReceiveSmsListFragment;
import com.example.qq.smsparser.controller.send.SendSmsListFragment;

import java.util.ArrayList;

public class SendGoodFragmentControl {
    private int containerId;
    private FragmentManager fm;
    private ArrayList<Fragment> fragmentList;

    private static SendGoodFragmentControl controller;

    public static SendGoodFragmentControl getInstance(){
        if(controller==null){
            controller=new SendGoodFragmentControl();
        }
        return controller;
    }

    public static void onDestroy() {
        controller = null;
    }


    private SendGoodFragmentControl(){
        initFragment();
    }

    //初始化fragment
    private void initFragment(){
        fragmentList=new ArrayList<Fragment>();

        fragmentList.add(new SendSmsListFragment());
        fragmentList.add(new ReceiveSmsListFragment());
    }

    public Fragment getFragment(int position){
        return fragmentList.get(position);
    }


}
