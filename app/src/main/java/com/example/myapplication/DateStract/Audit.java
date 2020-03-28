/**
 * Copyright 2020 bejson.com
 */
package com.example.myapplication.DateStract;

import java.io.Serializable;

/**
 * Auto-generated: 2020-03-23 4:21:52
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Audit implements Serializable {

    private String uuid;
    private com.example.myapplication.DateStract.Auditexp Auditexp;
    private byte[]                             signdate;
    private long                             signdatelen;

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