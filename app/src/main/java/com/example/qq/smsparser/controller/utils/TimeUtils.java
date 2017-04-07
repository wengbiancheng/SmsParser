package com.example.qq.smsparser.controller.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 将毫秒转化为特定格式的日期
 */
public class TimeUtils {

    public static String getTime(String time){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = Long.parseLong(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        return formatter.format(calendar.getTime());
    }
}
