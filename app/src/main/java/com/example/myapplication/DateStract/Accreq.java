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
public class Accreq implements Serializable {

    private String accsee;
    private String time;
    private String info;
    private long infolen;
    private byte[] signdate;
    private long signdatelen;
    public void setAccsee(String accsee) {
        this.accsee = accsee;
    }
    public String getAccsee() {
        return accsee;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getTime() {
        return time;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public String getInfo() {
        return info;
    }

    public void setInfolen(long infolen) {
        this.infolen = infolen;
    }
    public long getInfolen() {
        return infolen;
    }

    public void setSigndate(byte[] signdate) {
        this.signdate = signdate;
    }
    public byte[] getSigndate() {
        return signdate;
    }

    public void setSigndatelen(long signdatelen) {
        this.signdatelen = signdatelen;
    }
    public long getSigndatelen() {
        return signdatelen;
    }

}