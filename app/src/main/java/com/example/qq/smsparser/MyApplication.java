package com.example.qq.smsparser;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.qq.smsparser.model.db.MySQLiteHelper;

import java.util.ArrayList;

public class MyApplication extends Application {

    private MySQLiteHelper sqLiteOpenHelper;
    private static boolean serviceIsAlive=false;

    public static boolean isServiceIsAlive() {
        return serviceIsAlive;
    }

    public  void setServiceIsAlive(boolean serviceIsAlive) {
        this.serviceIsAlive = serviceIsAlive;
    }

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
}
