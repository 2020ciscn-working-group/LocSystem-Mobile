package com.example.myapplication;

import java.util.ArrayList;
import java.util.LinkedList;

/*
 *作者：zyc14588
 *github地址：https://github.com/zyc14588
 */public class owner_date extends Thread
{
    private LinkedList<hub> hublist;
    private hub[]           achubs;
    private LinkedList<tocken>  tockens;
    private String                uuid;
    private String              name;
    private LinkedList<Guest>   guests;
    private LinkedList<Aduit>   aduits;
    private String    json;
    private int       type =0;
    private tcpsocket tcp;
    private String    info;
    private String    other;


    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setname(String name) {
        this.name = name;
    }


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public void setType(int type) {
        this.type = type;
    }
    public void setJson(String json) {
        this.json = json;
    }

    public void run(){
        while (true){
            if(tcp.isFlash()){
                tcp.getMessage(this);
            }

        }
    }
    public owner_date(){}
    public owner_date(tcpsocket tt){
        tcp=tt;
    }
    public hub[] getAchubs() {
        return achubs;
    }

    public LinkedList<tocken> getTockens() {
        return tockens;
    }

    public String getUuid() {
        return uuid;
    }

    public String getname() {
        return name;
    }
    public Guest getGuest(String uuid){
        for (Guest ss:guests) {
            if(ss.getUuid().contains(uuid))return ss;
        }
        return null;
    }
    public LinkedList<Guest> getGuests() {
        return guests;
    }
    public Aduit getAduit(String tockenuuid){
        for (Aduit ss:aduits) {
            if(ss.getToken_uuid().contains(tockenuuid))return ss;
        }
        return null;
    }
    public Aduit[] getAudit(String gestuuid){
        ArrayList<Aduit> at=new ArrayList<Aduit>();
        for(Aduit ss:aduits){
            if(ss.getGuest_uuid().contains(gestuuid))at.add(ss);
        }
        Aduit[] ads=new Aduit[at.size()];
        for(int s=0;s<at.size();s++){
            ads[s]=at.get(s);
        }
        return ads;
    }
    public LinkedList<Aduit> getAduots() {
        return aduits;
    }
    public void addTocken(tocken tk){
        if(tk.getGuest_uuid().contains(uuid.toString()))
            tockens.add(tk);
    }
    public void deleteTocken(String uuid){
        for(tocken tt:tockens){
           if(tt.getToken_uuid().contains(uuid))
               tockens.remove(tt);
        }
    }
    public void deleteTocken(tocken tt){
            tockens.remove(tt);
    }
    public  void addAduit(Aduit ad){
        aduits.add(ad);
    }
    public void addHub(hub[] h){
    //目前不需要做
    }
    public void sendTocken(tocken t){
        tcp.sendObject(t,1);
    }
}
