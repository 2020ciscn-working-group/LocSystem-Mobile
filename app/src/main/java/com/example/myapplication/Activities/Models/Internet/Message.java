package com.example.myapplication.Activities.Models.Internet;

import com.google.gson.Gson;

import java.io.Serializable;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Message extends InternetBase implements Serializable {
    private String hosi_id;
    private String guest_id;
    //private String date;
    private int msg_type;
    private String message;

    public Message(){}
    public Message(String hosi_id,String guest_id,
                   //String date,
                   String message,int msg_type){
        //this.date=date;
        this.guest_id=guest_id;
        this.hosi_id=hosi_id;
        this.message=message;
        this.msg_type=msg_type;
    }

    public int getmsg_type() {
        return msg_type;
    }

    public void setmsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public String gethosi_id() {
        return hosi_id;
    }

    public void sethosi_id(String hosi_id) {
        this.hosi_id = hosi_id;
    }

    public String getguest_id() {
        return guest_id;
    }

    public void setguest_id(String guest_id) {
        this.guest_id = guest_id;
    }
/*
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
*/
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toJson() {
        Gson gson=new Gson();
        return gson.toJson(this,Message.class);
    }
}
