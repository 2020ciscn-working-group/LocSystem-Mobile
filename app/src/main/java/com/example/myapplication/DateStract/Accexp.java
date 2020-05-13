/**
 * Copyright 2020 bejson.com
 */
package com.example.myapplication.DateStract;

import java.io.Serializable;

/**
 * Auto-generated: 2020-03-23 3:43:46
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Accexp implements Serializable {//授权申请实例

    private Accreq accreq;//申请授权包
    private byte[] RootKey;//RootKey公钥
    private byte[] signkey;//签名密钥
    private String info;//个人信息
    private int access;//授权范围
    private String acctime;//授权时间
    private String accendtime;//授权终止时间
    public void setAccreq(Accreq accreq) {
        this.accreq = accreq;
    }
    public Accreq getAccreq() {
        return accreq;
    }

    public void setRootKey(byte[] RootKey) {
        this.RootKey = RootKey;
    }
    public byte[] getRootKey() {
        return RootKey;
    }

    public void setSignkey(byte[] signkey) {
        this.signkey = signkey;
    }
    public byte[] getSignkey() {
        return signkey;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public String getInfo() {
        return info;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public void setAcctime(String acctime) {
        this.acctime = acctime;
    }
    public String getAcctime() {
        return acctime;
    }

    public void setAccendtime(String accendtime) {
        this.accendtime = accendtime;
    }
    public String getAccendtime() {
        return accendtime;
    }

}