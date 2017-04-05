package com.example.qq.smsparser;

/**
 * 常量类
 */
public class Configs {

    public final static String SMS_ORDER_AND_PAY_NUMBER="15608443963";

    public final static int version = 12;

    //初始化数据库表
    public final static String[] ORDER_COLS = new String[]{"id", "goodId", "goodName",
            "price", "number", "cost", "buyerName", "buyerAddress", "buyerPhone", "postcard", "isPay", "helperId", "sendName", "sendTime", "sendPrice", "isSend"};
    public final static String DB = "SmsParseDB";
    public final static String TABLE_ORDER = "orderGoodDB";


    public final static String[] HELPER_COLS = new String[]{"_id", "name", "phone", "choose"};
    public final static String TABLE_HELPER = "helperDB";

}
