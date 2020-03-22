/**
 * Copyright 2020 bejson.com
 */
package com.example.myapplication;
import com.example.myapplication.DateStract.RemoteKey;

import java.util.List;

/**
 * Auto-generated: 2020-03-23 2:59:18
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class hub {

    private String                                               uuid;
    private String                                               uuid_ow;
    private String                                               id;
    private String                                               info;
    private String                                               desc;
    private List<Integer>                                        locs;
    private List<com.example.myapplication.DateStract.RemoteKey> RemoteKey;
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

    public void setLocs(List<Integer> locs) {
        this.locs = locs;
    }
    public List<Integer> getLocs() {
        return locs;
    }

    public void setRemoteKey(List<RemoteKey> RemoteKey) {
        this.RemoteKey = RemoteKey;
    }
    public List<RemoteKey> getRemoteKey() {
        return RemoteKey;
    }

}