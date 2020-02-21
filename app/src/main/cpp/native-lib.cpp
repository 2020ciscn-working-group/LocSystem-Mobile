#include <jni.h>
#include <string>
#include <android/log.h>
#include "sm2_impl/sm3.h"

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
                                                            jcharArray prikey, jobject pul_pri_len,
                                                            jcharArray pubkey__xy) {
    // TODO: implement GM_GenSM2keypair()

}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Gm_1sm2_13_GM_1SM2Sign(JNIEnv *env, jobject thiz,
                                                      jcharArray signed_data, jlong pul_sig_len,
                                                      jcharArray src, jlong src_len,
                                                      jcharArray user_id, jlong len_uid,
                                                      jcharArray prikey, jlong ul_prikey_len) {
    // TODO: implement GM_SM2Sign()
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Gm_1sm2_13_GM_1SM2VerifySig(JNIEnv *env, jobject thiz,
                                                           jcharArray signed_data, jlong ul_sig_len,
                                                           jcharArray src, jlong src_len,
                                                           jcharArray user_id, jlong len_uid,
                                                           jcharArray sz_pubkey__xy,
                                                           jlong ul_pubkey__xylen) {
    // TODO: implement GM_SM2VerifySig()
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
}extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_Gm_1sm2_13_GM_1SM2Decrypt(JNIEnv *env, jobject thiz,
                                                         jcharArray dec_data, jlong ul_dec_data_len,
                                                         jcharArray input, jlong inlen,
                                                         jcharArray pri_d_a, jlong ul_pri_d_alen) {
    // TODO: implement GM_SM2Decrypt()
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