package com.example.qq.smsparser;

import android.app.Application;
import com.example.qq.smsparser.controller.db.MySQLiteHelper;

/**
 * Created by qq on 2017/3/25.
 */
public class MyApplication extends Application {

    private  MySQLiteHelper sqLiteOpenHelper;

    public  synchronized MySQLiteHelper getSQLiteOpenHelper(){
        if(sqLiteOpenHelper==null){
            sqLiteOpenHelper=MySQLiteHelper.getInstance(getApplicationContext());
        }
        return sqLiteOpenHelper;
    }
}
