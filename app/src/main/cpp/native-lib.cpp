#include <jni.h>
#include <string>
#include <android/log.h>
#include <istream>
#include <unistd.h>
#include "stdio.h"
#include "stdlib.h"
#include "sm2_impl/sm3.h"
#include "sm2_impl/sm2.h"
#include "include/data_type.h"
#include "include/alloc.h"
#include "include/memfunc.h"
#include "include/crypto_func.h"

#include <sys/types.h>
#include <sys/stat.h>
#include <dirent.h>
#include <asm/fcntl.h>
#include <fstream>
#include <fcntl.h>

#define LOG_TAG "System.out"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

//字节流转换为十六进制字符串
void ByteToHexStr(const unsigned char* source, char* dest, int sourceLen)
{
    short i;
    unsigned char highByte, lowByte;

    for (i = 0; i < sourceLen; i++)
    {
        highByte = source[i] >> 4;
        lowByte = source[i] & (unsigned char)0x0f ;

        highByte += 0x30;

        if (highByte > 0x39)
            dest[i * 2] = highByte + (unsigned char)0x07;
        else
            dest[i * 2] = highByte;

        lowByte += 0x30;
        if (lowByte > 0x39)
            dest[i * 2 + 1] = lowByte +(unsigned char) 0x07;
        else
            dest[i * 2 + 1] = lowByte;
    }
    return ;
}

//字节流转换为十六进制字符串的另一种实现方式
void Hex2Str( const char *sSrc,  char *sDest, int nSrcLen )
{
    int  i;
    char szTmp[3];

    for( i = 0; i < nSrcLen; i++ )
    {
        sprintf( szTmp, "%02X", (unsigned char) sSrc[i] );
        memcpy( &sDest[i * 2], szTmp, 2 );
    }
    return ;
}

