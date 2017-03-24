package com.example.qq.smsparser.view.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.example.qq.smsparser.view.analysis.SaleListFragment;
import com.example.qq.smsparser.view.helper.HelperFragment;
import com.example.qq.smsparser.view.order.OrderGoodFragment;
import com.example.qq.smsparser.view.send.SendGoodFragment;
import java.util.ArrayList;

/**
 * MainActivity的Fragment界面的总控制器
 */
public class MainFragmentController {

    private int containerId;
    private FragmentManager fm;
    private ArrayList<Fragment> fragmentList;

    private static MainFragmentController controller;

    public static MainFragmentController getInstance(FragmentActivity activity, int containerId){
        if(controller==null){
            controller=new MainFragmentController(activity,containerId);
        }
        return controller;
    }

    public static void onDestroy() {
        controller = null;
    }


    private MainFragmentController(FragmentActivity activity, int containerId){
        this.containerId=containerId;
        fm=activity.getSupportFragmentManager();
        initFragment();
    }

    //初始化fragment
    private void initFragment(){
        fragmentList=new ArrayList<Fragment>();

        fragmentList.add(new OrderGoodFragment());
        fragmentList.add(new SendGoodFragment());
        fragmentList.add(new SaleListFragment());
        fragmentList.add(new HelperFragment());

        FragmentTransaction ft=fm.beginTransaction();
        for(Fragment fragment:fragmentList){
            ft.add(containerId,fragment);
        }

        ft.commit();
    }

    public Fragment getFragment(int position){
        return fragmentList.get(position);
    }

    public int getPosition(Fragment fragment){
        for(int i=0;i<fragmentList.size();i++){
            if(fragment==fragmentList.get(i)){
                return i;
            }
        }
        return -1;
    }

    public void showFragment(int position){
        hideFragments();
        Fragment fragment=fragmentList.get(position);
        FragmentTransaction ft=fm.beginTransaction();
        ft.show(fragment);
        ft.commit();
    }

    public void hideFragments(){
        FragmentTransaction ft=fm.beginTransaction();
        for(Fragment fragment:fragmentList){
            if(fragment!=null){
                ft.hide(fragment);
            }
        }
        ft.commit();
    }
}
