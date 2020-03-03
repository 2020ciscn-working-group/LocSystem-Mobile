#include <jni.h>
#include <string>
#include <android/log.h>
#include <istream>
#include "sm2_impl/sm3.h"
#include "sm2_impl/sm2.h"
#include "sm2_impl/tommath.h"
#include "sm2_impl/tommath_class.h"
#include "sm2_impl/tommath_superclass.h"

#define LOG_TAG "System.out"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_myapplication_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Gm_1sm2_13_GM_1GenSM2keypair(JNIEnv *env, jobject thiz,
                                                            jbyteArray prikey, jcharArray pri_sm) {
    // TODO: implement GM_GenSM2keypair()
    unsigned char*pri;
    unsigned char pub[64];
    unsigned long len=32;
    unsigned char pri_sm3[32];
    unsigned char pub_sm3[32];
    FILE *prif;

    FILE*pubf;
    int ret=0;
    //執行SM2密钥生成
    pri=(unsigned char *)calloc(1024,sizeof(unsigned char));
    ret=GM_GenSM2keypair(pri,&len,pub);
    if(ret!=0){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                         "Printf3.fprint:format is null");
        return 1;
    }
    //将生成的公钥返回java层
    env->SetByteArrayRegion(prikey,0,64,(jbyte*)pub);
    //以SM3结果为索引存储私钥
    sm3(pri,len,pri_sm3);
    env->SetCharArrayRegion(pri_sm,0,32,(jchar*)pri_sm3);
    if((prif=fopen((char*)pri_sm3,"rb"))==NULL){
        prif=fopen((char*)pri_sm3,"wb");
        fwrite(&len,1,sizeof(unsigned long),prif);
        fwrite(pri,(int)len, sizeof(unsigned long),prif);
    } else{
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "SM2 key GEN Error");
        return 1;
    }
    //以SM3结果为索引存储公钥
    sm3(pub,64,pub_sm3);
    if((pubf=fopen((char*)pub_sm3,"rb"))==NULL){
        pubf=fopen((char*)pub_sm3,"wb");
        fwrite(&len,1,sizeof(unsigned long),pubf);
        fwrite(pri,(int)len, sizeof(unsigned long),pubf);
    } else{
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "SM2 key GEN Error");
        return 1;
    }
    fclose(pubf);
    fclose(prif);
    return 0;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Gm_1sm2_13_GM_1SM2Sign(JNIEnv *env, jobject thiz,
                                                      jcharArray signed_data,
                                                      jbyteArray src, jlong src_len,
                                                      jcharArray uid, jlong len_uid,
                                                      jcharArray pri_sm3) {
    // TODO: implement GM_SM2Sign()
    unsigned char *prikey;
    unsigned char *uid_name;
    unsigned char sigd[64];
    unsigned char *src_in;
    unsigned  long len;
    unsigned long len_sig;
    char pri_name[32];
    jchar*buf;
    FILE* pri;
    jbyte * src_by;
    //签名对象转换成字节
    src_by=env->GetByteArrayElements(src,0);
    src_in=(unsigned char *)calloc((size_t)src_len,sizeof(unsigned char));
    memcpy(src_in,src_by,(size_t)src_len);
    env->ReleaseByteArrayElements(src,src_by,0);
    //转换私钥文件名称
    buf=env->GetCharArrayElements(pri_sm3,0);
    memcpy(pri_name,buf,32);
    env->ReleaseCharArrayElements(pri_sm3,buf,0);
    //打开并加载私钥
    if((pri=fopen(pri_name,"rb"))==NULL){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "SM2 Prikey Not Found");
        return 1;
    };
    fread(&len,sizeof(unsigned long),1,pri);
    prikey=(unsigned char *)calloc(len,sizeof(unsigned char));
    fread(prikey,sizeof(unsigned char),len,pri);
    fclose(pri);
    //转换并加载签名者UID
    buf=env->GetCharArrayElements(uid,0);
    uid_name=(unsigned char*)calloc((size_t)len_uid,sizeof(unsigned char));
    memcpy(uid_name,buf,(size_t)len_uid);
    env->ReleaseCharArrayElements(uid,buf,0);
    //签名
    GM_SM2Sign(sigd,&len_sig,src_in,(unsigned long)src_len,uid_name,(unsigned long)len_uid,prikey,len);
    //签名数据返还
    env->SetCharArrayRegion(signed_data,0,64,(jchar *)sigd);

    return 0;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Gm_1sm2_13_GM_1SM2VerifySig(JNIEnv *env, jobject thiz,
                                                           jcharArray signed_data, jlong ul_sig_len,
                                                           jbyteArray _src, jlong src_len,
                                                           jcharArray user_id, jlong len_uid,
                                                           jcharArray sz_pubkey__xy,
                                                           jlong ul_pubkey__xylen) {
    // TODO: implement GM_SM2VerifySig()
    unsigned char*uid;
    unsigned char*pubkey;
    unsigned char*src;
    unsigned char*sigd;
    jbyte *buf_b;
    jchar *buf_c;
    int ret;
    //验证参数
    if(ul_pubkey__xylen!=(jlong)64||ul_sig_len!=(jlong)64){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "Parameter Error");
        return 1;
    }
    //提取源
    buf_b=env->GetByteArrayElements(_src,nullptr);
    src=(unsigned char*)calloc((size_t)src_len,sizeof(unsigned char));
    memcpy(src,buf_b,(size_t)src_len);
    env->ReleaseByteArrayElements(_src,buf_b,0);
    //提取签名数据
    buf_c=env->GetCharArrayElements(signed_data,nullptr);
    sigd=(unsigned char*)calloc(64,sizeof(unsigned char));
    memcpy(sigd,buf_c,64);
    env->ReleaseCharArrayElements(signed_data,buf_c,0);
    //提取签名UID
    buf_c=env->GetCharArrayElements(user_id,nullptr);
    uid=(unsigned char*)calloc((size_t)len_uid,sizeof(unsigned char));
    memcpy(uid,buf_c,64);
    env->ReleaseCharArrayElements(user_id,buf_c,0);
    //提取公钥
    buf_c=env->GetCharArrayElements(sz_pubkey__xy,nullptr);
    pubkey=(unsigned char*)calloc((size_t)64,sizeof(unsigned char));
    memcpy(pubkey,buf_c,(size_t)64);
    env->ReleaseCharArrayElements(sz_pubkey__xy,buf_c,0);
    //验证签名
    ret=GM_SM2VerifySig(sigd,64,src,(unsigned long)src_len,uid,(unsigned long)len_uid,pubkey,64);
    //验证不成功
    if(!ret){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "Verify Error");
        return 1;
    }
    //验证成功
    return 0;
}extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Gm_1sm2_13_KDFwithSm3(JNIEnv *env, jobject thiz,
                                                     jcharArray kdf_out_buff, jcharArray z_in,
                                                     jlong ul_zlen, jlong klen) {
    // TODO: implement KDFwithSm3()
}extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Gm_1sm2_13_GM_1SM2Encrypt(JNIEnv *env, jobject thiz,
                                                         jcharArray enc_data, jlong ul_enc_data_len,
                                                         jcharArray plain, jlong plain_len,
                                                         jcharArray sz_pubkey__xy,
                                                         jlong ul__pubk_xy_len) {
    // TODO: implement GM_SM2Encrypt()
    unsigned char*enc;
    unsigned char*pubkey;
    unsigned char*src;
    jchar *buf_c;
    int ret;
    unsigned long len=0;

    //验证参数
    if(ul__pubk_xy_len!=(jlong)64){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "Parameter Error");
        return 1;
    }
    //提取源
    buf_c=env->GetCharArrayElements(plain,nullptr);
    src=(unsigned char*)calloc((size_t)plain_len,sizeof(unsigned char));
    memcpy(src,buf_c,64);
    env->ReleaseCharArrayElements(plain,buf_c,0);
    //提取公钥
    buf_c=env->GetCharArrayElements(sz_pubkey__xy,nullptr);
    pubkey=(unsigned char*)calloc((size_t)64,sizeof(unsigned char));
    memcpy(pubkey,buf_c,(size_t)64);
    env->ReleaseCharArrayElements(sz_pubkey__xy,buf_c,0);
    //加密
    enc=(unsigned char*)calloc(1024,sizeof(unsigned char));
    ret=GM_SM2Encrypt(enc,&len,src,(unsigned long)plain_len,pubkey,(unsigned long)64);
    //
    if(!ret){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "Encrypt Error");
        return 1;
    }
    //
    ul_enc_data_len=(jlong)len;
    env->SetCharArrayRegion(enc_data,0,(jsize)len,(jchar*)enc);

    return 0;
}extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Gm_1sm2_13_GM_1SM2Decrypt(JNIEnv *env, jobject thiz,
                                                         jcharArray dec_data, jlong ul_dec_data_len,
                                                         jcharArray input, jlong inlen,
                                                         jcharArray pri_sm) {
    // TODO: implement GM_SM2Decrypt()
    unsigned char*dec;
    unsigned char*prikey;
    unsigned char*src;
    char*prism;
    jchar *buf_c;
    int ret;
    FILE*buf_f;
    unsigned long len=0;
    unsigned long len_=0;

    if(pri_sm== nullptr||input== nullptr){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "Parameter Error");
        return 1;
    }
    //提取源
    buf_c=env->GetCharArrayElements(input,0);
    src=(unsigned char*)calloc((size_t)inlen,sizeof(unsigned char));
    memcpy(src,buf_c,(size_t)inlen);
    env->ReleaseCharArrayElements(input,buf_c,0);
    //提取私钥文件名
    buf_c=env->GetCharArrayElements(pri_sm,nullptr);
    prism=(char*)calloc((size_t)32,sizeof(char));
    memcpy(prism,buf_c,(size_t)32);
    env->ReleaseCharArrayElements(pri_sm,buf_c,0);
    //提取私钥
    if((buf_f=fopen(prism,"rb"))==NULL){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "SM2 Prikey Not Found");
        return 1;
    };
    fread(&len,sizeof(unsigned long),1,buf_f);
    prikey=(unsigned char *)calloc(len,sizeof(unsigned char));
    fread(prikey,sizeof(unsigned char),len,buf_f);
    fclose(buf_f);
    //
    dec=(unsigned char*)calloc(1024,sizeof(unsigned char));
    ret=GM_SM2Decrypt(dec,&len_,src,(unsigned long)inlen,prikey,len);

    if(ret){
        (env)->ThrowNew((env)->FindClass( "java/lang/NullPointerException"),
                        "Decrypt Error");
        return 1;
    }

    env->SetCharArrayRegion(dec_data,0,(jsize)len_,(jchar*)dec);
    ul_dec_data_len=len_;
    return 0;
}extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Gm_1sm2_13_BYTE_1POINT_1is_1on_1sm2_1curve(JNIEnv *env, jobject thiz,
                                                                          jcharArray pubkey__xy,
                                                                          jlong ul_pub_xylen) {
    // TODO: implement BYTE_POINT_is_on_sm2_curve()
}extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Gm_1sm2_13_BYTE_1Point_1mul(JNIEnv *env, jobject thiz, jcharArray k,
                                                           jcharArray new_point) {
    // TODO: implement BYTE_Point_mul()

}extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_myapplication_Gm_1sm2_13_getVersion(JNIEnv *env, jobject thiz) {
    // TODO: implement getVersion()
    char*ver;
    //ver = getVersion();
    ver="24895\0";
    jclass strClass = env->FindClass("Ljava/lang/String;");
    jmethodID ctorID = env->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
    jbyteArray bytes = env->NewByteArray(strlen(ver));
    env->SetByteArrayRegion(bytes, 0, strlen(ver), (jbyte*)ver);
    jstring encoding = env->NewStringUTF("utf-8");
    return (jstring)env->NewObject(strClass, ctorID, bytes, encoding);
}extern "C"
JNIEXPORT void JNICALL
Java_com_example_myapplication_Gm_1sm2_13_sm3(JNIEnv *env, jobject thiz, jbyteArray input,
                                              jlong ilen, jbyteArray output) {
    // TODO: implement sm3()
    //jbyte *buf=env->GetByteArrayElements(input,0);
    //unsigned char*chars=(unsigned char *)buf;
    char*chars=NULL;
    unsigned char ou[32];
    jbyte *bts=NULL;
    bts=env->GetByteArrayElements(input,0);
    chars=(char*)calloc(ilen,sizeof(char));
    memcpy(chars,bts,ilen);
    env->ReleaseByteArrayElements(input,bts,0);
    sm3((unsigned char *)chars,ilen,ou);
    env->SetByteArrayRegion(output,0,32,(jbyte *)ou);
}