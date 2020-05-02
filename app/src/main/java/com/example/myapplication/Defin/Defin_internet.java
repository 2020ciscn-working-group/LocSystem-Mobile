package com.example.myapplication.Defin;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Defin_internet {
    public static final String SeverAddress   ="10.0.2.2";
    public static final String DebugSeverPort =":63342";
    public static final String AppServerPort  =":8080";
    public static final String AppServerLogin ="/login";
    public static final String AppServerSend ="/send";
    public static final String AppServerGetMsg ="/getMsg";
    public static final int    str            =100;
    public static final int    friendreq=101;
    public static final int    tocken       =200;
    public static final int    accreq       =180;
    public static final int    cert           =150;
    public static final int    remotekey      =151;
    public static final int    nego_req       =110;
    public static final int    nego_apl       =111;
    public static       String CApub          ="";
    public static void setCApub(String cApub){
        CApub=cApub;
    }
}
