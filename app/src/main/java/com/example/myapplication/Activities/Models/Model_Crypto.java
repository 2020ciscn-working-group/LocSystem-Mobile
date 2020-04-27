package com.example.myapplication.Activities.Models;

import android.app.Activity;
import android.graphics.Paint;

import com.example.myapplication.Dao.Secret.Sql.AppSql;
import com.example.myapplication.DateStract.Cert;
import com.example.myapplication.DateStract.Guest;
import com.example.myapplication.DateStract.Hub;
import com.example.myapplication.DateStract.LocalKey;
import com.example.myapplication.DateStract.Owner;
import com.example.myapplication.DateStract.RemoteKey;
import com.example.myapplication.Defin.Defin_crypto;
import com.example.myapplication.Utils.Gm_sm2_3;
import com.example.myapplication.errs.NoSuchGuestException;
import com.example.myapplication.errs.NoSuchKeyException;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Model_Crypto extends Model_Basic implements Serializable {
    private       Owner             mOwner;
    private       LinkedList<Guest> mGuests;
    private       AppSql            mAppSql;
    private final byte[]            CApub;

    Model_Crypto(Activity activity, String uuid, String info, String desc, byte[] cApub)  {
        CApub = cApub;
        mOwner=new Owner(uuid, info, desc);
        mGuests= new LinkedList<>();
        mAppSql=new AppSql(activity);
    }
    public void addOwHub(Hub hub){
        mOwner.addHub(hub);
    }
    public void addGuestHub(Hub hub,String uuid){
        List<Guest>guests=mOwner.getGuests();
        for(Guest guest:guests){
            if(guest.getUuid().equals(uuid))
                guest.getHubs().add(hub);
        }
    }
    public void addRemoteKey(RemoteKey remoteKey,String uuid){
        List<Guest>guests=mOwner.getGuests();
        for(Guest guest:guests){
            if(guest.getUuid().equals(uuid))
                guest.getRemoteKey().add(remoteKey);
        }
    }
    public void addGuset(Guest guest){
        mGuests.add(guest);
    }
    public void addLocalKey(LocalKey localKey){
        mOwner.addLocalKey(localKey);
    }
    public boolean VerifyRemoteKey(RemoteKey remoteKey, Cert cert,String Gustuuid) {
        if(remoteKey.getPubkey()==cert.getPubkey())
            if(remoteKey.getInfo().equals(cert.getInfo()))
                if(remoteKey.getType()==cert.getType()){
                    Guest guest=null;
                    for(Guest guest1:mGuests){
                        if(guest1.getUuid().equals(Gustuuid))
                            guest=guest1;
                        else
                            return false;
                    }
                    byte[] src_info=(remoteKey.getInfo()+remoteKey.getType()).getBytes();
                    byte[]src=new byte[remoteKey.getPubkey().length+src_info.length];
                    byte[]pub=remoteKey.getPubkey();
                    for(int i=0;i<pub.length+src_info.length;i++){
                        if(i<pub.length)
                            src[i]=pub[i];
                        else src[i]=src_info[i-pub.length];
                    }
                    Gm_sm2_3 gm_sm2_3=Gm_sm2_3.getInstance();
                    byte[] expub=null;
                    switch (cert.getType()){
                        case Defin_crypto.PIK:{
                            expub=CApub;break;
                        }
                        case Defin_crypto.ROOT:{
                            assert guest != null;
                            for(RemoteKey remoteKey1:guest.getRemoteKey()){
                                    if(remoteKey1.getType()==Defin_crypto.PIK)
                                        expub=remoteKey1.getPubkey();
                                }
                            break;
                        }
                        case Defin_crypto.SIGN:
                        case Defin_crypto.BIND: {
                            assert guest != null;
                            for(RemoteKey remoteKey1:guest.getRemoteKey()){
                                    if(remoteKey1.getType()==Defin_crypto.ROOT)
                                        expub=remoteKey1.getPubkey();
                            }break;
                        }
                        default:{
                            return false;
                        }
                    }
                    int ret=gm_sm2_3.GM_SM2VerifySig(cert.getsigndata(),src,src.length,cert.getUserId().toCharArray(),cert.getUserId().toCharArray().length,expub);
                    return ret == 0;
                }
        return false;
    }


    @Override
    public byte[] getSM3() {
        return new byte[0];
    }
}
