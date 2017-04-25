package com.example.qq.smsparser.controller.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qq.smsparser.MyApplication;
import com.example.qq.smsparser.R;
import com.example.qq.smsparser.controller.analysis.SaleMessageAty;
import com.example.qq.smsparser.entity.OrderSaleMessage;
import com.example.qq.smsparser.model.db.DbutilHelper;
import com.example.qq.smsparser.entity.HelperMessage;
import com.example.qq.smsparser.controller.BaseFragment;
import com.example.qq.smsparser.controller.adapter.HelperAdapter;
import com.example.qq.smsparser.model.db.DbutilOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 帮工界面，主要用来对帮工的数据进行增删查改，然后也涉及到数据库的操作
 */
public class HelperFragment extends BaseFragment {

    private ListView listView;
    private HelperAdapter adapter;

    private List<HelperMessage> data = new ArrayList<>();
    public final  String PHONE_PATTERN = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            int position=message.arg1;
            boolean isChecked= (boolean) message.obj;
            HelperMessage helperMessage=data.get(position);
            helperMessage.setCheck(isChecked);
            if(message.what==11){
                //执行数据库的更新操作
                int result=DbutilHelper.getInstance().updateHelper(helperMessage,((MyApplication)Baseactivity.getApplication()).getSQLiteOpenHelper().getReadableDatabase(),
                        ((MyApplication)Baseactivity.getApplication()).getSQLiteOpenHelper().getWritableDatabase());
                Log.e("SQLite","HelperFragment:updateHelper的结果是:"+result);
                if(result!=-1){
                    data.set(position,helperMessage);
                    adapter.notifyDataSetChanged();
                    Baseactivity.setFlag(true);
                    Baseactivity.setFlag1(true);
                    Toast.makeText(Baseactivity,"选择成功",Toast.LENGTH_SHORT).show();

                    int count=0;
                    for(int i=0;i<data.size();i++){
                        if(data.get(i).isCheck()){
                            count++;
                        }
                    }
                    if(count<1){
                        new AlertDialog.Builder(Baseactivity).setTitle("警告").setMessage("你应该选择一个帮工，请点击进行选择")
                                .setPositiveButton("确定",null).create().show();
                    }else if(count>1){
                        new AlertDialog.Builder(Baseactivity).setTitle("警告").setMessage("你应该只选择一个帮工，请点击进行选择")
                                .setPositiveButton("确定",null).create().show();
                    }
                }else{
                    Toast.makeText(Baseactivity,"选择失败",Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("Process", "HelperFragment:onCreateView");
        View view = inflater.inflate(R.layout.fragment_helper, null);

        data = DbutilHelper.getInstance().getHelperListData(((MyApplication)Baseactivity.getApplication()).getSQLiteOpenHelper().getReadableDatabase());//得到所有的列表数据

        listView = (ListView) view.findViewById(R.id.helper_listView);
        adapter = new HelperAdapter(Baseactivity, data,handler);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Baseactivity, HelperMessageAty.class);
                List<OrderSaleMessage> list= DbutilOrder.getInstance().getAllHelperSaleMessage(((MyApplication)Baseactivity.getApplication()).
                        getSQLiteOpenHelper().getReadableDatabase(),data.get(i).getId());
                if(list==null){
                    list=new ArrayList<OrderSaleMessage>();
                }
                float sale=0,helper_cost=0,send_cost=0,other_cost=0,profit=0;
                for(int t=0;t<list.size();t++){
                    sale=sale+list.get(t).getGood_price();
                    helper_cost=helper_cost+list.get(t).getHelper_cost();
                    send_cost=send_cost+list.get(t).getDelivery_price();
                    other_cost=other_cost+list.get(t).getOther_cost();
                    profit=profit+list.get(t).getProfit();
                }
                intent.putExtra("sale",sale);
                intent.putExtra("helper_cost",helper_cost);
                intent.putExtra("send_cost",send_cost);
                intent.putExtra("other_cost",other_cost);
                intent.putExtra("profit",profit);
                intent.putExtra("number",list.size());
                intent.putExtra("helperName",data.get(i).getName());
                startActivity(intent);
                Log.e("TestHelper","点击的帮工的id是:"+data.get(i).getId());
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                DeleteHelper(position);
                return true;
            }
        });


        view.findViewById(R.id.helper_add_helper_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHelper();
            }
        });
        return view;
    }

    private void DeleteHelper(final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(Baseactivity).setMessage("您真的要删除这个帮工信息吗?").setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int code=DbutilHelper.getInstance().deleteHelper(data.get(position).getId(),((MyApplication)Baseactivity.getApplication()).getSQLiteOpenHelper().getWritableDatabase());
                        if(code==1){
                            data.remove(position);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(Baseactivity,"删除成功",Toast.LENGTH_SHORT).show();
                            Baseactivity.setFlag(true);
                            Baseactivity.setFlag1(true);
                            int count=0;
                            for(int i=0;i<data.size();i++){
                                if(data.get(i).isCheck()){
                                    count++;
                                }
                            }
                            if(count<1){
                                new AlertDialog.Builder(Baseactivity).setTitle("警告").setMessage("你应该选择一个帮工，请点击进行选择")
                                        .setPositiveButton("确定",null).create().show();
                            }else if(count>1){
                                new AlertDialog.Builder(Baseactivity).setTitle("警告").setMessage("你应该只选择一个帮工，请点击进行选择")
                                        .setPositiveButton("确定",null).create().show();
                            }
                        }
                        Log.e("SQLite","Helper数据库删除:deleteHelper()的结果是:"+code);
                    }
                }).setNegativeButton("取消",null).create();
        alertDialog.show();
    }

    private void addHelper(){
        LayoutInflater inflater = Baseactivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog_add_helper, null);
        final AlertDialog alertDialog1 = new AlertDialog.Builder(Baseactivity).setView(view)
                .setTitle("添加成员").create();
        alertDialog1.show();


        final EditText name= (EditText) view.findViewById(R.id.alertDialog_edit_name);
        final EditText phone= (EditText) view.findViewById(R.id.alertDialog_edit_phone);

        view.findViewById(R.id.alertDialog_confirmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //判断是否添加为空
                String phoneMessage=phone.getText().toString();
                String nameMessage=name.getText().toString();
                if(phoneMessage==null||phoneMessage.isEmpty()||nameMessage==null||nameMessage.isEmpty()){
                    Toast.makeText(Baseactivity,"电话号码不符合要求,请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }

                HelperMessage helperMessage=new HelperMessage();
                helperMessage.setName(name.getText().toString());

                //添加+86首字段
                if(phoneMessage.length()>=3 && phoneMessage.substring(0,3).equals("+86")){
                    helperMessage.setPhone(phone.getText().toString());
                }else{
                    helperMessage.setPhone("+86"+phone.getText().toString());
                }

                //判断电话号码是否符合要求
                String tempPhoneNumber=helperMessage.getPhone().substring(4,helperMessage.getPhone().length());
                if(IsVaild(tempPhoneNumber)){
                    Toast.makeText(Baseactivity,"电话号码不符合要求,请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }

                //数据库的帮工操作
                helperMessage.setCheck(false);
                long code=DbutilHelper.getInstance().saveHelper(helperMessage,((MyApplication)Baseactivity.getApplication()).getSQLiteOpenHelper().getWritableDatabase());
                Log.e("SQLite","Helper数据库插入:saveHelper()的结果是:"+code);
                List<HelperMessage> temp = DbutilHelper.getInstance().getHelperListData(((MyApplication)Baseactivity.getApplication()).getSQLiteOpenHelper().getReadableDatabase());//得到所有的列表数据
                if(temp.size()-data.size()==1){
                    data.add(temp.get(temp.size()-1));
                    adapter.notifyDataSetChanged();
                    Toast.makeText(Baseactivity,"添加成功",Toast.LENGTH_SHORT).show();
                    Baseactivity.setFlag(true);
                    Baseactivity.setFlag1(true);
                    int count=0;
                    for(int i=0;i<data.size();i++){
                        if(data.get(i).isCheck()){
                            count++;
                        }
                    }
                    if(count<1){
                        new AlertDialog.Builder(Baseactivity).setTitle("警告").setMessage("你应该选择一个帮工，请点击进行选择")
                                .setPositiveButton("确定",null).create().show();
                    }else if(count>1){
                        new AlertDialog.Builder(Baseactivity).setTitle("警告").setMessage("你应该只选择一个帮工，请点击进行选择")
                                .setPositiveButton("确定",null).create().show();
                    }
                }
                alertDialog1.dismiss();//对话框消失
            }
        });

        view.findViewById(R.id.alertDialog_cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();
            }
        });
    }

    private boolean IsVaild(String temp){
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(temp);
        return m.matches()?false:true;
    }
}
