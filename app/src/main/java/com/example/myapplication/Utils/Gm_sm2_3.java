package com.example.myapplication.Utils;

import java.io.Serializable;

/*
 *作者：zyc14588
 *github地址：https://github.com/zyc14588
 */public class Gm_sm2_3 implements Serializable {
    public native String GM_GenSM2keypair(byte [] pubkey_XY,byte [] prikey) ;
    public native String GM_SM2Sign(byte [] signedData,
                                 byte [] Src,  long SrcLen,
                                 char [] UserID, long lenUID,
                                 byte [] prikey);
    public native int GM_SM2VerifySig(byte [] signedData,
                                      byte [] Src, long SrcLen,
                                      char [] UserID, long lenUID,
                                      byte [] szPubkey_XY);
    public native int GM_SM2Encrypt(byte [] encData, long ulEncDataLen,
                                    byte [] plain, long plainLen,
                                    byte [] szPubkey_XY);
    public native int GM_SM2Decrypt(byte [] DecData, long  ulDecDataLen,
                                    byte [] input,long inlen,
                                    byte [] pri_sm);
    public native String sm3( byte [] input, long ilen,
                            byte[] output );
    public native int GetEncLen(byte [] Src, long SrcLen,byte [] szPubkey_XY);
    public native int GetDecLen(byte [] Src, long SrcLen,byte [] pri_sm);

    public final String test ="wergsdfgqeagasdfg";
}
