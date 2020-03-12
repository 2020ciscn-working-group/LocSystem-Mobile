package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Utils.ByteUtils;
import com.example.myapplication.Utils.Gm_sm2_3;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("gm_sm2_master");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        Gm_sm2_3 gm=new Gm_sm2_3();
        byte []pub=new byte[64];
        byte []pri=new byte[32];
        byte []sigd=new byte[64];
        byte []sm3=new byte[32];
        int ret=0;
        String pri_sm3= gm.GM_GenSM2keypair(pub,pri);
        String uid="zyc14588";
        byte []src=ByteUtils.objectToByteArray(gm);

        String sigd_hex=gm.GM_SM2Sign(sigd,src,src.length,uid.toCharArray(),uid.toCharArray().length,pri);
        Log.d("pub", Arrays.toString(pub));
        Log.d("pri", Arrays.toString(pri));
        Log.d("pri_sm",pri_sm3);
        Log.d("sign", Arrays.toString(src));
        Log.d("sign", sigd_hex);

        ret=gm.GM_SM2VerifySig(sigd,src,src.length,uid.toCharArray(),uid.toCharArray().length,pub);
        if(ret==0)Log.d("sign_verf","success");
        else Log.e("sign_verf", String.valueOf(ret));

        String src_sm3=gm.sm3(src,src.length,sm3);
        Log.d("sm3", Arrays.toString(sm3));
        Log.d("sm3", src_sm3);

        int len=gm.GetEncLen(src,src.length,pub);
        byte []enc=new byte[len];
        try{
        ret=gm.GM_SM2Encrypt(enc,len,src,src.length,pub);}
        catch (Exception e) {
            e.printStackTrace();
        }
        if(ret==0)Log.d("encrpty","success");
        else Log.e("encrpty", String.valueOf(ret));
        Log.d("encrpty", Arrays.toString(enc));

        len=gm.GetDecLen(enc,enc.length,pri);
        byte []dec=new byte[len];
        try{
            ret=gm.GM_SM2Decrypt(dec,len,enc,enc.length,pri);}
        catch (Exception e) {
            e.printStackTrace();
        }
        if(ret==0)Log.d("decrpty","success");
        else Log.e("decrpty", String.valueOf(ret));
        Gm_sm2_3 dec_ret=(Gm_sm2_3) ByteUtils.byteArrayToObject(dec);
        Log.d("decrpty", dec_ret.test);
        //File externalFilesDir = Context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
