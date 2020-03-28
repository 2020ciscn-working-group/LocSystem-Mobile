package com.example.myapplication.DateStract;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Cert {
    private String algo_key;
    private String  algo_hash;
    private String info;
    private byte[] signdate;
    private long signdatelen;

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

    public byte[] getSigndate() {
        return signdate;
    }

    public void setSigndate(byte[] signdate) {
        this.signdate = signdate;
    }

    public long getSigndatelen() {
        return signdatelen;
    }

    public void setSigndatelen(long signdatelen) {
        this.signdatelen = signdatelen;
    }
}
