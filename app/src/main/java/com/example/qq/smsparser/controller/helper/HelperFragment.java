package com.example.qq.smsparser.controller.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.qq.smsparser.model.db.DbutilHelper;
import com.example.qq.smsparser.entity.HelperMessage;
import com.example.qq.smsparser.controller.BaseFragment;
import com.example.qq.smsparser.controller.adapter.HelperAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 帮工界面，主要用来对帮工的数据进行增删查改，然后也涉及到数据库的操作
 */
public class HelperFragment extends BaseFragment {

    private ListView listView;
    private HelperAdapter adapter;

    private List<HelperMessage> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("Process", "HelperFragment:onCreateView");
        View view = inflater.inflate(R.layout.fragment_helper, null);

        data = DbutilHelper.getInstance().getHelperListData(((MyApplication)Baseactivity.getApplication()).getSQLiteOpenHelper().getReadableDatabase());//得到所有的列表数据

        listView = (ListView) view.findViewById(R.id.helper_listView);
        adapter = new HelperAdapter(Baseactivity, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Baseactivity, HelperMessageAty.class);
                startActivity(intent);
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
                HelperMessage helperMessage=new HelperMessage();
                helperMessage.setName(name.getText().toString());
                if(phone.getText().toString().substring(0,3).equals("+86")){
                    helperMessage.setPhone(phone.getText().toString());
                }else{
                    helperMessage.setPhone("+86"+phone.getText().toString());
                }
                helperMessage.setCheck(false);
                long code=DbutilHelper.getInstance().saveHelper(helperMessage,((MyApplication)Baseactivity.getApplication()).getSQLiteOpenHelper().getWritableDatabase());
                Log.e("SQLite","Helper数据库插入:saveHelper()的结果是:"+code);
                List<HelperMessage> temp = DbutilHelper.getInstance().getHelperListData(((MyApplication)Baseactivity.getApplication()).getSQLiteOpenHelper().getReadableDatabase());//得到所有的列表数据
                if(temp.size()-data.size()==1){
                    data.add(temp.get(temp.size()-1));
                    adapter.notifyDataSetChanged();
                    Toast.makeText(Baseactivity,"添加成功",Toast.LENGTH_SHORT).show();
                    Baseactivity.setFlag(true);
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
}
