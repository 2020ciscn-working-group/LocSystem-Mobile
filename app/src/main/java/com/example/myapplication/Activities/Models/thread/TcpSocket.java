package com.example.myapplication.Activities.Models.thread;

import android.util.Log;

import com.example.myapplication.Utils.jsontrans;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class TcpSocket implements Runnable {
    private String           threadname;
    private Socket           socket;
    private jsontrans        jts;
    private DataOutputStream pw;
    private DataInputStream  re;

    @Override
    public void run() {
        try {
            Log.d("Socket","正在连接服务端...");
            /**
             * 实例化Socket时需要传入两个参数：
             * 1：服务端的IP地址
             * 2：服务端的端口号
             * 通过IP地址可以找到服务端所在的计算机
             * 通过端口可以找到运行在服务端计算机上的
             * 服务端应用程序
             * 注意，实例化Socket的过程就是连接的过程，若
             * 连接失败就会抛出异常。
             * 有两个参数，一个是IP地址，一个是端口号
             */
            //所有应用程序在使用网络的时候都要和操作系统申请一个网络端口
            //异常要我们自己进行处理，不要抛出
            Log.d("Socket","正在连接服务端...");
            socket = new Socket("10.0.2.2", 9000);
            Log.d("Socket","与服务端建立连接！");
            pw = new DataOutputStream(socket.getOutputStream());
            re =new DataInputStream(socket.getInputStream());
    } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
