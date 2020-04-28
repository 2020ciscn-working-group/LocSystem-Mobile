package com.example.myapplication.Activities.controls;

import com.example.myapplication.DateStract.Audit;
import com.example.myapplication.DateStract.Guest;
import com.example.myapplication.DateStract.Owner;
import com.example.myapplication.DateStract.Tocken;

import java.util.LinkedList;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class CryptologyControl {
    private volatile LinkedList<Guest>  mGuestList;
    private volatile LinkedList<Audit>  mAuditList;
    private volatile LinkedList<Tocken> mTockenList;
    private volatile Owner mOwner;
    private static volatile CryptologyControl  sCryptologyControl;
    private CryptologyControl(){
        mAuditList= new LinkedList<Audit>();
        mGuestList=new LinkedList<Guest>();
        mTockenList=new LinkedList<Tocken>();
        //mOwner=new Owner();
    }
    public static CryptologyControl getInstance(){
        if(sCryptologyControl==null){
            synchronized (CryptologyControl.class){
                if(sCryptologyControl==null)
                    sCryptologyControl=new CryptologyControl();
            }

        }
        return sCryptologyControl;
    }

    public LinkedList<Guest> getGuestList() {
        return mGuestList;
    }

    public LinkedList<Audit> getAuditList() {
        return mAuditList;
    }

    public LinkedList<Tocken> getTockenList() {
        return mTockenList;
    }

    public Owner getOwner() {
        return mOwner;
    }



}
