package com.example.qq.smsparser.controller.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 暂时没有用
 */
public class Dbutil {

    private static Dbutil dbutils;
    private Context context;
    private SQLiteDatabase read_sqlite;
    private SQLiteOpenHelper helper;
    private SQLiteDatabase write_sqlite;
    private int version=5;

    //初始化数据库表
    private final String[] ORDER_COLS = new String[]{"id", "goodId", "goodName",
            "price", "number", "cost", "buyerName", "buyerAddress", "buyerPhone", "postcard", "isPay","helperId", "sendName", "sendTime","sendPrice","isSend"};
    private String DB = "SmsParseDB";
    private String TABLE_ORDER = "orderGoodDB";
    public String create_table_order = "create table " + TABLE_ORDER +
            "(id integer primary key autoincrement,goodId integer,goodName text,price real,number integer," +
            "cost real,buyerName text,buyerAddress text,buyerPhone text,postcard text,isPay integer,helperId integer,sendName text,sendTime text,sendPrice real," +
            "isSend integer)";

    private final String[] HELPER_COLS=new String[]{"_id","name","phone","choose"};
    private String TABLE_HELPER="helperDB";
    public String create_table_helper="CREATE TABLE "+TABLE_HELPER+
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,phone TEXT,choose INTEGER);";


    public static Dbutil getInstance(Context context){
        if(dbutils==null){
            dbutils=new Dbutil(context);
            return dbutils;
        }
        return dbutils;
    }

    private Dbutil(Context context){
        this.context=context;
        init();
    }

    private void init(){
        if(helper==null){
            helper=new MySQLiteHelper(context,DB,null,version);
            write_sqlite=helper.getWritableDatabase();
            read_sqlite=helper.getReadableDatabase();
        }else{
            write_sqlite=helper.getWritableDatabase();
            read_sqlite=helper.getReadableDatabase();
        }
    }

    public synchronized SQLiteDatabase getRead_sqlite() {
        return read_sqlite;
    }

    public synchronized SQLiteDatabase getWrite_sqlite() {
        return write_sqlite;
    }

    class MySQLiteHelper extends SQLiteOpenHelper {

        public MySQLiteHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(create_table_helper);
            db.execSQL(create_table_order);
            Log.e("SQLite","Helper数据库建立:OnCreate()");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.e("SQLite","Helper数据库更新:onUpgrade()");
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_HELPER);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_ORDER);
            this.onCreate(db);
        }
    }
}
