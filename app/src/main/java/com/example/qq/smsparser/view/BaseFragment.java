package com.example.qq.smsparser.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.qq.smsparser.controller.db.DbutilHelper;
import com.example.qq.smsparser.controller.db.DbutilOrder;

public class BaseFragment extends Fragment {

    protected MainActivity Baseactivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Baseactivity= (MainActivity) getActivity();

    }

}
