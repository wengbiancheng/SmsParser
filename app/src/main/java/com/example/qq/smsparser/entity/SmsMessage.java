package com.example.qq.smsparser.entity;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * 短信实体类，主要简化系统的短信实体类
 */
public class SmsMessage implements Serializable{

    private int id;
    private String body;
    private String time;
    private String number;
//    private ImageView icon;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

//    public ImageView getIcon() {
//        return icon;
//    }
//
//    public void setIcon(ImageView icon) {
//        this.icon = icon;
//    }
}
