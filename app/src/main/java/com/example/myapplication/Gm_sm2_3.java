package com.example.myapplication;

/*
 *作者：zyc14588
 *github地址：https://github.com/zyc14588
 */public class Gm_sm2_3 {
    public native int GM_GenSM2keypair(byte [] pubkey_XY,char []pri_sm3) ;
    public native int GM_SM2Sign(char [] signedData,
                                 byte [] Src,  long SrcLen,
                                 char [] UserID, long lenUID,
                                 char [] pri_sm3);
    public native int GM_SM2VerifySig(char [] signedData,long ulSigLen,
                                      byte [] Src, long SrcLen,
                                      char [] UserID, long lenUID,
                                      char [] szPubkey_XY,long ulPubkey_XYLen);
    public native int KDFwithSm3(char [] kdfOutBuff, char [] Z_in, long ulZlen, long klen );
    public native int GM_SM2Encrypt(char [] encData, long ulEncDataLen,
                                    char [] plain, long plainLen,
                                    char [] szPubkey_XY, long ul_PubkXY_len);
    public native int GM_SM2Decrypt(char [] DecData, long  ulDecDataLen,
                                    char [] input,long inlen,
                                    char [] pri_sm);
    public native int BYTE_POINT_is_on_sm2_curve(char [] pubkey_XY, long ulPubXYLen);
    public native int BYTE_Point_mul(char k[],char newPoint[]);
    public native String getVersion();
    public native void sm3( byte [] input, long ilen,
                            byte[] output );

}
