/**
 * Copyright 2020 bejson.com
 */
package com.example.myapplication.DateStract;

import java.io.Serializable;

/**
 * Auto-generated: 2020-03-22 2:58:55
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class LocalKey implements Serializable {

    private byte[] pubkey;
    private byte[] prikey;
    private int type;
    private String info;
    private byte[] signdata;
    private byte[] certdata;
    public void setPubkey(byte[] pubkey) {
        this.pubkey = pubkey;
    }
    public byte[] getPubkey() {
        return pubkey;
    }

    public void setPrikey(byte[] prikey) {
        this.prikey = prikey;
    }
    public byte[] getPrikey() {
        return prikey;
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

    public void setSigndata(byte[] signdata) {
        this.signdata = signdata;
    }
    public byte[] getSigndata() {
        return signdata;
    }

    public void setCertdata(byte[] certdata) {
        this.certdata = certdata;
    }
    public byte[] getCertdata() {
        return certdata;
    }

}