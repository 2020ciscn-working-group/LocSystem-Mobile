package com.example.myapplication.DateStract;

import java.util.List;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class LocalHub {
    private String    uuid;
    private String    uuid_ow;
    private String    id;
    private String    info;
    private String    desc;
    private List<Loc> locs;
    private List<RemoteKey> mRemoteKey;
    private List<Cert>mCerts;

    public List<RemoteKey> getRemoteKey() {
        return mRemoteKey;
    }

    public void setRemoteKey(List<RemoteKey> remoteKey) {
        mRemoteKey = remoteKey;
    }

    public List<Cert> getCerts() {
        return mCerts;
    }

    public void setCerts(List<Cert> certs) {
        mCerts = certs;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getUuid() {
        return uuid;
    }

    public void setUuid_ow(String uuid_ow) {
        this.uuid_ow = uuid_ow;
    }
    public String getUuid_ow() {
        return uuid_ow;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public String getInfo() {
        return info;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }

    public void setLocs(List<Loc> locs) {
        this.locs = locs;
    }
    public List<Loc> getLocs() {
        return locs;
    }
    public Hub genhub(){
        Hub hub=new Hub();
        hub.setDesc(desc);
        hub.setId(id);
        hub.setInfo(info);
        hub.setLocs(locs);
        hub.setUuid(uuid);
        hub.setUuid_ow(uuid_ow);
        return hub;
    }
}
