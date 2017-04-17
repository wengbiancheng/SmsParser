package com.example.qq.smsparser.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.qq.smsparser.controller.utils.OrderSaleUtils;
import com.example.qq.smsparser.entity.OrderGood;
import com.example.qq.smsparser.entity.OrderSaleMessage;
import com.example.qq.smsparser.entity.PayMessage;
import com.example.qq.smsparser.entity.SendMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 数据库的订单表操作类，主要进行数据的创建和销毁，数据库的增删查改等操作
 */
public class DbutilOrder {
    private static DbutilOrder dbutils = null;
    private String TABLE_ORDER = "orderGoodDB";
    private final String[] ORDER_COLS = new String[]{"_id", "orderId", "goodId", "goodName",
            "price", "number", "cost", "buyerName", "buyerAddress", "buyerPhone", "postcard", "isPay", "helperId", "sendName", "sendTime", "sendPrice", "isSend"};

    public static DbutilOrder getInstance() {
        if (dbutils == null) {
            dbutils = new DbutilOrder();
            return dbutils;
        }
        return dbutils;
    }

    //TODO add,remove,update,search

    public long saveOrderMessage(OrderGood orderGood, SQLiteDatabase write_sqlite) {
        Log.e("SQLite", "Order数据库插入:saveOrderMessage()");
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
        values.put("helperId", -1);
        values.put("sendName", "");
        values.put("sendTime", "");
        values.put("sendPrice", "");
        values.put("isSend", -1);
        return write_sqlite.insert(TABLE_ORDER, null, values);
    }

