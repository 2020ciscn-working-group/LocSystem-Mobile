package com.example.myapplication.Activities.Models.Internet;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Friend extends InternetBase implements Serializable {
    private String        uid;
    private String        firend_uid;
    private List<Message> MessagehashList;
    private String sm4key;
    public Friend(){}
    public Friend(String uid,String firend_uid){
        this.firend_uid=firend_uid;
        this.uid=uid;
    }

    public List<Message> getMessagehashList() {
        return MessagehashList;
    }

    public void setMessagehashList(List<Message> messageList) {
        MessagehashList = messageList;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
