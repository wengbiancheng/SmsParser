package com.example.qq.smsparser.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.qq.smsparser.entity.OrderGood;
import com.example.qq.smsparser.entity.PayMessage;
import com.example.qq.smsparser.entity.SendMessage;

/**
 * 数据库的订单表操作类，主要进行数据的创建和销毁，数据库的增删查改等操作
 */
public class DbutilOrder{
    private static DbutilOrder dbutils = null;
    private String TABLE_ORDER = "orderGoodDB";
    private final String[] ORDER_COLS = new String[]{"_id","orderId","goodId", "goodName",
            "price", "number", "cost", "buyerName", "buyerAddress", "buyerPhone", "postcard", "isPay","helperId", "sendName", "sendTime","sendPrice","isSend"};

    public static DbutilOrder getInstance(){
        if(dbutils==null){
            dbutils=new DbutilOrder();
            return dbutils;
        }
        return dbutils;
    }

    //TODO add,remove,update,search

    public long saveOrderMessage(OrderGood orderGood,SQLiteDatabase write_sqlite) {
        Log.e("SQLite","Order数据库插入:saveOrderMessage()");
        ContentValues values = new ContentValues();
        values.put("orderId", orderGood.getOrder_id());
        values.put("goodId", orderGood.getGood_id());
        values.put("goodName", orderGood.getGood_name());
        values.put("price", orderGood.getGood_price());
        values.put("number", orderGood.getGood_number());
        values.put("cost", orderGood.getCost());
        values.put("buyerName", orderGood.getBuyer_name());
        values.put("buyerAddress", orderGood.getBuyer_address());
        values.put("buyerPhone", orderGood.getBuyer_phone());
        values.put("postcard", orderGood.getBuyer_postcard());

        values.put("isPay", -1);
        values.put("helperId",-1);
        values.put("sendName","");
        values.put("sendTime","");
        values.put("sendPrice","");
        values.put("isSend",-1);
        return write_sqlite.insert(TABLE_ORDER, null, values);
    }

    /**
     * 根据订单号去搜索数据库，然后更新数据库的信息
     * @param payMessage
     */
    public int updateOrderMessage(PayMessage payMessage,SQLiteDatabase read_sqlite,SQLiteDatabase write_sqlite) {
        Log.e("SQLite","Order数据库更新:updateOrderMessage(PayMessage)");
        Cursor cursor = read_sqlite.query(TABLE_ORDER, ORDER_COLS, null, null, null, null, null);
        cursor.moveToFirst();
        String order_id;
        int count = cursor.getCount();
        for (int i = 0; i < count; i++) {
            order_id = cursor.getString(1);
            if (payMessage.getOrder_id().equals(order_id)) {
                ContentValues values = new ContentValues();

//                values.put("_id", cursor.getInt(0));
//                values.put("orderId", cursor.getString(1));
//                values.put("goodId", cursor.getString(2));
//                values.put("goodName", cursor.getString(3));
//                values.put("price", cursor.getFloat(4));
//                values.put("number", cursor.getInt(5));
//                values.put("cost", cursor.getFloat(6));
//                values.put("buyerName", cursor.getString(7));
//                values.put("buyerAddress", cursor.getString(8));
//                values.put("buyerPhone", cursor.getString(9));
//                values.put("postcard", cursor.getString(10));
                values.put("isPay", payMessage.isPay()?1:0);

                return write_sqlite.update(TABLE_ORDER, values, "_id=" + cursor.getInt(0), null);
            }
            cursor.moveToNext();
        }
        return -1;
    }

    /**
     * 根据订单号去搜索数据库，然后更新数据库的信息
     * @param sendMessage
     */
    public int updateOrderMessage(SendMessage sendMessage,SQLiteDatabase read_sqlite,SQLiteDatabase write_sqlite) {
        Log.e("SQLite","Order数据库更新:updateOrderMessage(SendMessage)");
        Cursor cursor = read_sqlite.query(TABLE_ORDER, ORDER_COLS, null, null, null, null, null);
        cursor.moveToFirst();
        String order_id;
        int count = cursor.getCount();
        for (int i = 0; i < count; i++) {
            order_id = cursor.getString(1);
            if (sendMessage.getOrder_id().equals(order_id)) {
                ContentValues values = new ContentValues();
//
//                values.put("_id", cursor.getInt(0));
//                values.put("orderId", cursor.getString(1));
//                values.put("goodId", cursor.getString(2));
//                values.put("goodName", cursor.getString(3));
//                values.put("price", cursor.getFloat(4));
//                values.put("number", cursor.getInt(5));
//                values.put("cost", cursor.getFloat(6));
//                values.put("buyerName", cursor.getString(7));
//                values.put("buyerAddress", cursor.getString(8));
//                values.put("buyerPhone", cursor.getString(9));
//                values.put("postcard", cursor.getString(10));
//                values.put("isPay", cursor.getInt(11));
                values.put("helperId",sendMessage.getHelper_id());
                values.put("sendName",sendMessage.getDelivery_name());
                values.put("sendTime",sendMessage.getDelivery_time());
                values.put("sendPrice",sendMessage.getDelivery_price());
                values.put("isSend",sendMessage.isSend()?1:0);

                return write_sqlite.update(TABLE_ORDER, values, "_id=" + cursor.getInt(0), null);
            }
            cursor.moveToNext();
        }
        return -1;
    }

    /**
     * 订货短信到达后，我们得到发货数据，进行帮工的短信发送工作
     * @param orderId
     * @return
     */
    public OrderGood getOrderGood(String orderId,SQLiteDatabase read_sqlite){
        Log.e("SQLite","Order数据库检索:getOrderGood()");
        String selection = "orderId=?";
        String[] selectionArgs = new String[]{orderId};

        Cursor cursor = read_sqlite.query(TABLE_ORDER, ORDER_COLS, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        if(cursor!=null&&cursor.getCount()>0){

            OrderGood orderGood=new OrderGood();
            orderGood.setId(cursor.getInt(0));
            orderGood.setOrder_id(cursor.getString(1));
            orderGood.setGood_id(cursor.getString(2));
            orderGood.setGood_name(cursor.getString(3));
            orderGood.setGood_price(cursor.getFloat(4));
            orderGood.setGood_number(cursor.getInt(5));
            orderGood.setCost(cursor.getFloat(6));
            orderGood.setBuyer_name(cursor.getString(7));
            orderGood.setBuyer_address(cursor.getString(8));
            orderGood.setBuyer_phone(cursor.getString(9));
            orderGood.setBuyer_postcard(cursor.getString(10));
            orderGood.getPayMessage().setPay(cursor.getInt(11)==1?true:false);
            orderGood.getPayMessage().setOrder_id(orderGood.getOrder_id());
            orderGood.setSend(cursor.getInt(16)==1?true:false);

            return orderGood;
        }
        return null;
    }
}
