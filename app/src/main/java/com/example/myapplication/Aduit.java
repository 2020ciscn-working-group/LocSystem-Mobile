package com.example.myapplication;
/**
 * Auto-generated: 2019-08-22 1:19:39
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Aduit {

    private String Token_uuid;
    private String Owner_uuid;
    private String Guest_uuid;
    private String Hub_uuid;
    private String Lock_id;
    private String fieldaccesstime;
    private long Nonce;
    private int Signdatasize;
    private String signdata;

    public Aduit(String token_uuid, String owner_uuid, String guest_uuid, String hub_uuid, String lock_id, String fieldaccesstime, long nonce, int signdatasize, String signdata) {
        Token_uuid = token_uuid;
        Owner_uuid = owner_uuid;
        Guest_uuid = guest_uuid;
        Hub_uuid = hub_uuid;
        Lock_id = lock_id;
        this.fieldaccesstime = fieldaccesstime;
        Nonce = nonce;
        Signdatasize = signdatasize;
        this.signdata = signdata;
    }
    public Aduit(){}
    public void setToken_uuid(String Token_uuid) {
        this.Token_uuid = Token_uuid;
    }
    public String getToken_uuid() {
        return Token_uuid;
    }

    public void setOwner_uuid(String Owner_uuid) {
        this.Owner_uuid = Owner_uuid;
    }
    public String getOwner_uuid() {
        return Owner_uuid;
    }

    public void setGuest_uuid(String Guest_uuid) {
        this.Guest_uuid = Guest_uuid;
    }
    public String getGuest_uuid() {
        return Guest_uuid;
    }

    public void setHub_uuid(String Hub_uuid) {
        this.Hub_uuid = Hub_uuid;
    }
    public String getHub_uuid() {
        return Hub_uuid;
    }

    public void setLock_id(String Lock_id) {
        this.Lock_id = Lock_id;
    }
    public String getLock_id() {
        return Lock_id;
    }

    public void setFieldaccesstime(String fieldaccesstime) {
        this.fieldaccesstime = fieldaccesstime;
    }
    public String getFieldaccesstime() {
        return fieldaccesstime;
    }

    public void setNonce(long Nonce) {
        this.Nonce = Nonce;
    }
    public long getNonce() {
        return Nonce;
    }

    public void setSigndatasize(int Signdatasize) {
        this.Signdatasize = Signdatasize;
    }
    public int getSigndatasize() {
        return Signdatasize;
    }

    public void setSigndata(String signdata) {
        this.signdata = signdata;
    }
    public String getSigndata() {
        return signdata;
    }

}