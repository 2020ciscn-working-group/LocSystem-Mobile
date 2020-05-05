package com.example.myapplication.Activities.Models.Internet;

import com.google.gson.Gson;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class FriendQuery extends InternetBase {
    private String friend_uid;
    private String username;
    private String phoneNum;
    public void setFriend_uid(String friend_uid) {
        this.friend_uid = friend_uid;
    }
    public String getFriend_uid() {
        return friend_uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public String getPhoneNum() {
        return phoneNum;
    }

    @Override
    public String toJson() {
        Gson gson=new Gson();
        return gson.toJson(this,FriendQuery.class);
    }
}
