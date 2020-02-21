package com.example.myapplication;

import java.security.Key;
import java.security.PublicKey;
import java.util.UUID;

/*
 *作者：zyc14588
 *github地址：https://github.com/zyc14588
 */public class Guest {
     private String      uuid;
     private String    name;
     private String    pubkey;

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getPubkey() {
        return pubkey;
    }

    public Guest(String uuid,String name,String pubkey) {
        this.name=name;
        this.uuid=uuid;
        this.pubkey=pubkey;
    }
}
