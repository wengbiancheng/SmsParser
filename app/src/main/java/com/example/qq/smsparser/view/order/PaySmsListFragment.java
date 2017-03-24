package com.example.qq.smsparser.view.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.qq.smsparser.R;
import com.example.qq.smsparser.view.BaseFragment;
import com.example.qq.smsparser.view.MainActivity;
import com.example.qq.smsparser.view.adapter.SonFragmentAdapter;

/**
 * 付款短信列表，主要用来展示收到的付款短信列表
 */
public class PaySmsListFragment extends BaseFragment {

    private ListView listView;
    private SonFragmentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Baseactivity= (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("Process","PaySmsListFragment:onCreateView");
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

