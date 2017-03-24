package com.example.qq.smsparser.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.qq.smsparser.controller.db.DbutilHelper;
import com.example.qq.smsparser.controller.db.DbutilOrder;

public class BaseFragment extends Fragment {

    protected MainActivity Baseactivity;
    protected DbutilOrder dbutilOrder;
    protected DbutilHelper dbutilHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Baseactivity= (MainActivity) getActivity();
        dbutilOrder=DbutilOrder.getInstance(Baseactivity.getApplicationContext());
        dbutilHelper=DbutilHelper.getInstance(Baseactivity.getApplicationContext());
    }

}
