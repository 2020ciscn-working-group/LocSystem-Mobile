/**
 * Copyright 2020 bejson.com
 */
package com.example.myapplication.DateStract;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Auto-generated: 2020-03-23 4:21:52
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Audit implements Serializable {//审计

    private String uuid;//实例UUID
    private com.example.myapplication.DateStract.Auditexp Auditexp;//审计实例
    private byte[]                             signdata;//签名数据
    private long                             signdatalen;//签名数据长度

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setAuditexp(com.example.myapplication.DateStract.Auditexp Auditexp) {
        this.Auditexp = Auditexp;
    }
    public com.example.myapplication.DateStract.Auditexp getAuditexp() {
        return Auditexp;
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
    public String toJson(){
        Gson gson=new Gson();
        return gson.toJson(this,Audit.class);
    }
}