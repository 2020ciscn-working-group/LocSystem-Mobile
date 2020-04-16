package com.example.myapplication.Activities.Models.thread;

import android.os.AsyncTask;
import android.util.Log;

import com.example.myapplication.Dao.Sql.AppSql;
import com.example.myapplication.Interfaces.PullCallBack;
import com.example.myapplication.Utils.StreamTool;
import com.example.myapplication.Utils.jsontrans;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Pull extends AsyncTask <Void, Void,String>{
    private String       url;
    private String       json=null;
    private int          type=0;
    private PullCallBack mPullCallBack;
    public Pull(String url){
        this.url=url;
    }
    public void setPullCallBack(PullCallBack mPullCallBack) {
        this.mPullCallBack = mPullCallBack;
    }


    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL Url=new URL(url);
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
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
            json=new String(bt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    //ui
    protected void onPostExecute(String json){
        super.onPreExecute();
        if(json!=null)
            mPullCallBack.onPullCallBackSuccessfully(json);
        else mPullCallBack.onPullCallBackFailed();

    }

    //
}
