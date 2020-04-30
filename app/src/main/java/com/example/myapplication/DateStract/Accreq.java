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

    private String accsee;//授权范围
    private String time;//授权时间
    private String info;//个人信息
    private String hubuuid;
    private long infolen;//个人信息长度
    private byte[] signdata;//签名数据
    private long signdatalen;//签名数据长度

    public String getHubuuid() {
        return hubuuid;
    }

    public void setHubuuid(String hubuuid) {
        this.hubuuid = hubuuid;
    }

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

    public void setsigndata(byte[] signdata) {
        this.signdata = signdata;
    }
    public byte[] getsigndata() {
        return signdata;
    }

    public void setsigndatalen(long signdatalen) {
        this.signdatalen = signdatalen;
    }
    public long getsigndatalen() {
        return signdatalen;
    }

}