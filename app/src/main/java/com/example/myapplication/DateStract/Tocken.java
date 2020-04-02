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
    private byte[] signdata;
    private long singdatalen;
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

    public void setSigndata(byte[] signdata) {
        this.signdata = signdata;
    }
    public byte[] getSigndate() {
        return signdata;
    }

    public void setSingdatalen(long singdatalen) {
        this.singdatalen = singdatalen;
    }
    public long getSingdatalen() {
        return singdatalen;
    }

}