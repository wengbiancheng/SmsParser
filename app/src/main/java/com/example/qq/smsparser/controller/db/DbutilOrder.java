package com.example.qq.smsparser.controller.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.qq.smsparser.entity.OrderGood;
import com.example.qq.smsparser.entity.PayMessage;
import com.example.qq.smsparser.entity.SendMessage;

/**
 * Created by qq on 2017/3/17.
 */
public class DbutilOrder {
    private static DbutilOrder dbutils = null;
    private Context context;
    private SQLiteDatabase read_sqlite;
    private SQLiteOpenHelper helper;
    private SQLiteDatabase write_sqlite;
    private int version = 5;

    private Dbutil dbutil=null;

    //初始化数据库表
    private final String[] ORDER_COLS = new String[]{"id", "goodId", "goodName",
            "price", "number", "cost", "buyerName", "buyerAddress", "buyerPhone", "postcard", "isPay","helperId", "sendName", "sendTime","sendPrice","isSend"};
    private String DB = "SmsParseDB";
    private String TABLE_ORDER = "orderGoodDB";
    public String create_table_order = "create table " + TABLE_ORDER +
            "(id integer primary key autoincrement,goodId integer,goodName text,price real,number integer," +
            "cost real,buyerName text,buyerAddress text,buyerPhone text,postcard text,isPay integer,helperId integer,sendName text,sendTime text,sendPrice real," +
            "isSend integer)";

    public static DbutilOrder getInstance(Context context) {
        if (dbutils == null) {
            dbutils = new DbutilOrder(context);
            return dbutils;
        }
        return dbutils;
    }

    private DbutilOrder(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        if (helper == null) {
            helper = new MySQLiteHelper(context, DB, null, version);
            write_sqlite = helper.getWritableDatabase();
            read_sqlite = helper.getReadableDatabase();
        } else {
            write_sqlite = helper.getWritableDatabase();
            read_sqlite = helper.getReadableDatabase();
        }

        if(dbutil==null){
            dbutil=Dbutil.getInstance(context);
        }
    }

    //TODO add,remove,update,search

    public long saveOrderMessage(OrderGood orderGood) {
        Log.e("SQLite","Order数据库插入:saveOrderMessage()");
        ContentValues values = new ContentValues();
        values.put("id", orderGood.getOrder_id());
        values.put("goodId", orderGood.getGood_id());
        values.put("goodName", orderGood.getGood_name());
        values.put("price", orderGood.getGood_price());
        values.put("number", orderGood.getGood_number());
        values.put("cost", orderGood.getCost());
        values.put("buyerName", orderGood.getBuyer_name());
        values.put("buyerAddress", orderGood.getBuyer_address());
        values.put("buyerPhone", orderGood.getBuyer_phone());
        values.put("postcard", orderGood.getBuyer_postcard());
        return write_sqlite.insert(TABLE_ORDER, null, values);
    }

    /**
     * 根据订单号去搜索数据库，然后更新数据库的信息
     * @param payMessage
     */
    public int updateOrderMessage(PayMessage payMessage) {
        Log.e("SQLite","Order数据库更新:updateOrderMessage(PayMessage)");
        Cursor cursor = read_sqlite.query(TABLE_ORDER, ORDER_COLS, null, null, null, null, null);
        cursor.moveToFirst();
        int order_id;
        int count = cursor.getCount();
        for (int i = 0; i < count; i++) {
            order_id = cursor.getInt(0);
            if (payMessage.getOrder_id() == order_id) {
                ContentValues values = new ContentValues();

                values.put("id", cursor.getInt(1));
                values.put("goodId", cursor.getInt(2));
                values.put("goodName", cursor.getString(3));
                values.put("price", cursor.getFloat(4));
                values.put("number", cursor.getInt(5));
                values.put("cost", cursor.getFloat(6));
                values.put("buyerName", cursor.getString(7));
                values.put("buyerAddress", cursor.getString(8));
                values.put("buyerPhone", cursor.getString(9));
                values.put("postcard", cursor.getString(10));
                values.put("isPay", payMessage.isPay()?1:0);

                return write_sqlite.update(TABLE_ORDER, values, "id=" + payMessage.getOrder_id(), null);
            }
            cursor.moveToNext();
        }
        return -1;
    }

    /**
     * 根据订单号去搜索数据库，然后更新数据库的信息
     * @param sendMessage
     */
    public int updateOrderMessage(SendMessage sendMessage) {
        Log.e("SQLite","Order数据库更新:updateOrderMessage(SendMessage)");
        Cursor cursor = read_sqlite.query(TABLE_ORDER, ORDER_COLS, null, null, null, null, null);
        cursor.moveToFirst();
        int order_id;
        int count = cursor.getCount();
        for (int i = 0; i < count; i++) {
            order_id = cursor.getInt(0);
            if (sendMessage.getOrder_id() == order_id) {
                ContentValues values = new ContentValues();

                values.put("id", cursor.getInt(1));
                values.put("goodId", cursor.getInt(2));
                values.put("goodName", cursor.getString(3));
                values.put("price", cursor.getFloat(4));
                values.put("number", cursor.getInt(5));
                values.put("cost", cursor.getFloat(6));
                values.put("buyerName", cursor.getString(7));
                values.put("buyerAddress", cursor.getString(8));
                values.put("buyerPhone", cursor.getString(9));
                values.put("postcard", cursor.getString(10));
                values.put("isPay", cursor.getInt(11));
                values.put("helperId",sendMessage.getHelper_id());
                values.put("sendName",sendMessage.getDelivery_name());
                values.put("sendTime",sendMessage.getDelivery_time());
                values.put("sendPrice",sendMessage.getDelivery_price());
                values.put("isSend",sendMessage.isSend()?1:0);

                return write_sqlite.update(TABLE_ORDER, values, "id=" + sendMessage.getOrder_id(), null);
            }
            cursor.moveToNext();
        }
        return -1;
    }

    /**
     * 订货短信到达后，我们得到发货数据，进行帮工的短信发送工作
     * @param id
     * @return
     */
    public OrderGood getOrderGood(int id){
        Log.e("SQLite","Order数据库检索:getOrderGood()");
        String selection = "id=?";
        String[] selectionArgs = new String[]{id+""};

        Cursor cursor = read_sqlite.query(TABLE_ORDER, ORDER_COLS, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        if(cursor!=null){
            OrderGood orderGood=new OrderGood();
            orderGood.setOrder_id(cursor.getInt(0));
            orderGood.setGood_id(cursor.getInt(1));
            orderGood.setGood_name(cursor.getString(2));
            orderGood.setGood_price(cursor.getFloat(3));
            orderGood.setGood_number(cursor.getInt(4));
            orderGood.setCost(cursor.getFloat(5));
            orderGood.setBuyer_name(cursor.getString(5));
            orderGood.setBuyer_phone(cursor.getString(6));
            orderGood.setBuyer_postcard(cursor.getString(7));
            orderGood.setBuyer_address(cursor.getString(8));
            return orderGood;
        }
        return null;
    }

    class MySQLiteHelper extends SQLiteOpenHelper {

        public MySQLiteHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(create_table_order);
            Log.e("SQLite","Order数据库建立:OnCreate()");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.e("SQLite","Order数据库更新:onUpgrade()");
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_ORDER);
            this.onCreate(db);
        }
    }
}
