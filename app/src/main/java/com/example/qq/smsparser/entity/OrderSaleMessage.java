package com.example.qq.smsparser.entity;

/**
 * 一个订单的销售统计,主要是帮工的工资，快递费用这些可以在这个类中统一修改，方便以后扩展
 */
public class OrderSaleMessage {

    private String orderId;
    private int month;
    private int helpId;
    private float good_price;//商品的销售额
    private float delivery_price;//快递费用
    private float helper_cost;//帮工的工资
    private float other_cost;//包装费等费用
    private float profit;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getHelpId() {
        return helpId;
    }

    public void setHelpId(int helpId) {
        this.helpId = helpId;
    }

    public float getGood_price() {
        return good_price;
    }

    public void setGood_price(float good_price) {
        this.good_price = good_price;
    }

    public float getDelivery_price() {
        return delivery_price;
    }

    public void setDelivery_price(float delivery_price) {
        this.delivery_price = delivery_price;
    }

    public float getHelper_cost() {
        return helper_cost;
    }

    public void setHelper_cost(float helper_cost) {
        this.helper_cost = helper_cost;
    }

    public float getOther_cost() {
        return other_cost;
    }

    public void setOther_cost(float other_cost) {
        this.other_cost = other_cost;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }
}
