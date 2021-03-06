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
import com.example.qq.smsparser.entity.OrderGood;
import com.example.qq.smsparser.entity.SmsMessage;
import com.example.qq.smsparser.model.db.DbutilSms;
import com.example.qq.smsparser.model.parser.SmsParserUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 订货短信的展示界面
 */
public class OrderGoodSmsListFragment extends BaseFragment{

    private ListView listView;
    private SonFragmentAdapter adapter;
    private List<SmsMessage> list=new ArrayList<>();

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
                String content=list.get(i).getBody();
                OrderGood orderGood= SmsParserUtil.getInstance()
                        .getOrderData(content.substring(3,content.length()));
                intent.putExtra("orderId",orderGood.getOrder_id());
                intent.putExtra("SonFragment",0);
                startActivity(intent);
                Baseactivity.finish();
            }
        });
        list= DbutilSms.getInstance().getOrderSmsList(Baseactivity.getApplicationContext());
        if(list==null){
            list=new ArrayList<>();
        }
        adapter=new SonFragmentAdapter(Baseactivity,list);
        listView.setAdapter(adapter);
        return view;
    }
}
