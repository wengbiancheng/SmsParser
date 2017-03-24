package com.example.qq.smsparser.view.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.qq.smsparser.R;
import com.example.qq.smsparser.controller.db.DbutilHelper;
import com.example.qq.smsparser.entity.HelperMessage;
import com.example.qq.smsparser.view.BaseFragment;
import com.example.qq.smsparser.view.adapter.HelperAdapter;

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

        dbutilHelper = DbutilHelper.getInstance(Baseactivity.getApplicationContext());
        data = dbutilHelper.getHelperListData();//得到所有的列表数据

        listView = (ListView) view.findViewById(R.id.helper_listView);
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
                return false;
            }
        });
        adapter = new HelperAdapter(Baseactivity, data);
        listView.setAdapter(adapter);

        view.findViewById(R.id.helper_add_helper_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHelper();
            }
        });
        return view;
    }

    private void DeleteHelper(final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(Baseactivity).setMessage("您真的要删除这个帮工信息吗?").setPositiveButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbutilHelper.deleteHelper(data.get(position).getId());
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
                helperMessage.setPhone(phone.getText().toString());
                helperMessage.setCheck(false);
                dbutilHelper.saveHelper(helperMessage);
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
