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

    private final float EACH_GOOD_HELPER_COST=0.1f;
    private final float EACH_GOOD_ORTHER_COST=0.2f;

    @Override
    public String toString() {
        return "订货号是:"+orderId+";月份是:"+month+";帮工id是:"+helpId+";商品的销售额是:"+good_price+";快递费用是:"+delivery_price+";帮工的工资水平是:"
                +helper_cost+";包装费用是:"+other_cost+";纯利润是:"+getProfit();
    }

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

    public void setHelper_cost(int number) {
        this.helper_cost = number*EACH_GOOD_HELPER_COST;
    }

    public float getOther_cost() {
        return other_cost;
    }

    public void setOther_cost(int number) {
        this.other_cost = number*EACH_GOOD_ORTHER_COST;
    }

    public float getProfit() {
        return good_price-delivery_price-helper_cost-other_cost;
    }
}
