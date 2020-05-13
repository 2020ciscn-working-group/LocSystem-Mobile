/**
  * Copyright 2019 bejson.com 
  */
package com.example.myapplication.DateStract;

import java.io.Serializable;

/**
 * Auto-generated: 2019-08-25 22:38:35
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Loc implements Serializable {

    private String Hub_uuid;
    private int Lock_id;
    private String desc;
    private int acctype;
    public void setHub_uuid(String Hub_uuid) {
         this.Hub_uuid = Hub_uuid;
     }
     public String getHub_uuid() {
         return Hub_uuid;
     }

    public void setLock_id(int Lock_id) {
         this.Lock_id = Lock_id;
     }
     public int getLock_id() {
         return Lock_id;
     }

    public int getAcctype() {
        return acctype;
    }

    public void setAcctype(int acctype) {
        this.acctype = acctype;
    }

    public void setDesc(String Lock_name) {
         desc = Lock_name;
     }
     public String getDesc() {
         return desc;
     }

}