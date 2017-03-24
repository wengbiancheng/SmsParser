package com.example.qq.smsparser.entity;

/**
 * 订货信息的实体类,不是说数据库就使用这个实体类，要简化的
 */
public class OrderGood {

    private int order_id;
    private int good_id;
    private String good_name;
    private int good_number;
    private float good_price;
    private float cost;
    private String buyer_name;
    private String buyer_address;
    private String buyer_phone;
    private String buyer_postcard;

    private boolean send;
    private PayMessage payMessage;

    public PayMessage getPayMessage() {
        return payMessage;
    }

    public void setPayMessage(PayMessage payMessage) {
        this.payMessage = payMessage;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getGood_id() {
        return good_id;
    }

    public void setGood_id(int good_id) {
        this.good_id = good_id;
    }

    public String getGood_name() {
        return good_name;
    }

    public void setGood_name(String good_name) {
        this.good_name = good_name;
    }

    public int getGood_number() {
        return good_number;
    }

    public void setGood_number(int good_number) {
        this.good_number = good_number;
    }

    public float getGood_price() {
        return good_price;
    }

    public void setGood_price(float good_price) {
        this.good_price = good_price;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getBuyer_address() {
        return buyer_address;
    }

    public void setBuyer_address(String buyer_address) {
        this.buyer_address = buyer_address;
    }

    public String getBuyer_phone() {
        return buyer_phone;
    }

    public void setBuyer_phone(String buyer_phone) {
        this.buyer_phone = buyer_phone;
    }

    public String getBuyer_postcard() {
        return buyer_postcard;
    }

    public void setBuyer_postcard(String buyer_postcard) {
        this.buyer_postcard = buyer_postcard;
    }
}
