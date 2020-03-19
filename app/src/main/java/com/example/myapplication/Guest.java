package com.example.myapplication;

/*
 *作者：zyc14588
 *github地址：https://github.com/zyc14588
 */public class Guest {
     private String      uuid;
     private String    name;
     private String    pubkey;
     private String     info;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPubkey() {
        return pubkey;
    }

    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Guest(String uuid, String name, String pubkey) {
        this.name=name;
        this.uuid=uuid;
        this.pubkey=pubkey;
    }
}
