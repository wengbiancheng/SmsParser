package com.example.qq.smsparser.model.db;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {

    public static void writeServerNumber(String content, Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences("server",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("number", content);
        editor.commit();
    }

    public static String getServerNumber(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        String number=sharedPreferences.getString("number","106902208411010008");
        return number;
    }
}
