package com.example.myapplication.DateStract;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class RootReq {
    //密钥公钥
    private byte[] pubkey;
    private byte[] prikey;

    //该密钥类型
    private int type;
    //密钥信息
    private String info;

    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public byte[] getPrikey() {
        return prikey;
    }

    public void setPrikey(byte[] prikey) {
        this.prikey = prikey;
    }

    public void setPubkey(byte[] pubkey) {
        this.pubkey = pubkey;
    }
    public byte[] getPubkey() {
        return pubkey;
    }

    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public String getInfo() {
        return info;
    }
}
