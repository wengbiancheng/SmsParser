package com.example.qq.smsparser.controller.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.example.qq.smsparser.controller.order.OrderGoodSmsListFragment;
import com.example.qq.smsparser.controller.order.PaySmsListFragment;

import java.util.ArrayList;

public class OrderGoodFragmentControl {
    private int containerId;
    private FragmentManager fm;
    private ArrayList<Fragment> fragmentList;

    private static OrderGoodFragmentControl controller;

    public static OrderGoodFragmentControl getInstance(){
        if(controller==null){
            controller=new OrderGoodFragmentControl();
        }
        return controller;
    }

    public static void onDestroy() {
        controller = null;
    }


    private OrderGoodFragmentControl(){
        initFragment();
    }

    //初始化fragment
    private void initFragment(){
        fragmentList=new ArrayList<Fragment>();

        fragmentList.add(new OrderGoodSmsListFragment());
        fragmentList.add(new PaySmsListFragment());
    }

    public Fragment getFragment(int position){
        return fragmentList.get(position);
    }


}
