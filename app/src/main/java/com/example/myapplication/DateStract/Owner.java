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

    private String               uuid;
    private String               info;
    private String               userid;
    private LinkedList<LocalKey> Localkey;
    private LinkedList<Hub>      Hub;
    private LinkedList<Guest>    Guests;
    private LinkedList<Tocken>   Tockens;
    private LinkedList<Audit>    Audits;

    public List<Guest> getGuests() {
        return Guests;
    }

    public List<Tocken> getTockens() {
        return Tockens;
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

    public void setuserid(String userid) {
        this.userid = userid;
    }
    public String getuserid() {
        return userid;
    }
    public List<LocalKey> getLocalkey() {
        return Localkey;
    }
    public List<Hub> getHub() {
        return Hub;
    }
    private Owner(){}
    public Owner(String uuid,String info,String userid){
        this.uuid=uuid;
        this.info=info;
        this.userid=userid;
        Localkey=new LinkedList<>();
        Hub=new LinkedList<>();
        Guests=new LinkedList<>();
        Tockens=new LinkedList<>();
        Audits=new LinkedList<>();
    }
    public void addGuest(Guest guest){
        Guests.add(guest);
    }
    public void addHub(Hub hub){
        Hub.add(hub);
    }
    public void addTocken(Tocken tocken){
        Tockens.add(tocken);
    }
    public void addLocalKey(LocalKey localKey){
        Localkey.add(localKey);
    }

}
