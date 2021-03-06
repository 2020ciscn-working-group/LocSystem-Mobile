/**
 * Copyright 2020 bejson.com
 */
package com.example.myapplication.DateStract;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Auto-generated: 2020-04-01 3:52:10
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Owner implements Serializable {

    private String               uuid=null;
    private String               info=null;
    private LinkedList<LocalKey> Localkey;
    private LinkedList<LocalHub> mLocalHubs;
    private LinkedList<Guest>    Guests;
    private LinkedList<Audit>    Audits;
    private RemoteKey PIK;
    private Cert PIK_cert;

    public List<Guest> getGuests() {
        return Guests;
    }

    public List<Audit> getAudits() {
        return Audits;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getUuid() {
        return uuid;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public String getInfo() {
        return info;
    }
    public List<LocalKey> getLocalkey() {
        return Localkey;
    }
    public List<LocalHub> getLocalHub() {
        return mLocalHubs;
    }

    public RemoteKey getPIK() {
        return PIK;
    }

    public void setPIK(RemoteKey PIK) {
        this.PIK = PIK;
    }

    public Cert getPIK_cert() {
        return PIK_cert;
    }

    public void setPIK_cert(Cert PIK_cert) {
        this.PIK_cert = PIK_cert;
    }

    public Owner(){
        Localkey=new LinkedList<>();
        mLocalHubs=new LinkedList<>();
        Guests=new LinkedList<>();
        Audits=new LinkedList<>();
    }
    public void addGuest(Guest guest){
        Guests.add(guest);
    }
    public void addLocalHub(LocalHub LocalHub){
        mLocalHubs.add(LocalHub);
    }
    public void addLocalKey(LocalKey localKey){
        Localkey.add(localKey);
    }
    public LocalKey getlocalkey(int def){
        for(LocalKey localKey:this.Localkey){
            if(localKey.getType()==def)
                return localKey;
        }
        return null;
    }
}
