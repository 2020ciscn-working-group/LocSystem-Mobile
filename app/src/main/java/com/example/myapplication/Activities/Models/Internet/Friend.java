package com.example.myapplication.Activities.Models.Internet;

import com.google.gson.Gson;

import org.jetbrains.annotations.Contract;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Friend extends InternetBase implements Serializable {
    private String        Guestid;
    private String        firend_uid;
    private String username;
    private String phnum;
    private List<Message> MessagehashList;
    private String sm4key;
    private String sm4iv;
    public Friend(){}
    public Friend(String firend_uid,String phnum,String uname){
        this.firend_uid=firend_uid;
        this.phnum=phnum;
        username=uname;
        MessagehashList=new LinkedList<>();
    }

    public final String getPhnum() {
        return phnum;
    }

    public synchronized void setPhnum(String phnum) {
        this.phnum = phnum;
    }

    public final List<Message> getMessagehashList() {
        return MessagehashList;
    }

    public final String getUsername() {
        return username;
    }

    public synchronized void setUsername(String username) {
        this.username = username;
    }

    public synchronized void setMessagehashList(List<Message> messageList) {
        MessagehashList = messageList;
    }
    public final String getGuestid() {
        return Guestid;
    }

    public synchronized void setGuestid(String uid) {
        Guestid = uid;
    }

    public final String getFirend_uid() {
        return firend_uid;
    }

    public synchronized void setFirend_uid(String firend_uid) {
        this.firend_uid = firend_uid;
    }

    public final String getSm4key() {
        return sm4key;
    }

    public synchronized void setSm4key(String sm4key) {
        this.sm4key = sm4key;
    }

    public final String getSm4iv() {
        return sm4iv;
    }

    public synchronized void setSm4iv(String sm4iv) {
        this.sm4iv = sm4iv;
    }

    @Override
    public String toJson() {
        Gson gson=new Gson();
        return gson.toJson(this,Friend.class);
    }
}
