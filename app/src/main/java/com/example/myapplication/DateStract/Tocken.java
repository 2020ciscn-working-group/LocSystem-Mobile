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
public class Tocken implements Serializable {

    private String uuid;
    private Accexp accexp;
    private byte[] signdate;
    private long singdatelen;
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getUuid() {
        return uuid;
    }

    public void setAccexp(Accexp accexp) {
        this.accexp = accexp;
    }
    public Accexp getAccexp() {
        return accexp;
    }

    public void setSigndate(byte[] signdate) {
        this.signdate = signdate;
    }
    public byte[] getSigndate() {
        return signdate;
    }

    public void setSingdatelen(long singdatelen) {
        this.singdatelen = singdatelen;
    }
    public long getSingdatelen() {
        return singdatelen;
    }

}