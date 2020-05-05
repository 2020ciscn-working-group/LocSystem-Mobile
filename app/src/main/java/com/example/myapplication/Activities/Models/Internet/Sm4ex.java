package com.example.myapplication.Activities.Models.Internet;

import com.example.myapplication.Defin.Defin_crypto;
import com.example.myapplication.Utils.Util;
import com.example.myapplication.Utils.utils.sm4.SM4Utils;
import com.google.gson.Gson;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Sm4ex extends InternetBase {
    private String sm4k;
    private String sm4iv;

    public String getSm4k() {
        return sm4k;
    }

    public void setSm4k(String sm4k) {
        this.sm4k = sm4k;
    }

    public String getSm4iv() {
        return sm4iv;
    }

    public void setSm4iv(String sm4iv) {
        this.sm4iv = sm4iv;
    }

    public void keygen() throws Exception {
        sm4k= Util.byteToHex(SM4Utils.genersm4key());
        sm4iv= Defin_crypto.iv;
    }

    @Override
    public String toJson() {
        Gson gson=new Gson();
        return gson.toJson(this,Sm4ex.class);
    }
}
