package com.example.myapplication.Activities.Models.Internet;

import com.google.gson.Gson;

import java.io.Serializable;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Message extends InternetBase implements Serializable {
    private String hash;
    private String frienduid;
    private String date;
    private String message;

    public Message(){}
    public Message(String hash,String frienduid,String date,String message){
        this.date=date;
        this.frienduid=frienduid;
        this.hash=hash;
        this.message=message;
    }
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getFrienduid() {
        return frienduid;
    }

    public void setFrienduid(String frienduid) {
        this.frienduid = frienduid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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
