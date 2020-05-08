/**
 * Copyright 2020 bejson.com
 */
package com.example.myapplication.DateStract;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2020-03-23 2:59:18
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Hub implements Serializable {

    private String                                               uuid;
    private String                                               uuid_ow;
    private String                                               id;
    private String                                               info;
    private String                                               desc;
    private List<Loc>                                            locs;
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
    public String toJson(){
        Gson gson=new Gson();
        return gson.toJson(this,Hub.class);
    }

}