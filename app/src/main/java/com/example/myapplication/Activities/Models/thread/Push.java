package com.example.myapplication.Activities.Models.thread;

import android.os.AsyncTask;
import android.util.Log;
import com.example.myapplication.Interfaces.PushCallBackListener;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Push extends AsyncTask <String, Void, String>{
    private String       url;
    private PushCallBackListener mPushCallBackListener;
    private boolean success=true;
    private int code;
    private String msg=null;

    public Push(String url,PushCallBackListener pushCallBackListener){
        mPushCallBackListener = pushCallBackListener;
        this.url=url;
    }
    @Override
    protected String doInBackground(String... voids) {
        try {
            URL Url=new URL(url);
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
            // 设置连接超时为5秒
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            // 设置请求类型为POST类型
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");//设置参数类型是json格式
            //conn.setRequestProperty("Connection", "Keep-Alive");
            //conn.setRequestProperty("logType", "base");

            //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 判断请求Url是否成功
            conn.connect();
            OutputStream outputStream=conn.getOutputStream();
            byte[]src=voids[0].getBytes(StandardCharsets.UTF_8);
            outputStream.write(src);
            outputStream.flush();
            outputStream.close();
            code=conn.getResponseCode();
            Log.d("push log:",String.valueOf(code));
            if ( code== 200) {
                // 获取响应的输入流对象
                InputStream is = conn.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte[] buffer = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    message.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                message.close();
                // 返回字符串
                msg = new String(message.toByteArray());
                if(msg.equals("404")||msg.equals("406")||msg.equals("NOT FOUND")||msg.contains("Empty"))
                    success=false;
            }else{
                success=false;
                msg=conn.getResponseMessage();
                Log.d("pusherr",msg+code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    //ui
    protected void onPostExecute(String json){
        if(success) {
            try {
                mPushCallBackListener.onPushSuccessfully(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            mPushCallBackListener.onPushFailed(code,json);
    }

    //
}
