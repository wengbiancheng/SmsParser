package com.example.qq.smsparser.controller.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 将毫秒转化为特定格式的日期
 */
public class TimeUtils {

    public static String getTime(String time){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long now = Long.parseLong(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        return formatter.format(calendar.getTime());
    }

    public static long StringToMillisecond(String time){
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time));
            return c.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
