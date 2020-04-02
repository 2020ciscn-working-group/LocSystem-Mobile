/**
 * Copyright 2020 bejson.com
 */
package com.example.myapplication.DateStract;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2020-04-01 3:52:10
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Owner implements Serializable {

    private String uuid;
    private String info;
    private String desc;
    private List<LocalKey> Localkey;
    private List<Hub> Hub;
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

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }

    public void setLocalkey(List<LocalKey> Localkey) {
        this.Localkey = Localkey;
    }
    public List<LocalKey> getLocalkey() {
        return Localkey;
    }

    public void setHub(List<Hub> Hub) {
        this.Hub = Hub;
    }
    public List<Hub> getHub() {
        return Hub;
    }

}
