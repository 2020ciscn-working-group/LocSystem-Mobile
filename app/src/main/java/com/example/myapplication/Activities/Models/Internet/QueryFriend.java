package com.example.myapplication.Activities.Models.Internet;

import com.google.gson.Gson;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class QueryFriend extends InternetBase {
    private String friend_uid;
    public void setFriend_uid(String friend_uid) {
        this.friend_uid = friend_uid;
    }
    public String getFriend_uid() {
        return friend_uid;
    }

    @Override
    public String toJson() {
        Gson gson=new Gson();
        return gson.toJson(this,QueryFriend.class);
    }
}
