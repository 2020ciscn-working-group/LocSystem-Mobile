package com.example.myapplication.Activities.Models.Internet;

import com.google.gson.Gson;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Login extends InternetBase {
    private String password;
    private String uid;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    @Override
    public String toJson() {
        //Gson gson=new Gson();
        //return gson.toJson(this,Login.class);
        return "{\" uid\"：\" "+uid+"\"\" password\"：\""+password+"\"，}";
    }
}
