package com.example.qq.smsparser;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import com.example.qq.smsparser.model.db.MySQLiteHelper;

import java.util.ArrayList;

public class MyApplication extends Application {

    private MySQLiteHelper sqLiteOpenHelper;


    public synchronized MySQLiteHelper getSQLiteOpenHelper() {
        if (sqLiteOpenHelper == null) {
            sqLiteOpenHelper = MySQLiteHelper.getInstance(getApplicationContext());
        }
        return sqLiteOpenHelper;
    }

    public static boolean isServiceWorked(Context context, String serviceName) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    public static void writeServerNumber(String content, Context context){
//        SharedPreferences sharedPreferences= context.getApplicationContext().getSharedPreferences("server",
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("number", content);
//        editor.commit();
        Settings.System.putString(context.getContentResolver(),"aaa",content);
    }

    public static String getServerNumber(Context context){
//        SharedPreferences sharedPreferences= context.getApplicationContext().getSharedPreferences("server",
//                Context.MODE_PRIVATE);
//        String number=sharedPreferences.getString("number","");
//        return number;
        return Settings.System.getString(context.getContentResolver(), "aaa");
    }
}
