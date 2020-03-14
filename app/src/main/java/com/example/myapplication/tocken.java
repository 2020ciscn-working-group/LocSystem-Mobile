package com.example.myapplication;

import java.io.Serializable;

/**
 * Auto-generated: 2019-08-19 23:14:39
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class tocken implements Serializable {

    private String token_uuid;
    private String Owner_uuid;
    private String Guest_uuid;
    private String Hub_uuid;
    private int Lock_id;
    private String starttime;
    private String endtime;
    private int signdatasize;
    private String signdata;

    public String getToken_uuid() {
        return token_uuid;
    }

    public void setToken_uuid(String token_uuid) {
        this.token_uuid = token_uuid;
    }

    public String getOwner_uuid() {
        return Owner_uuid;
    }

    public void setOwner_uuid(String owner_uuid) {
        Owner_uuid = owner_uuid;
    }

    public String getGuest_uuid() {
        return Guest_uuid;
    }

    public void setGuest_uuid(String guest_uuid) {
        Guest_uuid = guest_uuid;
    }

    public String getHub_uuid() {
        return Hub_uuid;
    }

    public void setHub_uuid(String hub_uuid) {
        Hub_uuid = hub_uuid;
    }

    public int getLock_id() {
        return Lock_id;
    }

    public void setLock_id(int lock_id) {
        Lock_id = lock_id;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getSigndatasize() {
        return signdatasize;
    }

    public void setSigndatasize(int signdatasize) {
        this.signdatasize = signdatasize;
    }

    public String getSigndata() {
        return signdata;
    }

    public void setSigndata(String signdata) {
        this.signdata = signdata;
    }
}