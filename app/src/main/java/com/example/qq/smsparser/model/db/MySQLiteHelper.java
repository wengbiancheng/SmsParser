package com.example.qq.smsparser.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.qq.smsparser.Configs;

/**
 * 负责数据库的建立和更新，以及数据库操作对象的获取工作
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    protected Context context;
    private static MySQLiteHelper helper;
    private final  String create_table_order = "create table " + Configs.TABLE_ORDER +
            "(id integer primary key autoincrement,goodId integer,goodName text,price real,number integer," +
            "cost real,buyerName text,buyerAddress text,buyerPhone text,postcard text,isPay integer,helperId integer,sendName text,sendTime text,sendPrice real," +
            "isSend integer)";
    private final  String create_table_helper = "CREATE TABLE " + Configs.TABLE_HELPER +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,phone TEXT,choose INTEGER);";

    public static MySQLiteHelper getInstance(Context context) {
        if (helper == null) {
            helper = new MySQLiteHelper(context, Configs.DB, null, Configs.version);
            return helper;
        }
        return helper;
    }

    private MySQLiteHelper(Context context, String name,
                           SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table_helper);
        db.execSQL(create_table_order);
        Log.e("SQLite", "两个数据库建立:OnCreate()");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("SQLite", "两个数据库更新:onUpgrade()");
        db.execSQL("DROP TABLE IF EXISTS " + Configs.TABLE_HELPER);
        db.execSQL("DROP TABLE IF EXISTS " + Configs.TABLE_ORDER);
        this.onCreate(db);
    }
}
