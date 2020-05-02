package com.example.myapplication.Activities.Models.Internet;

import com.google.gson.Gson;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class SendMessage extends InternetBase {

    private String host_id;
    private String guest_id;
    private String message;
    private int msg_type;
    public void setHost_id(String host_id) {
        this.host_id = host_id;
    }
    public String getHost_id() {
        return host_id;
    }

    public void setGuest_id(String guest_id) {
        this.guest_id = guest_id;
    }
    public String getGuest_id() {
        return guest_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }
    public int getMsg_type() {
        return msg_type;
    }
    @Override
    public String toJson() {
        Gson gson=new Gson();
        return gson.toJson(this,SendMessage.class);
    }
}
