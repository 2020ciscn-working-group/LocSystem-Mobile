package com.example.myapplication.Activities.Models.Internet;

import com.example.myapplication.Utils.Gm_sm2_3;
import com.example.myapplication.Utils.jsontrans;
import com.google.gson.Gson;

import java.util.List;

/**
 * Copyright 2020 bejson.com
 */
import java.util.List;

/**
 * Auto-generated: 2020-04-24 2:25:56
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class User extends InternetBase{

    private String uid;
    private String username;
    private String password;
    private String phoneNum;
    private List<String> friendUidList;
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUid() {
        return uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public String getPhoneNum() {
        return phoneNum;
    }

    public void setFriendUidList(List<String> friendUidList) {
        this.friendUidList = friendUidList;
    }
    public List<String> getFriendUidList() {
        return friendUidList;
    }


    public User(){}
    public User(String uid, String username, String passwd, String phoneNum, List<String>friendUidList){
        this.friendUidList=friendUidList;
        this.password=passwd;
        this.phoneNum=phoneNum;
        this.uid=uid;
        this.username=username;
    }

    @Override
    public String toJson(){
        Gson gson=new Gson();
        return gson.toJson(this,User.class);
    }
}
