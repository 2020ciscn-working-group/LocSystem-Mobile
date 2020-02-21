package com.example.myapplication;

import com.google.gson.Gson;

/*
 *作者：zyc14588
 *github地址：https://github.com/zyc14588
 */public class jsontrans {
     private Gson gson;
     public jsontrans(){
         gson=new Gson();
     }
     public hub[] trans_hublist_from_json(String hub_json){
         hub[] hbs=gson.fromJson(hub_json,hub[].class);
         return hbs;
     }
     public String trans_hublist_to_json(hub[] hbs){
         String json=gson.toJson(hbs);
         return json;
     }
    public tocken trans_tocken_from_json(String tocken_json){
        tocken tk=gson.fromJson(tocken_json,tocken.class);
        return tk;
    }
    public String trans_tocken_to_json(tocken tk){
        String json=gson.toJson(tk);
        return json;
    }

}
