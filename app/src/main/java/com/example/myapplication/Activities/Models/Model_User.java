package com.example.myapplication.Activities.Models;

import com.example.myapplication.Activities.Models.Internet.Friend;
import com.example.myapplication.Activities.Models.Internet.User;
import com.example.myapplication.Utils.Gm_sm2_3;

import java.util.LinkedList;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Model_User extends Model_Basic {
    private String UUID=null;
    private User mUser;
    private LinkedList<Friend>mFriends;
    private LinkedList<String>MessageList;
    //private MessageDataBase mMessageDataBase;
    //private


    @Override
    public byte[] getSM3() {
        Gm_sm2_3 gm_sm2_3=Gm_sm2_3.getInstance();

        return new byte[0];
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User users) {
        mUser = users;
    }

    public LinkedList<Friend> getFriends() {
        return mFriends;
    }

    public void setFriends(LinkedList<Friend> friends) {
        mFriends = friends;
    }

    public LinkedList<String> getMessageList() {
        return MessageList;
    }

    public void setMessageList(LinkedList<String> messageList) {
        MessageList = messageList;
    }
}
