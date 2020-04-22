package com.example.myapplication.Activities.Models.thread;

import com.example.myapplication.Defin.Defin_internet;
import com.example.myapplication.Utils.StreamTool;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Pull implements Callable {
    private String detail = "";
    private String Url=null;
    public Pull(String url){
        Url=url;
    }
    @Override
    public String call() throws Exception {
        URL url = new URL(Url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置连接超时为5秒
        conn.setConnectTimeout(5000);
        // 设置请求类型为Get类型
        conn.setRequestMethod("GET");
        // 判断请求Url是否成功
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("请求url失败");
        }
        InputStream inStream = conn.getInputStream();
        byte[] bt = StreamTool.read(inStream);
        inStream.close();
        return new String(bt);
    }
}
