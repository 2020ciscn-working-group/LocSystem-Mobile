package com.example.myapplication.Activities.Models.Internet;

import com.google.gson.Gson;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Vue_Accreq extends InternetBase {

    private int accsee;
    private String hubuuid;
    private String info;
    public void setAccsee(int accsee) {
        this.accsee = accsee;
    }
    public int getAccsee() {
        return accsee;
    }

    public void setHubuuid(String hubuuid) {
        this.hubuuid = hubuuid;
    }
    public String getHubuuid() {
        return hubuuid;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public String getInfo() {
        return info;
    }
    @Override
    public String toJson() {
        Gson gson=new Gson();
        return gson.toJson(this,Vue_Accreq.class);
    }
}
