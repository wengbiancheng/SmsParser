package com.example.qq.smsparser.controller.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.qq.smsparser.R;
import com.example.qq.smsparser.entity.HelperMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HelperFragment的适配器
 */
public class HelperAdapter extends BaseAdapter {

    private Context context;
    private List<HelperMessage> datas;
    private Handler handler;
    private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();

    public HelperAdapter(Context context, List<HelperMessage> datas, Handler handler){
        this.context=context;
        this.datas=datas;
        this.handler=handler;
        for(int i=0;i<datas.size();i++){
            if(datas.get(i).isCheck()){
                isCheckMap.put(i,true);
            }
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.item_helper, null);
            viewHolder.name = (TextView) view.findViewById(R.id.item_helper_name);
            viewHolder.phone = (TextView) view.findViewById(R.id.item_helper_phone);
            viewHolder.selected = (CheckBox) view.findViewById(R.id.item_helper_checkbox);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //每一项的控件的赋值操作
        viewHolder.name.setText(datas.get(position).getName());
        viewHolder.phone.setText(datas.get(position).getPhone());



        //找到需要选中的条目
        if (isCheckMap.size()>0 && isCheckMap != null && isCheckMap.containsKey(position) && isCheckMap.get(position)) {
            viewHolder.selected.setChecked(true);
        } else {
            viewHolder.selected.setChecked(false);
        }

        viewHolder.selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //将选中的放入hashmap中
                    isCheckMap.put(position, isChecked);
                } else {
                    //取消选中的则剔除
                    isCheckMap.remove(position);
                }
                Message message=new Message();
                message.what=11;
                message.arg1=position;
                message.obj=isChecked;
                handler.sendMessage(message);
            }
        });

        return view;
    }

    public static class ViewHolder {
        TextView name;
        TextView phone;
        CheckBox selected;
    }
}