//十六进制字符串转换为字节流
void HexStrToByte(const char* source, unsigned char* dest, int sourceLen)
{
    short i;
    unsigned char highByte, lowByte;

    for (i = 0; i < sourceLen; i += 2)
    {
        highByte = (unsigned char)toupper(source[i]);
        lowByte  = (unsigned char)toupper(source[i + 1]);

        if (highByte > 0x39)
            highByte -= 0x37;
        else
            highByte -= 0x30;

        if (lowByte > 0x39)
            lowByte -= 0x37;
        else
            lowByte -= 0x30;

        dest[i / 2] = (highByte << 4) | lowByte;
    }
    return ;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_myapplication_Activities_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_myapplication_Utils_Gm_1sm2_13_GM_1GenSM2keypair(JNIEnv *env, jobject thiz,
                                                            jbyteArray prikey, jbyteArray pri_re) {
    // TODO: implement GM_GenSM2keypair()
    unsigned char*pri;
    unsigned char pub[65];
    unsigned long len=200;
    unsigned char pri_sm3[32];
    char* pri_sm3_ret;
    //char*path_pri;
    //char*path_pub;
    //FILE*pubf;
    //FILE* prif;
    int ret=0;
    //執行SM2密钥生成
    pri=(unsigned char *)calloc(200,sizeof(unsigned char));
    ret=GM_GenSM2keypair(pri,&len,pub);
    if(ret!=0){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                         "Printf3.fprint:format is null");
        return NULL;
    }
    int i;
    LOGD("pub");
    for (i = 0; i<64; i++)
    {
        if (i % 4 ==0)
        {
            printf(" ");
        }
        LOGD("%02X", pub[i]);
    }
    LOGD("pri");
    for (i = 0; i<len; i++)
    {
        if (i % 4 ==0)
		{
			printf(" ");
		}
        LOGD("%02X", pri[i]);
    }
    printf("\n");
    //将生成的公钥返回java层

    env->SetByteArrayRegion(prikey,0,64,(jbyte*)pub);

    sm3(pri,len,pri_sm3);
    env->SetByteArrayRegion(pri_re,0,(jsize)len,(jbyte*)pri);
    pri_sm3_ret=(char*)calloc(len*2+1,sizeof(char));
    ByteToHexStr(pri_sm3,pri_sm3_ret,(int)len);
    return env->NewStringUTF(pri_sm3_ret);
    //以SM3结果为索引存储私钥
    /*const char *loc="/sdcard/Android/data/com.example.myapplication/pri.key";
    int stlen=strlen(loc);
    path_pri=(char*)calloc((size_t)stlen+32+1, sizeof(unsigned char));

    for (i = 0; i<(stlen+32); i++)
    {
        if(i<stlen){
            path_pri[i]=loc[i];
        } else{
            path_pri[i]=(char)pri_sm3[i-stlen];
        }
    }
    path_pri[stlen+32]='\0';

    prif=fopen(loc,"rb+");
    if (NULL!=prif) {
        (env)->ThrowNew((env)->FindClass("java/lang/NullPointerException"),
                        "SM2 key GEN Error");
        return 1;
    } else {
        prif = fopen(loc,"wb+");
        fwrite(&len,sizeof(int),1,prif);
        fwrite(pri,sizeof(unsigned char),len,prif);
    }
    //以SM3结果为索引存储公钥
    sm3(pub,64,pub_sm3);
    if((pubf=fopen((char*)pub_sm3,"rb"))==NULL){
        if((pubf=fopen((char*)pub_sm3,"wb"))==NULL){
            (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                            "SM2 key GEN Error");
            return 1;
        }
        fwrite(&len,1,sizeof(unsigned long),pubf);
        fwrite(pri,(int)len, sizeof(unsigned long),pubf);
    } else{
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "SM2 key GEN Error");
        return 1;
    }
    fclose(pubf);
    fclose(prif);


    return 0;*/
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_myapplication_Utils_Gm_1sm2_13_GM_1SM2Sign(JNIEnv *env, jobject thiz,
                                                      jbyteArray signed_data,
                                                      jbyteArray src, jlong src_len,
                                                      jcharArray uid, jlong len_uid,
                                                      jbyteArray pri) {
    // TODO: implement GM_SM2Sign()
    unsigned char prikey[32];
    unsigned char *uid_name;
    unsigned char sigd[64];
    unsigned char *src_in;
    unsigned  long len=32;
    unsigned long len_sig;
    char *sigd_hex;
    jchar*buf;
    //FILE* pri;
    jbyte * src_by;
    //签名对象转换成字节
    src_by=env->GetByteArrayElements(src,0);
    src_in=(unsigned char *)calloc((size_t)src_len,sizeof(unsigned char));
    memcpy(src_in,src_by,(size_t)src_len);
    env->ReleaseByteArrayElements(src,src_by,0);
    //转换私钥
    src_by=env->GetByteArrayElements(pri,0);
    memcpy(prikey,src_by,32);
    env->ReleaseByteArrayElements(pri,src_by,0);
    //打开并加载私钥
    /*if((pri=fopen(pri_name,"rb"))==NULL){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "SM2 Prikey Not Found");
        return 1;
    };
    fread(&len,sizeof(unsigned long),1,pri);
    prikey=(unsigned char *)calloc(len,sizeof(unsigned char));
    fread(prikey,sizeof(unsigned char),len,pri);
    fclose(pri);*/
    //转换并加载签名者UID
    buf=env->GetCharArrayElements(uid,0);
    uid_name=(unsigned char*)calloc((size_t)len_uid,sizeof(unsigned char));
    memcpy(uid_name,buf,(size_t)len_uid);
    env->ReleaseCharArrayElements(uid,buf,0);
    //签名
    GM_SM2Sign(sigd,&len_sig,src_in,(unsigned long)src_len,uid_name,(unsigned long)len_uid,prikey,len);
    //签名数据返还
    env->SetByteArrayRegion(signed_data,0,64,(jbyte *)sigd);
    sigd_hex=(char*)calloc(129,sizeof(char));
    ByteToHexStr(sigd,sigd_hex,64);
    return env->NewStringUTF(sigd_hex);
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Utils_Gm_1sm2_13_GM_1SM2VerifySig(JNIEnv *env, jobject thiz,
                                                           jbyteArray signed_data,
                                                           jbyteArray _src, jlong src_len,
                                                           jcharArray user_id, jlong len_uid,
                                                           jbyteArray sz_pubkey__xy) {
    // TODO: implement GM_SM2VerifySig()
    unsigned char*uid;
    unsigned char*pubkey;
    unsigned char*src;
    unsigned char*sigd;
    jbyte *buf_b;
    jchar *buf_c;
    int ret;
    //提取源
    buf_b=env->GetByteArrayElements(_src,nullptr);
    src=(unsigned char*)calloc((size_t)src_len,sizeof(unsigned char));
    memcpy(src,buf_b,(size_t)src_len);
    env->ReleaseByteArrayElements(_src,buf_b,0);
    //提取签名数据
    buf_b=env->GetByteArrayElements(signed_data,nullptr);
    sigd=(unsigned char*)calloc(64,sizeof(unsigned char));
    memcpy(sigd,buf_b,64);
    env->ReleaseByteArrayElements(signed_data,buf_b,0);
    //提取签名UID
    buf_c=env->GetCharArrayElements(user_id,nullptr);
    uid=(unsigned char*)calloc((size_t)len_uid,sizeof(unsigned char));
    memcpy(uid,buf_c,64);
    env->ReleaseCharArrayElements(user_id,buf_c,0);
    //提取公钥
    buf_b=env->GetByteArrayElements(sz_pubkey__xy,nullptr);
    pubkey=(unsigned char*)calloc((size_t)64,sizeof(unsigned char));
    memcpy(pubkey,buf_b,(size_t)64);
    env->ReleaseByteArrayElements(sz_pubkey__xy,buf_b,0);
    //验证签名
    ret=GM_SM2VerifySig(sigd,64,src,(unsigned long)src_len,uid,(unsigned long)len_uid,pubkey,64);
    //验证不成功
    if(ret){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "Verify Error");
        return ret;
    }
    //验证成功
    return ret;
}extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Utils_Gm_1sm2_13_GM_1SM2Encrypt(JNIEnv *env, jobject thiz,
                                                         jbyteArray enc_data, jlong ul_enc_data_len,
                                                         jbyteArray plain, jlong plain_len,
                                                         jbyteArray sz_pubkey__xy) {
    // TODO: implement GM_SM2Encrypt()
    unsigned char*enc;
    unsigned char*pubkey;
    unsigned char*src;
    jbyte *buf_c;
    int ret;
    unsigned long len=(unsigned long)ul_enc_data_len;
    //提取源
    buf_c=env->GetByteArrayElements(plain,nullptr);
    src=(unsigned char*)calloc((size_t)plain_len,sizeof(unsigned char));
    memcpy(src,buf_c,(size_t)plain_len);
    env->ReleaseByteArrayElements(plain,buf_c,0);
    //提取公钥
    buf_c=env->GetByteArrayElements(sz_pubkey__xy,nullptr);
    pubkey=(unsigned char*)calloc((size_t)64,sizeof(unsigned char));
    memcpy(pubkey,buf_c,(size_t)64);
    env->ReleaseByteArrayElements(sz_pubkey__xy,buf_c,0);
    //加密
    enc=(unsigned char*)calloc((size_t)ul_enc_data_len,sizeof(unsigned char));
    ret=GM_SM2Encrypt(enc,&len,src,(unsigned long)plain_len,pubkey,(unsigned long)64);
    //
    if(ret){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "Encrypt Error");
        return ret;
    }
    //
    env->SetByteArrayRegion(enc_data,0,(jsize)ul_enc_data_len,(jbyte*)enc);
    return ret;
}extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Utils_Gm_1sm2_13_GM_1SM2Decrypt(JNIEnv *env, jobject thiz,
                                                         jbyteArray dec_data, jlong ul_dec_data_len,
                                                         jbyteArray input, jlong inlen,
                                                         jbyteArray pri_sm) {
    // TODO: implement GM_SM2Decrypt()
    unsigned char*dec;
    unsigned char*prikey;
    unsigned char*src;
    jbyte *buf_c;
    int ret;
    FILE*buf_f;
    unsigned long len=(unsigned long)ul_dec_data_len;


    //提取源
    buf_c=env->GetByteArrayElements(input,0);
    src=(unsigned char*)calloc((size_t)inlen,sizeof(unsigned char));
    memcpy(src,buf_c,(size_t)inlen);
    env->ReleaseByteArrayElements(input,buf_c,0);
    //提取私钥文件名
    buf_c=env->GetByteArrayElements(pri_sm,nullptr);
    prikey=(unsigned char*)calloc((size_t)32,sizeof(unsigned char));
    memcpy(prikey,buf_c,(size_t)64);
    env->ReleaseByteArrayElements(pri_sm,buf_c,0);

    //
    dec=(unsigned char*)calloc((size_t)ul_dec_data_len,sizeof(unsigned char));
    ret=GM_SM2Decrypt(dec,&len,src,(unsigned long)inlen,prikey,32);

    if(ret){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "Decrypt Error");
        return 1;
    }

    env->SetByteArrayRegion(dec_data,0,(jsize)len,(jbyte*)dec);

    return 0;
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_myapplication_Utils_Gm_1sm2_13_sm3(JNIEnv *env, jobject thiz, jbyteArray input,
                                              jlong ilen, jbyteArray output) {
    // TODO: implement sm3()
    unsigned char*chars=NULL;
    unsigned char ou[32];
    char *sigd_hex;
    jbyte *bts=NULL;
    bts=env->GetByteArrayElements(input,0);
    chars=(unsigned char*)calloc((size_t)ilen,sizeof(unsigned char));
    memcpy(chars,bts,(size_t)ilen);
    env->ReleaseByteArrayElements(input,bts,0);
    sm3(chars,(unsigned long)ilen,ou);
    env->SetByteArrayRegion(output,0,32,(jbyte *)ou);
    sigd_hex=(char*)calloc(129,sizeof(char));
    ByteToHexStr(ou,sigd_hex,64);
    return env->NewStringUTF(sigd_hex);
}extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Utils_Gm_1sm2_13_GetEncLen(JNIEnv *env, jobject thiz, jbyteArray plain,
                                                    jlong plain_len, jbyteArray sz_pubkey__xy) {
    // TODO: implement GetEncLen()
    unsigned char*enc;
    unsigned char*pubkey;
    unsigned char*src;
    jbyte *buf_c;
    int ret;
    unsigned long len=(unsigned long)plain_len;
    //提取源
    buf_c=env->GetByteArrayElements(plain,nullptr);
    src=(unsigned char*)calloc((size_t)plain_len,sizeof(unsigned char));
    memcpy(src,buf_c,(size_t)plain_len);
    env->ReleaseByteArrayElements(plain,buf_c,0);
    //提取公钥
    buf_c=env->GetByteArrayElements(sz_pubkey__xy,nullptr);
    pubkey=(unsigned char*)calloc((size_t)64,sizeof(unsigned char));
    memcpy(pubkey,buf_c,(size_t)64);
    env->ReleaseByteArrayElements(sz_pubkey__xy,buf_c,0);
    //加密
    enc=(unsigned char*)calloc((size_t)plain_len,sizeof(unsigned char));
    GM_SM2Encrypt(enc,&len,src,(unsigned long)plain_len,pubkey,(unsigned long)64);
    free(enc);
    return (jint)len;
}extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Utils_Gm_1sm2_13_GetDecLen(JNIEnv *env, jobject thiz, jbyteArray plain,
                                                    jlong plain_len, jbyteArray sz_pubkey__xy) {
    // TODO: implement GetDecLen()
    unsigned char*enc;
    unsigned char*pubkey;
    unsigned char*src;
    jbyte *buf_c;
    unsigned long len=1;
    //提取源
    buf_c=env->GetByteArrayElements(plain,nullptr);
    src=(unsigned char*)calloc((size_t)plain_len*4,sizeof(unsigned char));
    memcpy(src,buf_c,(size_t)plain_len);
    env->ReleaseByteArrayElements(plain,buf_c,0);
    //提取公钥
    buf_c=env->GetByteArrayElements(sz_pubkey__xy,nullptr);
    pubkey=(unsigned char*)calloc((size_t)64,sizeof(unsigned char));
    memcpy(pubkey,buf_c,(size_t)64);
    env->ReleaseByteArrayElements(sz_pubkey__xy,buf_c,0);
    //加密
    enc=(unsigned char*)calloc(1,sizeof(unsigned char));
    GM_SM2Decrypt(enc,&len,src,(unsigned long)plain_len,pubkey,(unsigned long)32);
    free(enc);
    return (jint)len;
}
/*extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_myapplication_Utils_Gm_1sm2_13_sm4Encrypto(JNIEnv *env, jobject thiz,
                                                            jbyteArray enc_data, jbyteArray plain,
                                                            jlong plain_len, jbyteArray key) {
    // TODO: implement sm4Encrypto()
    unsigned char**enc=NULL;
    char*sm4key;
    unsigned char*src;
    jbyte *buf_c;
    int ret;
    unsigned long len=(unsigned long)plain_len;
    //提取源
    buf_c=env->GetByteArrayElements(plain,nullptr);
    src=(unsigned char*)calloc((size_t)plain_len,sizeof(unsigned char));
    memcpy(src,buf_c,(size_t)plain_len);
    env->ReleaseByteArrayElements(plain,buf_c,0);
    //提取公钥
    buf_c=env->GetByteArrayElements(key,nullptr);
    sm4key=(char*)calloc((size_t)32,sizeof(char));
    memcpy(sm4key,buf_c,(size_t)32);
    env->ReleaseByteArrayElements(key,buf_c,0);
    //
    sm4_data_prepare((int)plain_len, src, &ret, src);
    ret=sm4_context_crypt(src,enc,ret,sm4key);
    if(*enc==NULL){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "Decrypt Error");
        return -1;
    }
    env->SetByteArrayRegion(enc_data,0,ret,(jbyte*)*enc);
    return ret;

}extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_myapplication_Utils_Gm_1sm2_13_sm4Decrypto(JNIEnv *env, jobject thiz,
                                                            jbyteArray dec_data, jbyteArray plain,
                                                            jlong plain_len, jbyteArray key) {
    // TODO: implement sm4Decrypto()
    unsigned char**dec=NULL;
    char*sm4key;
    unsigned char*src;
    jbyte *buf_c;
    int ret;
    unsigned long len=(unsigned long)plain_len;
    //提取源
    buf_c=env->GetByteArrayElements(plain,nullptr);
    src=(unsigned char*)calloc((size_t)plain_len,sizeof(unsigned char));
    memcpy(src,buf_c,(size_t)plain_len);
    env->ReleaseByteArrayElements(plain,buf_c,0);
    //提取公钥
    buf_c=env->GetByteArrayElements(key,nullptr);
    sm4key=(char*)calloc((size_t)32,sizeof(char));
    memcpy(sm4key,buf_c,(size_t)32);
    env->ReleaseByteArrayElements(key,buf_c,0);

    sm4_context_decrypt(src,dec,(int)plain_len,sm4key);
    sm4_data_recover((int)plain_len,src,&ret,src);

    if(*dec==NULL){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "Decrypt Error");
        return -1;
    }
    env->SetByteArrayRegion(dec_data,0,ret,(jbyte*)*dec);
    return ret;

}extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_myapplication_Utils_Gm_1sm2_13_Getsm4EncLen(JNIEnv *env, jobject thiz,
                                                             jbyteArray plain, jlong plain_len,
                                                             jbyteArray key) {
    // TODO: implement Getsm4EncLen()
    unsigned char**enc=NULL;
    char*sm4key;
    unsigned char*src;
    jbyte *buf_c;
    int ret;
    unsigned long len=(unsigned long)plain_len;
    //提取源
    buf_c=env->GetByteArrayElements(plain,nullptr);
    src=(unsigned char*)calloc((size_t)plain_len,sizeof(unsigned char));
    memcpy(src,buf_c,(size_t)plain_len);
    env->ReleaseByteArrayElements(plain,buf_c,0);
    //提取公钥
    buf_c=env->GetByteArrayElements(key,nullptr);
    sm4key=(char*)calloc((size_t)32,sizeof(char));
    memcpy(sm4key,buf_c,(size_t)32);
    env->ReleaseByteArrayElements(key,buf_c,0);
    //
    sm4_data_prepare((int)len,src,&ret,src);
    ret=sm4_context_crypt(src,enc,ret,sm4key);
    if(*enc==NULL){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "Incrypt Error");
        return -1;
    }
    return ret;
}extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_myapplication_Utils_Gm_1sm2_13_Getsm4DecLen(JNIEnv *env, jobject thiz,
                                                             jbyteArray plain, jlong plain_len,
                                                             jbyteArray key) {
    // TODO: implement Getsm4DecLen()
    unsigned char**dec=NULL;
    char*sm4key;
    unsigned char*src;
    jbyte *buf_c;
    int ret;
    int len=(int)plain_len;
    //提取源
    buf_c=env->GetByteArrayElements(plain,nullptr);
    src=(unsigned char*)calloc((size_t)plain_len,sizeof(unsigned char));
    memcpy(src,buf_c,(size_t)plain_len);
    env->ReleaseByteArrayElements(plain,buf_c,0);
    //提取公钥
    buf_c=env->GetByteArrayElements(key,nullptr);
    sm4key=(char*)calloc((size_t)32,sizeof(char));
    memcpy(sm4key,buf_c,(size_t)32);
    env->ReleaseByteArrayElements(key,buf_c,0);

    ret=sm4_context_decrypt(src,dec,len,sm4key);


    if(*dec==NULL){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "Decrypt Error");
        return -1;
    }
    ret=sm4_data_recover(ret,*dec,&ret,*dec);
    return ret;
}*/