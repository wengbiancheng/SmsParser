package com.example.qq.smsparser.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qq.smsparser.R;
import com.example.qq.smsparser.controller.utils.TimeUtils;
import com.example.qq.smsparser.entity.SmsMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * OrderGoodSmsListFragment、PaySmsListFragment
 * ReceiveSmsListFragment、SendSmsListFragment的这些子fragment的适配器
 */
public class SonFragmentAdapter extends BaseAdapter {

    private Context context;
    private List<SmsMessage> list=new ArrayList<>();

    public SonFragmentAdapter(Context context, List<SmsMessage> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
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
            view = View.inflate(context, R.layout.item_sms, null);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.item_sms_image);
            viewHolder.phone = (TextView) view.findViewById(R.id.item_sms_phone);
            viewHolder.time = (TextView) view.findViewById(R.id.item_sms_time);
            viewHolder.content = (TextView) view.findViewById(R.id.item_sms_content);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.phone.setText(list.get(i).getNumber());
        viewHolder.content.setText(list.get(i).getBody());
        viewHolder.time.setText(TimeUtils.getTime(list.get(i).getTime()));

        return view;
    }

    public static class ViewHolder {
        ImageView imageView;
        TextView phone;
        TextView time;
        TextView content;
    }
}
