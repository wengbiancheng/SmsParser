package com.example.qq.smsparser;

import android.app.Application;
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
