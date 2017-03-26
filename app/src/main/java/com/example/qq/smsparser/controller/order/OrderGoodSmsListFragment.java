package com.example.qq.smsparser.controller.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.qq.smsparser.R;
import com.example.qq.smsparser.controller.BaseFragment;
import com.example.qq.smsparser.controller.MainActivity;
import com.example.qq.smsparser.controller.adapter.SonFragmentAdapter;

/**
 * 订货短信的展示界面
 */
public class OrderGoodSmsListFragment extends BaseFragment{

    private ListView listView;
    private SonFragmentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Baseactivity= (MainActivity) getActivity();
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("Process","OrderGoodSmsListFragment:onCreateView");
        View view=inflater.inflate(R.layout.sonfragment_order_good, null);
        listView= (ListView) view.findViewById(R.id.sonfragment_listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(Baseactivity,OrderGoodMessageAty.class);
                startActivity(intent);
            }
        });
        adapter=new SonFragmentAdapter(Baseactivity);
        listView.setAdapter(adapter);
        return view;
    }
}
