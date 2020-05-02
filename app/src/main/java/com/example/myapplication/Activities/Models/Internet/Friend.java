package com.example.myapplication.Activities.Models.Internet;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Friend extends InternetBase implements Serializable {
    private String        Guestid;
    private String        firend_uid;
    private String username;
    private List<Message> MessagehashList;
    private String sm4key;
    public Friend(){}
    public Friend(String uid,String firend_uid){
        this.firend_uid=firend_uid;
        Guestid=uid;
    }

    public List<Message> getMessagehashList() {
        return MessagehashList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMessagehashList(List<Message> messageList) {
        MessagehashList = messageList;
    }
    public String getGuestid() {
        return Guestid;
    }

    public void setGuestid(String uid) {
        Guestid = uid;
    }

    public String getFirend_uid() {
        return firend_uid;
    }

    public void setFirend_uid(String firend_uid) {
        this.firend_uid = firend_uid;
    }

    public String getSm4key() {
        return sm4key;
    }

    public void setSm4key(String sm4key) {
        this.sm4key = sm4key;
    }

    @Override
    public String toJson() {
        Gson gson=new Gson();
        return gson.toJson(this,Friend.class);
    }
}
