package com.example.myapplication.DateStract;

import com.google.gson.Gson;

import java.io.Serializable;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Cert implements Serializable {
    private String algo_key;
    private String algo_hash;
    private String uuid;
    private String info;
    private int    type;
    private byte[] pubkey;
    private byte[] signdata;
    private byte[] certdata;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getPubkey() {
        return pubkey;
    }

    public void setPubkey(byte[] pubkey) {
        this.pubkey = pubkey;
    }

    public String getuuid() {
        return uuid;
    }

    public void setuuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAlgo_key() {
        return algo_key;
    }

    public void setAlgo_key(String algo_key) {
        this.algo_key = algo_key;
    }

    public String getAlgo_hash() {
        return algo_hash;
    }

    public void setAlgo_hash(String algo_hash) {
        this.algo_hash = algo_hash;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public byte[] getsigndata() {
        return signdata;
    }

    public void setsigndata(byte[] signdata) {
        this.signdata = signdata;
    }

    public byte[] getcertdata() {
        return certdata;
    }

    public void setcertdata(byte[] certdata) {
        this.certdata = certdata;
    }
    public String toJson(){
        Gson gson=new Gson();
        return gson.toJson(this,Cert.class);
    }
}
