/**
 * Copyright 2020 bejson.com
 */
package com.example.myapplication.DateStract;

import java.io.Serializable;

/**
 * Auto-generated: 2020-03-22 2:52:47
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class RemoteKey implements Serializable {

    private String uuid;

    //密钥公钥
    private byte[] pubkey;
    //该密钥类型
    private int type;
    //密钥信息
    private String info;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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