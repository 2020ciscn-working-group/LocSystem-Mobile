/**
 * Copyright 2020 bejson.com
 */
package com.example.myapplication.DateStract;
import java.util.List;

/**
 * Auto-generated: 2020-03-23 2:40:55
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Guest {

    private String uuid;
    private String id;
    private String info;
    private String desc;
    private List<RemoteKey> RemoteKey;
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getUuid() {
        return uuid;
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

    public void setRemoteKey(List<RemoteKey> RemoteKey) {
        this.RemoteKey = RemoteKey;
    }
    public List<RemoteKey> getRemoteKey() {
        return RemoteKey;
    }

}