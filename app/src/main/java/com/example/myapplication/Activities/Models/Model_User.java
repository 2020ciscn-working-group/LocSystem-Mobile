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
    private User mUsers;
    private LinkedList<Friend>mFriends;
    //private MessageDataBase mMessageDataBase;
    //private


    @Override
    public byte[] getSM3() {
        Gm_sm2_3 gm_sm2_3=Gm_sm2_3.getInstance();

        return new byte[0];
    }
}
