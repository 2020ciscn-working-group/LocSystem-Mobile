package com.example.myapplication.Activities.Models.Internet;

import com.example.myapplication.Utils.Gm_sm2_3;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public abstract class InternetBase {
    public  byte[] getSM3(){
        Gm_sm2_3 gm_sm2_3=Gm_sm2_3.getInstance();
        byte[] src=this.toJson().getBytes();
        byte[]output=new byte[32];
        gm_sm2_3.sm3(src,src.length,output);
        return output;
    }
    public  String getSM3str(){
        Gm_sm2_3 gm_sm2_3=Gm_sm2_3.getInstance();
        byte[] src=this.toJson().getBytes();
        byte[]output=new byte[32];
        return gm_sm2_3.sm3(src,src.length,output);
    };
    public abstract String toJson();
}
