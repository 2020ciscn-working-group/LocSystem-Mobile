package com.example.myapplication.Activities.Models.Internet;

import com.google.gson.Gson;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class SignUp extends InternetBase {
    private String uid;
    private String username;
    private String password;
    private String phoneNum;
    public SignUp(String uid, String uname, String passwd, String phnum){
        this.uid=uid;
        username=uname;
        password=passwd;
        phoneNum=phnum;
    }
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

    @Override
    public String toJson() {
        Gson gson=new Gson();
        return gson.toJson(this,SignUp.class);
    }

}
