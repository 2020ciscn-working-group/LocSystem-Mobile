/**
 * Copyright 2020 bejson.com
 */
package com.example.myapplication.DateStract;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Auto-generated: 2020-03-23 2:40:55
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Guest implements Serializable {

    private String          uuid;
    private String          id;
    private String          info;
    private String          desc;
    private List<RemoteKey> mRemoteKey;
    private List<Hub>       mHubs;
    private List<Audit>     mAudits;
    private List<Cert>      mCerts;
    private List<Tocken>    mTockens;
    private RemoteKey       catch_remotekey =null;
    private Cert            cath_remotecert =null;
    public Guest(){
        uuid=null;
        id=null;
        info=null;
        desc=null;
        mRemoteKey=new LinkedList<>();
        mHubs=new LinkedList<>();
        mAudits=new LinkedList<>();
        mCerts=new LinkedList<>();
        mTockens=new LinkedList<>();

    }
    public    List<Tocken> getTockens() {
        return mTockens;
    }

    public   void setTockens(List<Tocken> tockens) {
        mTockens = tockens;
    }

    public    RemoteKey getCatch_remotekey() {
        return catch_remotekey;
    }

    public   void setCatch_remotekey(RemoteKey catch_remotekey) {
        this.catch_remotekey = catch_remotekey;
    }

    public    Cert getCath_remotecert() {
        return cath_remotecert;
    }

    public   void setCath_remotecert(Cert cath_remotecert) {
        this.cath_remotecert = cath_remotecert;
    }

    public    List<Cert> getCerts() {
        return mCerts;
    }

    public   void setCerts(List<Cert> certs) {
        mCerts = certs;
    }

    public      List<Audit> getAudits() {
        return mAudits;
    }

    public   void setAudits(List<Audit> audits) {
        mAudits = audits;
    }

    public    List<Hub> getHubs() {
        return mHubs;
    }

    public     void setHubs(List<Hub> hubs) {
        mHubs = hubs;
    }

    public   void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public    String getUuid() {
        return uuid;
    }

    public   void setId(String id) {
        this.id = id;
    }
    public      String getId() {
        return id;
    }

    public   void setInfo(String info) {
        this.info = info;
    }
    public    String getInfo() {
        return info;
    }

    public   void setDesc(String desc) {
        this.desc = desc;
    }
    public    String getDesc() {
        return desc;
    }

    public   void setRemoteKey(List<RemoteKey> RemoteKey) {
        mRemoteKey = RemoteKey;
    }
    public    List<RemoteKey> getRemoteKey() {
        return mRemoteKey;
    }
    public   void addHub(Hub hub){
        mHubs.add(hub);
    }
    public   void addAudit(Audit audit){
        mAudits.add(audit);
    }
    public   void addCert(Cert cert){
        mCerts.add(cert);
    }
    public   void addTocken(Tocken tocken){
        mTockens.add(tocken);
    }
    public RemoteKey getremotekey(int def){
        for(RemoteKey remoteKey:this.mRemoteKey){
            if(remoteKey.getType()==def)
                return remoteKey;
        }
        return null;
    }
}