    /**
     * 根据订单号去搜索数据库，然后更新数据库的信息
     *
     * @param payMessage
     */
    public int updateOrderMessage(PayMessage payMessage, SQLiteDatabase read_sqlite, SQLiteDatabase write_sqlite) {
        Log.e("SQLite", "Order数据库更新:updateOrderMessage(PayMessage)");
        Cursor cursor = read_sqlite.query(TABLE_ORDER, ORDER_COLS, "orderId=?", new String[]{payMessage.getOrder_id()}, null, null, null);
        cursor.moveToFirst();
        String order_id;
        int count = cursor.getCount();
        for (int i = 0; i < count; i++) {
            order_id = cursor.getString(1);
            if (cursor.getInt(11) == 1) {
                return -1;
            } else {
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
                    values.put("isPay", payMessage.isPay() ? 1 : 0);
                    return write_sqlite.update(TABLE_ORDER, values, "_id=" + cursor.getInt(0), null);
                }
            }
            cursor.moveToNext();
        }
        return -1;
    }

    /**
     * 根据订单号去搜索数据库，然后更新数据库的信息
     *
     * @param sendMessage
     */
    public int updateOrderMessage(SendMessage sendMessage, SQLiteDatabase read_sqlite, SQLiteDatabase write_sqlite) {
        Log.e("SQLite", "Order数据库更新:updateOrderMessage(SendMessage)");
        Cursor cursor = read_sqlite.query(TABLE_ORDER, ORDER_COLS, "orderId=?", new String[]{sendMessage.getOrder_id()}, null, null, null);
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
                values.put("helperId", sendMessage.getHelper_id());
                values.put("sendName", sendMessage.getDelivery_name());
                values.put("sendTime", sendMessage.getDelivery_time());
                values.put("sendPrice", sendMessage.getDelivery_price());
                values.put("isSend", sendMessage.isSend() ? 1 : 0);
                return write_sqlite.update(TABLE_ORDER, values, "_id=" + cursor.getInt(0), null);
            }
            cursor.moveToNext();
        }
        return -1;
    }

    /**
     * 订货短信到达后，我们得到发货数据，进行帮工的短信发送工作
     *
     * @param orderId
     * @return
     */
    public OrderGood getOrderGood(String orderId, SQLiteDatabase read_sqlite) {
        Log.e("SQLite", "Order数据库检索:getOrderGood()");
        String selection = "orderId=?";
        String[] selectionArgs = new String[]{orderId};

        Cursor cursor = read_sqlite.query(TABLE_ORDER, ORDER_COLS, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {

            OrderGood orderGood = new OrderGood();
            orderGood.setId(cursor.getInt(0));
            orderGood.setOrder_id(cursor.getString(1));
            orderGood.setGood_id(cursor.getString(2));
            orderGood.setGood_name(cursor.getString(3));
            orderGood.setGood_price(cursor.getString(4));
            orderGood.setGood_number(cursor.getString(5));
            orderGood.setCost(cursor.getFloat(6));
            orderGood.setBuyer_name(cursor.getString(7));
            orderGood.setBuyer_address(cursor.getString(8));
            orderGood.setBuyer_phone(cursor.getString(9));
            orderGood.setBuyer_postcard(cursor.getString(10));
            orderGood.getPayMessage().setPay(cursor.getInt(11) == 1 ? true : false);
            orderGood.getPayMessage().setOrder_id(orderGood.getOrder_id());
            orderGood.setSend(cursor.getInt(16) == 1 ? true : false);

            return orderGood;
        }
        return null;
    }

    public List<OrderGood> getAllOrderGood(SQLiteDatabase read_sqlite) {
        Log.e("SQLite", "Order数据库检索:getAllOrderGood()");

        List<OrderGood> list = new ArrayList<>();

        Cursor cursor = read_sqlite.query(TABLE_ORDER, ORDER_COLS, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                OrderGood orderGood = new OrderGood();
                orderGood.setId(cursor.getInt(0));
                orderGood.setOrder_id(cursor.getString(1));
                orderGood.setGood_id(cursor.getString(2));
                orderGood.setGood_name(cursor.getString(3));
                orderGood.setGood_price(cursor.getString(4));
                orderGood.setGood_number(cursor.getString(5));
                orderGood.setCost(cursor.getFloat(6));
                orderGood.setBuyer_name(cursor.getString(7));
                orderGood.setBuyer_address(cursor.getString(8));
                orderGood.setBuyer_phone(cursor.getString(9));
                orderGood.setBuyer_postcard(cursor.getString(10));
                orderGood.getPayMessage().setPay(cursor.getInt(11) == 1 ? true : false);
                orderGood.getPayMessage().setOrder_id(orderGood.getOrder_id());
                orderGood.setSend(cursor.getInt(16) == 1 ? true : false);

                list.add(orderGood);
                cursor.moveToNext();
            }
            return list;
        }
        return null;
    }

    public Map<Integer, List<OrderSaleMessage>> getAllOrderSaleMessage(SQLiteDatabase read_sqlite) {
        Log.e("SQLite", "Order数据库检索:getAllOrderSaleMessage()");

        Map<Integer, List<OrderSaleMessage>> map = new HashMap<>();

        Cursor cursor = read_sqlite.query(TABLE_ORDER, ORDER_COLS, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {

                Log.e("TestSale", "查询的订单号是:" + cursor.getString(1));
                //当订货数据不是当前的年份的时候
                if (OrderSaleUtils.getMonth(cursor.getString(14)) == -1) {
                    cursor.moveToNext();
                    continue;
                }
                OrderSaleMessage orderSaleMessage = new OrderSaleMessage();
                orderSaleMessage.setOrderId(cursor.getString(1));
                orderSaleMessage.setMonth(OrderSaleUtils.getMonth(cursor.getString(14)));
                orderSaleMessage.setHelpId(cursor.getInt(12));
                orderSaleMessage.setGood_price(cursor.getFloat(6));
                orderSaleMessage.setDelivery_price(cursor.getFloat(15));
                orderSaleMessage.setHelper_cost(OrderSaleUtils.getNumber(cursor.getString(5)));
                orderSaleMessage.setOther_cost(OrderSaleUtils.getNumber(cursor.getString(5)));

                if (!map.containsKey(orderSaleMessage.getMonth())) {
                    map.put(orderSaleMessage.getMonth(), new ArrayList<OrderSaleMessage>());
                }
                map.get(orderSaleMessage.getMonth()).add(orderSaleMessage);

                cursor.moveToNext();
            }
            return map;
        }
        return null;
    }

    public List<OrderSaleMessage> getAllHelperSaleMessage(SQLiteDatabase read_sqlite, int helperId) {
        Log.e("SQLite", "Order数据库检索:getAllOrderSaleMessage()");

        List<OrderSaleMessage> list = new ArrayList<>();

        Cursor cursor = read_sqlite.query(TABLE_ORDER, ORDER_COLS, "helperId=?", new String[]{helperId + ""}, null, null, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {

                //当订货数据不是当前的年份的时候
                if (OrderSaleUtils.getMonth(cursor.getString(14)) == -1) {
                    cursor.moveToNext();
                    continue;
                }
                OrderSaleMessage orderSaleMessage = new OrderSaleMessage();
                orderSaleMessage.setOrderId(cursor.getString(1));
                orderSaleMessage.setMonth(OrderSaleUtils.getMonth(cursor.getString(14)));
                orderSaleMessage.setHelpId(cursor.getInt(12));
                orderSaleMessage.setGood_price(cursor.getFloat(6));
                orderSaleMessage.setDelivery_price(cursor.getFloat(15));
                orderSaleMessage.setHelper_cost(OrderSaleUtils.getNumber(cursor.getString(5)));
                orderSaleMessage.setOther_cost(OrderSaleUtils.getNumber(cursor.getString(5)));
                list.add(orderSaleMessage);
                cursor.moveToNext();
            }
            return list;
        }
        return null;
    }

    public List<SendMessage> getAllSendMessage(SQLiteDatabase read_sqlite) {
        Log.e("SQLite", "Order数据库检索:getAllSendMessage()");

        List<SendMessage> list = new ArrayList<>();


        Cursor cursor = read_sqlite.query(TABLE_ORDER, ORDER_COLS, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setOrder_id(cursor.getString(1));
                sendMessage.setGood_id(cursor.getString(2));
                sendMessage.setGood_name(cursor.getString(3));
                sendMessage.setBuyer_name(cursor.getString(7));
                sendMessage.setBuyer_address(cursor.getString(8));
                sendMessage.setBuyer_phone(cursor.getString(9));
                sendMessage.setBuyer_postcard(cursor.getString(10));
                sendMessage.setHelper_id(cursor.getInt(12));
                sendMessage.setDelivery_name(cursor.getString(13));
                sendMessage.setDelivery_time(cursor.getString(14));
                sendMessage.setDelivery_price(cursor.getFloat(15));

                list.add(sendMessage);
                cursor.moveToNext();
            }
            return list;
        }
        return null;
    }

    public SendMessage getSendMessage(String orderId, SQLiteDatabase read_sqlite) {
        Log.e("SQLite", "Order数据库检索:getSendMessage()");
        String selection = "orderId=?";
        String[] selectionArgs = new String[]{orderId};

        Cursor cursor = read_sqlite.query(TABLE_ORDER, ORDER_COLS, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {

            SendMessage sendMessage = new SendMessage();
            sendMessage.setOrder_id(cursor.getString(1));
            sendMessage.setGood_id(cursor.getString(2));
            sendMessage.setGood_name(cursor.getString(3));
            sendMessage.setBuyer_name(cursor.getString(7));
            sendMessage.setBuyer_address(cursor.getString(8));
            sendMessage.setBuyer_phone(cursor.getString(9));
            sendMessage.setBuyer_postcard(cursor.getString(10));
            sendMessage.setHelper_id(cursor.getInt(12));
            sendMessage.setDelivery_name(cursor.getString(13));
            sendMessage.setDelivery_time(cursor.getString(14));
            sendMessage.setDelivery_price(cursor.getFloat(15));

            return sendMessage;
        }
        return null;
    }
}
