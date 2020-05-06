package com.example.myapplication.Activities.Models.Internet;

import com.example.myapplication.Utils.Gm_sm2_3;
import com.example.myapplication.Utils.jsontrans;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.LinkedList;
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
public class User extends InternetBase implements Serializable {

    private String uid;
    private String username;
    private String password;
    private String phoneNum;
    private List<Friend> friendList;

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
    public Friend getFriend(String uid){
        for(Friend friend:friendList){
            if(friend.getFirend_uid().equals(uid))
                return friend;
        }
        return null;
    }

    public void setFriendUidList(List<Friend> friendUidList) {
        this.friendList = friendUidList;
    }
    public List<Friend> getFriendUidList() {
        return friendList;
    }
    public void addFriend(Friend fuid){
        friendList.add(fuid);
    }

    public User(){
        friendList= new LinkedList<>();
        password=null;
        phoneNum=null;
        uid=null;
        username=null;
    }
    public User(String uid, String username, String passwd, String phoneNum){
        this.friendList= new LinkedList<>();
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
