package com.example.myapplication.Defin;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Defin_internet {
    public static final String SeverAddress   ="http://10.0.2.2";
    public static final String DebugSeverPort =":63342";
    public static final String AppServerPort  =":8080";
    public static final String AppServerLogin ="/login";
    public static final String AppServerSignin ="/signin";
    public static final String AppServerFindFriend="/findfriend";
    public static final String AppServerSend ="/send";
    public static final String AppServerGetMsg ="/getMsg";
    public static final String AppServerQueryFriend="/queryFriend";
    public static final int    str            =100;
    public static final int    friendreq=101;
    public static final int    friendapl =102;
    public static final int    remotehub=112;
    public static final int    tocken       =200;
    public static final int    accreq       =180;
    public static final int    cert           =150;
    public static final int    remotekey      =151;
    public static final int    nego_req       =110;
    public static final int    nego_apl       =111;
    public static       String CApub          ="3cca45608914bd0b63e6c0b22116b632ccc555085037a9648e7bb428b2a90aa16a4107330c9f4cfe650c29c410112d7637be458f8cd93245ed97810c4a3ae5d1";
    public static void setCApub(String cApub){
        CApub=cApub;
    }
}
