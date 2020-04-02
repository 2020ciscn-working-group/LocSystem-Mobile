package com.example.myapplication;

import com.example.myapplication.DateStract.Audit;
import com.example.myapplication.DateStract.Guest;
import com.example.myapplication.DateStract.Hub;
import com.example.myapplication.DateStract.Tocken;

import java.util.ArrayList;
import java.util.LinkedList;

/*
 *作者：zyc14588
 *github地址：https://github.com/zyc14588
 */public class owner_date
{
    private LinkedList<Hub>    hublist;
    private Hub[]              achubs;
    private LinkedList<Tocken> tockens;
    private String             uuid;
    private String             name;
    private LinkedList<Guest>  guests;
    private LinkedList<Audit>  aduits;
    private String             json;
    private int                type =0;
    private tcpsocket          tcp;
    private String             info;
    private String             other;


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
    public Hub[] getAchubs() {
        return achubs;
    }

    public LinkedList<Tocken> getTockens() {
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
            if(new String(ss.getUuid()).contains(uuid))return ss;
        }
        return null;
    }
    public LinkedList<Guest> getGuests() {
        return guests;
    }
    public Audit getAduit(String tockenuuid){
        for (Audit ss:aduits) {
            if(ss.getAuditexp().getUuid_Tocken().contains(tockenuuid))return ss;
        }
        return null;
    }
    public Audit[] getAudit(String gestuuid){
        ArrayList<Audit> at=new ArrayList<Audit>();
        for(Audit ss:aduits){
            if(ss.getAuditexp().getUuid_Tocken().contains(gestuuid))at.add(ss);
        }
        Audit[] ads=new Audit[at.size()];
        for(int s=0;s<at.size();s++){
            ads[s]=at.get(s);
        }
        return ads;
    }
    public LinkedList<Audit> getAduots() {
        return aduits;
    }
    public void addTocken(Tocken tk){
        if(new String(tk.getUuid()).contains(uuid.toString()))
            tockens.add(tk);
    }
    public void deleteTocken(String uuid){
        for(Tocken tt:tockens){
           if(new String(tt.getUuid()).contains(uuid))
               tockens.remove(tt);
        }
    }
    public void deleteTocken(Tocken tt){
            tockens.remove(tt);
    }
    public  void addAduit(Audit ad){
        aduits.add(ad);
    }
    public void addHub(Hub[] h){
    //目前不需要做
    }
    public void sendTocken(Tocken t){
        tcp.sendObject(t,1);
    }
}
