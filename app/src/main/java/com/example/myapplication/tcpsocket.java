package com.example.myapplication;

import android.app.job.JobWorkItem;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class tcpsocket extends Thread{
    private String           threadname;
    private Socket           socket;
    private jsontrans        jts;
    private DataOutputStream pw;
    private DataInputStream  re;
    private String           json;
    private boolean          write;
    private boolean          flash;
    private int              type=0;
    public synchronized boolean isFlash() {
        return flash;
    }
    public synchronized  boolean isWrite() {
        return write;
    }

    public synchronized void sendJson(int type,String message) {
        this.write = true;
        json=message;
        this.type=type;
    }
    public synchronized void test(){
        write=true;
        type=-1;
    }
    public synchronized void sendObject(Object ob,int type){
        switch (type){
            case 1:{
                tocken tt=(tocken) ob;
                json=jts.trans_tocken_to_json(tt);
                write=true;
                this.type=type;
                break;
            }
            case 2:{
                hub[] tt=(hub[]) ob;
                json=jts.trans_hublist_to_json(tt);
                write=true;
                this.type=type;
                break;
            }
            //其余的再填
        }
    }
    public synchronized  void getMessage(owner_date owd){
        owd.setJson(json);
        owd.setType(type);
        switch (type){
            case 1:{
                tocken tt=jts.trans_tocken_from_json(json);
                owd.addTocken(tt);
                break;
            }
            case 2:{
                hub[] hl=jts.trans_hublist_from_json(json);
                owd.addHub(hl);
                break;
            }
            //其余的再填
        }
        flash=false;
        json=null;
        type=0;
    }

    public tcpsocket(String name){
        jts=new jsontrans();
        threadname=name;
        write=false;
        flash=false;
    }

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
            //开始通信
            while(true) {
                        if(write){
                            switch (type){
                                case 1:{
                                    pw.write(1);
                                    pw.writeBytes(json);
                                    break;
                                }
                                case 2:{
                                    pw.write(2);
                                    pw.writeBytes(json);
                                    break;
                                }
                                case -1:{
                                    pw.write(-1);
                                    pw.writeBytes("你好，服务器");
                                    break;
                                }
                            }
                    write=false;
                    json=null;
                    type=0;
                }
                int len=0;
                byte[] arr = new byte[1024];
                try{
                    type=re.readInt();
                } catch (IOException e) {
                    type=0;
                    continue;
                }
               switch (type){
                   case -1:{
                       len=re.read(arr);
                       Log.d("socket",new String(arr,0,len));
                       break;
                   }
                   case 0:break;
                   default:{
                       String me=new String();
                       while ((len=re.read(arr))!=0)
                           me=me+arr.toString();
                       json=me;
                       flash=true;
                       break;
                   }
               }
            }
        } catch (IOException e) {
            Log.d("socket",e.getMessage());
            e.printStackTrace();
        }
    }
    public String getThreadname() {
        return threadname;
    }
}