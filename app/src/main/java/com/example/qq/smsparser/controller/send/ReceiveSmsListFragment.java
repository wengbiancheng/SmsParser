package com.example.qq.smsparser.controller.send;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.example.qq.smsparser.MyApplication;
import com.example.qq.smsparser.R;
import com.example.qq.smsparser.controller.BaseFragment;
import com.example.qq.smsparser.controller.MainActivity;
import com.example.qq.smsparser.controller.adapter.HelperSmsAdapter;
import com.example.qq.smsparser.controller.adapter.SonFragmentAdapter;
import com.example.qq.smsparser.entity.HelperMessage;
import com.example.qq.smsparser.entity.OrderGood;
import com.example.qq.smsparser.entity.SmsMessage;
import com.example.qq.smsparser.model.db.DbutilHelper;
import com.example.qq.smsparser.model.db.DbutilSms;
import com.example.qq.smsparser.model.parser.SmsParserUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * 收到帮工短信列表
 */
public class ReceiveSmsListFragment extends BaseFragment {

    private ListView listView;
    private SonFragmentAdapter sms_adapter=null;
    private List<SmsMessage> list=new ArrayList<>();
    private HelperSmsAdapter help_adapter=null;
    private List<HelperMessage> data = new ArrayList<>();
    private Button back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Baseactivity= (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("Process", "HelperFragment:onCreateView");
        final View view = inflater.inflate(R.layout.fragment_helper, null);
        back= (Button) view.findViewById(R.id.helper_add_helper_btn);

        data = DbutilHelper.getInstance().getHelperListData(((MyApplication)Baseactivity.getApplication()).getSQLiteOpenHelper().getReadableDatabase());//得到所有的列表数据

        listView = (ListView) view.findViewById(R.id.helper_listView);
        help_adapter = new HelperSmsAdapter(Baseactivity, data);
        listView.setAdapter(help_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(listView.getAdapter()==help_adapter){
                    HelperMessage helperMessage=data.get(position);
                    list= DbutilSms.getInstance().getHelperSmsList(Baseactivity.getApplicationContext(),helperMessage.getPhone());
                    sms_adapter=new SonFragmentAdapter(Baseactivity,list);
                    listView.setAdapter(sms_adapter);
                    back.setVisibility(View.VISIBLE);
                }else if(listView.getAdapter()==sms_adapter){
                    Intent intent=new Intent(Baseactivity,SendGoodMessageAty.class);
                    String content=list.get(position).getBody();
                    OrderGood orderGood= SmsParserUtil.getInstance()
                            .getOrderData(content.substring(3,content.length()));
                    intent.putExtra("orderId",orderGood.getOrder_id());
                    startActivity(intent);
                }
            }
        });

        back.setText("返回上一页");
        back.setVisibility(View.GONE);
        view.findViewById(R.id.helper_add_helper_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listView.getAdapter()==sms_adapter){
                    if(Baseactivity.isFlag()){
                        data = DbutilHelper.getInstance().getHelperListData(((MyApplication)Baseactivity.getApplication()).getSQLiteOpenHelper().getReadableDatabase());//得到所有的列表数据
                        help_adapter = new HelperSmsAdapter(Baseactivity, data);
                        Baseactivity.setFlag(false);
                    }
                    listView.setAdapter(help_adapter);
                    back.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }
}
