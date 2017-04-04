package com.example.qq.smsparser.entity;

import java.io.Serializable;

/**
 * 订货信息的付款实体类
 */
public class PayMessage implements Serializable {
    private String order_id;
    private boolean pay;

    @Override
    public String toString() {
        return "order_id:"+getOrder_id()+";pay:"+pay;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public boolean isPay() {
        return pay;
    }

    public void setPay(boolean pay) {
        this.pay = pay;
    }
}
