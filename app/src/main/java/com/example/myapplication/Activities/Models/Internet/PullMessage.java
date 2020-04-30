package com.example.myapplication.Activities.Models.Internet;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class PullMessage extends InternetBase {

    private String host_id;
    private String guest_id;
    public void setHost_id(String host_id) {
        this.host_id = host_id;
    }
    public String getHost_id() {
        return host_id;
    }

    public void setGuest_id(String guest_id) {
        this.guest_id = guest_id;
    }
    public String getGuest_id() {
        return guest_id;
    }
    public PullMessage(String host,String guset){
        host_id=host;
        guest_id=guset;
    }
    @Override
    public String toJson() {

        return null;
    }
}
