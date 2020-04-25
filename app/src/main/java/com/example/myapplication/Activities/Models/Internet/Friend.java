package com.example.myapplication.Activities.Models.Internet;

import com.google.gson.Gson;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Friend extends InternetBase {
    private String uid;
    private String firend_uid;

    public Friend(){}
    public Friend(String uid,String firend_uid){
        this.firend_uid=firend_uid;
        this.uid=uid;
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


    @Override
    public String toJson() {
        Gson gson=new Gson();
        return gson.toJson(this,Friend.class);
    }
}
