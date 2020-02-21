/**
  * Copyright 2019 bejson.com 
  */
package com.example.myapplication;
import java.util.List;

/**
 * Auto-generated: 2019-08-25 22:38:35
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class hub {

    private String Hub_uuid;
    private String Hub_name;
    private String Owner_uuid;
    private int Lock_num;
    private List<Key> key;
    private List<Loc> loc;
    public void setHub_uuid(String Hub_uuid) {
         this.Hub_uuid = Hub_uuid;
     }
     public String getHub_uuid() {
         return Hub_uuid;
     }

    public void setHub_name(String Hub_name) {
         this.Hub_name = Hub_name;
     }
     public String getHub_name() {
         return Hub_name;
     }

    public void setOwner_uuid(String Owner_uuid) {
         this.Owner_uuid = Owner_uuid;
     }
     public String getOwner_uuid() {
         return Owner_uuid;
     }

    public void setLock_num(int Lock_num) {
         this.Lock_num = Lock_num;
     }
     public int getLock_num() {
         return Lock_num;
     }

    public void setKey(List<Key> key) {
         this.key = key;
     }
     public List<Key> getKey() {
         return key;
     }

    public void setLoc(List<Loc> loc) {
         this.loc = loc;
     }
     public List<Loc> getLoc() {
         return loc;
     }

}