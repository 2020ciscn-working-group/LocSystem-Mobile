package com.example.myapplication.Activities.Models.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class PullScheduledThreadPoolExecutor  {
    private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor=null;
    private volatile String json;
    private String url;
    public PullScheduledThreadPoolExecutor(String url){
        mScheduledThreadPoolExecutor=new ScheduledThreadPoolExecutor(1);
        this.url=url;
    }

    public synchronized String getJson() {
        return json;
    }

    public void Executor() throws ExecutionException, InterruptedException {
        Pull pull=new Pull(url);
        FutureTask<String>futureTask=new FutureTask<String>(pull);
        mScheduledThreadPoolExecutor.scheduleAtFixedRate(futureTask,1000,1500, TimeUnit.MILLISECONDS);
        if(futureTask.isDone()){
            json=futureTask.get();
        }
    }
    public void ShutDown(){
        mScheduledThreadPoolExecutor.shutdown();
    }
    public void ShutDownNow(){
        mScheduledThreadPoolExecutor.shutdownNow();
    }

}
