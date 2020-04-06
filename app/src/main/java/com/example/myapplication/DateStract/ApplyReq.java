package com.example.myapplication.DateStract;

import com.example.myapplication.Utils.Gm_sm2_3;

import java.io.Serializable;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class ApplyReq implements Serializable {//授权申请包
    private String info;//个人信息
    private String version;//版本
    private byte[] pik_pub;//pik公钥
    private byte[] ca_pub;//CA公钥
    private byte[] req;//申请实例

    public ApplyReq(String info,String version,byte[]pik_pub,byte[]ca_pub){
        if(info==null||version==null||pik_pub==null||ca_pub==null)
            throw new NullPointerException();
        this.ca_pub=ca_pub;
        this.info=info;
        this.version=version;
        this.pik_pub=pik_pub;
    }

    public String getInfo() {
        return info;
    }


    public String getVersion() {
        return version;
    }


    public byte[] getPik_pub() {
        return pik_pub;
    }


    public byte[] getCa_pub() {
        return ca_pub;
    }

    public byte[] getReq() {
        return req;
    }

    public byte[] makereq(){
        Gm_sm2_3 gm=Gm_sm2_3.getInstance();
        byte[] buf_src=(info).getBytes();
        byte[]buf=new byte[buf_src.length+pik_pub.length];
        for(int i=0;i<buf.length;i++){
            if(i<pik_pub.length){
                buf[i]=pik_pub[i];
            }
            else buf[i]=buf_src[i-pik_pub.length];
        }
        byte[]ret_hash=new byte[32];
        gm.sm3(buf,buf.length,ret_hash);
        byte[]byte_ver=(info).getBytes();
        byte[] buf2=new byte[ret_hash.length+pik_pub.length+byte_ver.length];
        for(int i=0;i<buf.length;i++){
            if(i<ret_hash.length){
                buf2[i]=ret_hash[i];
            }
            else if(i<ret_hash.length+byte_ver.length)
                buf2[i]=byte_ver[i-ret_hash.length];
            else
                buf2[i]=pik_pub[i-ret_hash.length-byte_ver.length];
            byte[] req_exp = new byte[32];
            gm.sm3(buf2,buf2.length, req_exp);
            int len=gm.GetEncLen(req_exp, req_exp.length,ca_pub);
            req=new byte[len];
            gm.GM_SM2Encrypt(req,len, req_exp, req_exp.length,ca_pub);
        }
        return req;
    }

}
