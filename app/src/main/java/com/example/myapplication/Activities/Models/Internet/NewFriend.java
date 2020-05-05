package com.example.myapplication.Activities.Models.Internet;

import com.google.gson.Gson;

import org.bouncycastle.asn1.crmf.EncKeyWithID;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class NewFriend extends InternetBase {
    private String uid;
    private String friend_uid;

    public NewFriend(String uid,String fuid){
        this.uid=uid;
        friend_uid=fuid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFriend_uid() {
        return friend_uid;
    }

    public void setFriend_uid(String friend_uid) {
        this.friend_uid = friend_uid;
    }

    @Override
    public String toJson() {
        Gson gson= new Gson();
        return gson.toJson(this,NewFriend.class);
    }
}
