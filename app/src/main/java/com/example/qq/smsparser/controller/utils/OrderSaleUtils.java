package com.example.qq.smsparser.controller.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by qq on 2017/4/14.
 */
public class OrderSaleUtils {

    private static Calendar now=Calendar.getInstance();
    public static int getNumber(String content){
        String []data=content.split(",");
        int total=0;
        for(int i=0;i<data.length;i++){
            total=total+Integer.parseInt(data[i]);
        }
        return total;
    }

    public static int getMonth(String content){
        if(content.equals("")||content==null){
            return -1;
        }
        Log.e("TestSale","数据库的数据的月份是:"+content);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(content));
            if(c.get(Calendar.YEAR)==now.get(Calendar.YEAR) && c.get(Calendar.MONTH)<=now.get(Calendar.MONTH)){
                return c.get(Calendar.MONTH)+1;
            }else{
                return -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
