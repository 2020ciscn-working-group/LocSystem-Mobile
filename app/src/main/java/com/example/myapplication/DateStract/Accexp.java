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
public class Accexp implements Serializable {

    private Accreq accreq;
    private byte[] PIK;
    private byte[] signkey;
    private String info;
    private String access;
    private String acctime;
    private String accendtime;
    public void setAccreq(Accreq accreq) {
        this.accreq = accreq;
    }
    public Accreq getAccreq() {
        return accreq;
    }

    public void setPIK(byte[] PIK) {
        this.PIK = PIK;
    }
    public byte[] getPIK() {
        return PIK;
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

    public void setAccess(String access) {
        this.access = access;
    }
    public String getAccess() {
        return access;
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