package com.example.qq.smsparser.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qq.smsparser.R;
import com.example.qq.smsparser.entity.HelperMessage;

import java.util.List;

/**
 * Created by qq on 2017/3/5.
 */
public class HelperAdapter extends BaseAdapter {

    private Context context;
    private List<HelperMessage> datas;

    public HelperAdapter(Context context, List<HelperMessage> datas){
        this.context=context;
        this.datas=datas;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
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

        viewHolder.name.setText(datas.get(i).getName());
        viewHolder.phone.setText(datas.get(i).getPhone());
        viewHolder.selected.setChecked(datas.get(i).isCheck());

        return view;
    }

    public static class ViewHolder {
        TextView name;
        TextView phone;
        CheckBox selected;
    }
}
