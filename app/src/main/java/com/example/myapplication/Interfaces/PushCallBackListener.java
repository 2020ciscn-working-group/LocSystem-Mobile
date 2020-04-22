package com.example.myapplication.Interfaces;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public interface PushCallBackListener {
    void onPushSuccessfully(String data);

    void onPushFailed(int code);

}
