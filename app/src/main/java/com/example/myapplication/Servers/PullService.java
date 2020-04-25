package com.example.myapplication.Servers;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/
public class PullService extends Service {
    private final String TAG = "pullservice";
    private boolean quit;
    private Thread thread;
    private LocalBinder binder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        // 声明一个方法，getService。（提供给客户端调用）
        public PullService getService() {
            // 返回当前对象LocalService,这样我们就可在客户端端调用Service的公共方法了
            return PullService.this;
        }
    }
    @Override
    public void onCreate() {


    }
    @Override
    public void onRebind(Intent intent) {

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        return flags;
    }

}
