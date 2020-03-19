package com.example.myapplication;

import com.google.gson.Gson;

/*
 *作者：zyc14588
 *github地址：https://github.com/zyc14588
 */public class jsontrans {
     private static Gson gson;
     static {
         gson=new Gson();
     }
     private jsontrans(){
     }
     public static hub[] trans_hublist_from_json(String hub_json){
         return gson.fromJson(hub_json,hub[].class);
     }
     public static String trans_hublist_to_json(hub[] hbs){
         return gson.toJson(hbs);
     }
    public static tocken trans_tocken_from_json(String tocken_json){
        return gson.fromJson(tocken_json,tocken.class);
    }
    public static String trans_tocken_to_json(tocken tk){
        return gson.toJson(tk);
    }

}
