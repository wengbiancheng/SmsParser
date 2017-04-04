package com.example.qq.smsparser;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.qq.smsparser.model.db.MySQLiteHelper;

public class MyApplication extends Application {

    private MySQLiteHelper sqLiteOpenHelper;

    public synchronized MySQLiteHelper getSQLiteOpenHelper() {
        if (sqLiteOpenHelper == null) {
            sqLiteOpenHelper = MySQLiteHelper.getInstance(getApplicationContext());
        }
        return sqLiteOpenHelper;

    }
}
