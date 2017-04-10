package com.example.qq.smsparser;

/**
 * 常量类
 */
public class Configs {

    //假设服务器发过来的短信号码是:18814122731
    public final static String SMS_SERVER_NUMBER="+8618814122731";
    //假设帮工的电话号码是：+8615608443963，这个帮工可能需要一个List
    public final static String SMS_HELPER_NUMBER="+8615608443963";

    public final static int version = 12;

    //初始化数据库表
    public final static String[] ORDER_COLS = new String[]{"id", "goodId", "goodName",
            "price", "number", "cost", "buyerName", "buyerAddress", "buyerPhone", "postcard", "isPay", "helperId", "sendName", "sendTime", "sendPrice", "isSend"};
    public final static String DB = "SmsParseDB";
    public final static String TABLE_ORDER = "orderGoodDB";


    public final static String[] HELPER_COLS = new String[]{"_id", "name", "phone", "choose"};
    public final static String TABLE_HELPER = "helperDB";

}
