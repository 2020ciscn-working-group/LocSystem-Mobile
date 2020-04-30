package com.example.myapplication.Activities.Models;

import android.app.Activity;
import android.graphics.Paint;
import android.hardware.usb.UsbDevice;

import com.example.myapplication.Activities.Models.Internet.SignUp;
import com.example.myapplication.Dao.Secret.Sql.AppSql;
import com.example.myapplication.DateStract.Cert;
import com.example.myapplication.DateStract.Guest;
import com.example.myapplication.DateStract.Hub;
import com.example.myapplication.DateStract.Loc;
import com.example.myapplication.DateStract.LocalKey;
import com.example.myapplication.DateStract.Owner;
import com.example.myapplication.DateStract.RemoteKey;
import com.example.myapplication.DateStract.RootReq;
import com.example.myapplication.Defin.Defin_crypto;
import com.example.myapplication.Utils.Gm_sm2_3;
import com.example.myapplication.Utils.UsbHelper;
import com.example.myapplication.errs.NoSuchGuestException;
import com.example.myapplication.errs.NoSuchKeyException;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private       UsbHelper         mUsbHelper;
    public Model_Crypto(Activity activity,SignUp signUp)  {
        mOwner=new Owner();
        mGuests= new LinkedList<>();
        mAppSql=new AppSql(activity);
        mUsbHelper=new UsbHelper(activity);
        OwnerInit(signUp);
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
    public boolean VerifyRemoteKey(@NotNull RemoteKey remoteKey, @NotNull Cert cert) {
        if(remoteKey.getPubkey()==cert.getPubkey())
            if(remoteKey.getInfo().equals(cert.getInfo()))
                if(remoteKey.getType()==cert.getType()){
                    Guest guest=null;
                    for(Guest guest1:mGuests){
                        if(guest1.getUuid().equals(cert.getuuid()))
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
                            expub=Defin_crypto.CApub;break;
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
                    int ret=gm_sm2_3.GM_SM2VerifySig(cert.getsigndata(),src,src.length,cert.getuuid().toCharArray(),cert.getuuid().toCharArray().length,expub);
                    byte[]src_cert=new byte[src.length+cert.getsigndata().length];
                    for(int i=0;i<src_cert.length;i++){
                        if(i<src.length)
                            src_cert[i]=src[i];
                        else
                            src_cert[i]=cert.getsigndata()[i-src.length];
                    }
                    int ret_cert=gm_sm2_3.GM_SM2VerifySig(cert.getcertdata(),src_cert,src_cert.length,cert.getuuid().toCharArray(),cert.getuuid().toCharArray().length,pub);
                    return ret == 0&&ret_cert==0;
                }
        return false;
    }
    @Nullable
    private LocalKey LocalKeyGen(int type){
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
            gm_sm2_3.GM_SM2Sign(signdata,src,src.length,mOwner.getUuid().toCharArray(),mOwner.getUuid().toCharArray().length,expri);
            localKey.setSigndata(signdata);
            byte[]src_cert=new byte[src.length+signdata.length];
            for(int i=0;i<src_cert.length;i++){
                if(i<src.length)
                    src_cert[i]=src[i];
                else
                    src_cert[i]=signdata[i-src.length];
            }
            byte[] certdata=new byte[32];
            gm_sm2_3.GM_SM2Sign(certdata,src_cert,src_cert.length,mOwner.getUuid().toCharArray(),mOwner.getUuid().toCharArray().length,pri);
            localKey.setCertdata(certdata);
            return localKey;
    }
    public Cert GenCert(@NotNull LocalKey localKey){
        Cert cert=new Cert();
        cert.setInfo(localKey.getInfo());
        cert.setPubkey(localKey.getPubkey());
        cert.setType(localKey.getType());
        cert.setuuid(mOwner.getUuid());
        cert.setsigndata(localKey.getSigndata());
        cert.setcertdata(localKey.getCertdata());
        cert.setAlgo_key("SM2");
        cert.setAlgo_hash("SM3");
        return cert;
    }
    private void RootKeyGen() {
        //生成rootkey的本地密钥，做成remotekey给板子，板子出证明+pik及pik证书
        //确保拿到的json字符串顺序为PIK/PIKCERT/ROOTKEYCERT
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                if(!mOwner.getLocalkey().isEmpty())
                    for(LocalKey localKey1:mOwner.getLocalkey())
                        if(localKey1.getType()==Defin_crypto.ROOT)
                            return;
                Gm_sm2_3 gm_sm2_3=Gm_sm2_3.getInstance();
                byte[]pub=new byte[64];
                byte[]pri=new byte[32];
                gm_sm2_3.GM_GenSM2keypair(pub,pri);
                LocalKey localKey=new LocalKey();
                localKey.setPrikey(pri);
                localKey.setPubkey(pub);
                localKey.setType(Defin_crypto.ROOT);
                localKey.setInfo(Defin_crypto.info);
                RootReq req=new RootReq();
                req.setPubkey(pub);
                req.setPrikey(pri);
                req.setType(Defin_crypto.ROOT);
                req.setInfo(Defin_crypto.info);
                req.setuseruuid(mOwner.getUuid());
                Gson gson=new Gson();
                String json_req=gson.toJson(req,RootReq.class);
                List<UsbDevice>list=mUsbHelper.getUsbDevice();
                if(list.size()==0)throw new NullPointerException();
                mUsbHelper.connection(list.get(0).getVendorId(),list.get(0).getProductId());
                mUsbHelper.sendData(json_req.getBytes());
                String json=mUsbHelper.readFromUsb();
                String[]jsons=json.split("\n");
                RemoteKey PIK = gson.fromJson(jsons[0], RemoteKey.class);
                Cert PIK_cert = gson.fromJson(jsons[1], Cert.class);
                Cert rootkey_cert = gson.fromJson(jsons[2], Cert.class);
                localKey.setSigndata(rootkey_cert.getsigndata());
                localKey.setCertdata(rootkey_cert.getcertdata());
                mOwner.getLocalkey().add(localKey);
                mOwner.setPIK(PIK);
                mOwner.setPIK_cert(PIK_cert);
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();
    }

    private void OwnerInit(@NotNull SignUp signUp){
        mOwner.setUuid(signUp.getSM3str());
        RootKeyGen();
        mOwner.addLocalKey(LocalKeyGen(Defin_crypto.SIGN));
        mOwner.addLocalKey(LocalKeyGen(Defin_crypto.BIND));
    }
    @Override
    public byte[] getSM3() {
        return new byte[0];
    }
}
