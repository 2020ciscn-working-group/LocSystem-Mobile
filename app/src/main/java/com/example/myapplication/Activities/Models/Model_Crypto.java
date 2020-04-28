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
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Model_Crypto extends Model_Basic implements Serializable {
    private       Owner             mOwner;
    private       LinkedList<Guest> mGuests;
    private       AppSql            mAppSql;
    private final byte[]            CApub;
    private RemoteKey PIK,RootKey;
    private Cert   PIK_cert, Rootkey_cert;

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
                    byte[]src_cert=new byte[src.length+cert.getsigndata().length];
                    for(int i=0;i<src_cert.length;i++){
                        if(i<src.length)
                            src_cert[i]=src[i];
                        else
                            src_cert[i]=cert.getsigndata()[i-src.length];
                    }
                    int ret_cert=gm_sm2_3.GM_SM2VerifySig(cert.getcertdata(),src_cert,src_cert.length,cert.getUserId().toCharArray(),cert.getUserId().toCharArray().length,expub);
                    return ret == 0&&ret_cert==0;
                }
        return false;
    }
    public LocalKey LocalKeyGen(int type){
        if(type==Defin_crypto.ROOT)
            return null;
        LocalKey localKey=null;
        Gm_sm2_3 gm_sm2_3=Gm_sm2_3.getInstance();
        localKey=new LocalKey();
        byte[]pub=new byte[64];
        byte[]pri=new byte[32];
        gm_sm2_3.GM_GenSM2keypair(pub,pri);
        localKey=new LocalKey();
        localKey.setPrikey(pri);
        localKey.setPubkey(pub);
        localKey.setType(type);
        localKey.setInfo(Defin_crypto.info);
        byte[]src_info=(localKey.getInfo()+localKey.getType()).getBytes();
        byte[]src=new byte[localKey.getPubkey().length+src_info.length];
        for(int i=0;i<pub.length+src_info.length;i++){
            if(i<pub.length)
                src[i]=pub[i];
            else src[i]=src_info[i-pub.length];
        }
        byte[]expri=null;
        for(LocalKey localKey1:mOwner.getLocalkey())
            if(localKey1.getType()==Defin_crypto.ROOT)
                expri=localKey1.getPrikey();
            byte[]signdata=new byte[32];
            gm_sm2_3.GM_SM2Sign(signdata,src,src.length,mOwner.getuserid().toCharArray(),mOwner.getuserid().toCharArray().length,pri);
            localKey.setSigndata(signdata);
            byte[]src_cert=new byte[src.length+signdata.length];
            for(int i=0;i<src_cert.length;i++){
                if(i<src.length)
                    src_cert[i]=src[i];
                else
                    src_cert[i]=signdata[i-src.length];
            }
            byte[] certdata=new byte[32];
            gm_sm2_3.GM_SM2Sign(certdata,src_cert,src_cert.length,mOwner.getuserid().toCharArray(),mOwner.getuserid().toCharArray().length,pri);
            localKey.setCertdata(certdata);
            return localKey;
    }
    public Cert GenCert(LocalKey localKey){
        Cert cert=new Cert();
        cert.setInfo(localKey.getInfo());
        cert.setPubkey(localKey.getPubkey());
        cert.setType(localKey.getType());
        cert.setUserId(mOwner.getuserid());
        cert.setsigndata(localKey.getSigndata());
        cert.setcertdata(localKey.getCertdata());
        cert.setAlgo_key("SM2");
        cert.setAlgo_hash("SM3");
        return cert;
    }
    public void RootKeyGen() {
        String[] jsons=null;//确保拿到的json字符串顺序为PIK/PIKCERT/ROOTKEY/ROOTKEYCERT
        if(!mOwner.getLocalkey().isEmpty())
            for(LocalKey localKey:mOwner.getLocalkey())
                if(localKey.getType()==Defin_crypto.ROOT)
                    return;
        //TODO:在这里调用usb与树莓派连接获取rootkey与其密钥证明和pik及其密钥证明

        Gson gson=new Gson();
        assert jsons != null;
        PIK=gson.fromJson(jsons[0],RemoteKey.class);
        PIK_cert=gson.fromJson(jsons[1],Cert.class);
        RootKey=gson.fromJson(jsons[2],RemoteKey.class);
        Rootkey_cert=gson.fromJson(jsons[3],Cert.class);
    }

    @Override
    public byte[] getSM3() {
        return new byte[0];
    }
}
