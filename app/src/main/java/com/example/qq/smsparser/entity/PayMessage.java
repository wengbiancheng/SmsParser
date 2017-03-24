package com.example.qq.smsparser.entity;

/**
 * 订货信息的付款实体类
 */
public class PayMessage {
    private int order_id;
    private boolean pay;

    private String buyer_name;
    private String buyer_address;
    private String buyer_phone;
    private String buyer_postcard;


    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public boolean isPay() {
        return pay;
    }

    public void setPay(boolean pay) {
        this.pay = pay;
    }
}
