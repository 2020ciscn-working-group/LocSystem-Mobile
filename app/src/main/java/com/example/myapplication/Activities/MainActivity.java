package com.example.myapplication.Activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication.Activities.Models.thread.Pull;
import com.example.myapplication.Dao.Dao_Tocken;
import com.example.myapplication.Dao.Sql.AppSql;
import com.example.myapplication.DateStract.Accexp;
import com.example.myapplication.DateStract.Accreq;
import com.example.myapplication.DateStract.LocalKey;
import com.example.myapplication.DateStract.Tocken;
import com.example.myapplication.Interfaces.PullCallBack;
import com.example.myapplication.R;
import com.example.myapplication.Utils.ByteUtils;
import com.example.myapplication.Utils.Gm_sm2_3;
import com.example.myapplication.Utils.Util;
import com.example.myapplication.Utils.jsontrans;
import com.example.myapplication.owner_date;

import java.nio.channels.NonReadableChannelException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public static String path;
    private WebView webView;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("gm_sm2_master");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        path=MainActivity.this.getFilesDir().toString();
        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

    }
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onStart() {
        super.onStart();
        Gm_sm2_3 gm=Gm_sm2_3.getInstance();
        byte []pub=new byte[64];
        byte []pri=new byte[32];
        byte []sigd=new byte[64];
        byte []sm3=new byte[32];
        int ret=0;
        String pri_sm3= gm.GM_GenSM2keypair(pub,pri);
        String uid="zyc14588";
        byte []src=ByteUtils.objectToByteArray(gm);

        String sigd_hex=gm.GM_SM2Sign(sigd,src,src.length,uid.toCharArray() ,uid.toCharArray().length,pri);
        Log.d("pub", Arrays.toString(pub));
        Log.d("pri", Arrays.toString(pri));
        Log.d("pub", Util.byteToHex(pub));
        Log.d("pri", Util.byteToHex(pri));
        Log.d("pri_sm",pri_sm3);
        Log.d("sign", Arrays.toString(src));
        Log.d("sign", sigd_hex);

        ret=gm.GM_SM2VerifySig(sigd,src,src.length,uid.toCharArray() ,uid .toCharArray().length,pub);
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

        String externalFilesDir = MainActivity.this.getExternalCacheDir().toString();
        owner_date own=new owner_date();
        own.setUuid("72351398430564");
        own.setInfo("fsdgwreyeturturtyuhgr");
        own.setOther("dsgwqgsdfwfasdfqw");
        own.setname("scdtx");
        try {
            Util.saveSm2Key(pub,pri,own,0,0,externalFilesDir);
        } catch (RuntimeException e){

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("save error",e.getMessage());
        }
        AppSql sql=new AppSql(this);

        Tocken tocken=new Tocken();
        Accexp accexp=new Accexp();
        Accreq accreq=new Accreq();
        byte[] sign_pub=new byte[64];
        byte[] sign_pri=new byte[32];
        gm.GM_GenSM2keypair(sign_pub,sign_pri);
        byte[] pik_pub=new byte[64];
        byte[] pik_pri=new byte[32];
        gm.GM_GenSM2keypair(pik_pub,pik_pri);

        accreq.setAccsee("一号门");
        accreq.setInfo("北京市通州区xx镇xxx街道xxx号" );
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        accreq.setTime(dateFormat.format(calendar.getTime()));
        accreq.setInfolen(accreq.getInfo().length());
        accreq.setsigndatalen(0);
        accreq.setsigndata(null);
        byte[] src_=ByteUtils.objectToByteArray(accreq);
        gm.GM_SM2Sign(sigd,src_,src_.length,"zyc14588".toCharArray() ,"zyc14588".toCharArray().length ,sign_pri);
        accreq.setsigndata(sigd);
        accreq.setsigndatalen(sigd.length);

        accexp.setAccreq(accreq);
        accexp.setAccendtime(dateFormat.format(calendar.getTime()) );
        accexp.setAccendtime("2020-12-12 :10:10:10" );
        accexp.setAccess("一号门" );
        accexp.setInfo("北京市通州区xxx镇xx01街道xx号" );
        accexp.setPIK(pik_pub);
        accexp.setSignkey(sign_pub);

        byte[]src__=ByteUtils.objectToByteArray(accexp);
        tocken.setAccexp(accexp);
        tocken.setUuid(gm.GM_SM2Sign(sigd,src__,src__.length,"zyc14588".toCharArray() ,"zyc14588".toCharArray() .length,pri).substring(0,16) );
        tocken.setSigndata(sigd);
        tocken.setSingdatalen(sigd.length);

        Dao_Tocken dao_tocken=Dao_Tocken.getInstance(sql);
        try {
            dao_tocken.InsertTocken(tocken);
            String uuid= tocken.getUuid();
            Tocken tkd=dao_tocken.SelectTocken(uuid);
            Log.d("info", tkd.getAccexp().getInfo());
            /*String key= Util.byteToHex(SM4Utils.genersm4key());
            byte[]tocken_byte=ByteUtils.objectToByteArray(tkd);
            byte[]sec=new byte[(int)gm.Getsm4EncLen(tocken_byte,tocken_byte.length,Util.hexToByte(key))];
            gm.sm4Encrypto(sec,tocken_byte,tocken_byte.length,Util.hexToByte(key));
            byte[]decsm4=new byte[(int)gm.Getsm4DecLen(sec,sec.length,Util.hexToByte(key))];
            gm.sm4Decrypto(decsm4,sec,sec.length,Util.hexToByte(key));
            Tocken tk= (Tocken) ByteUtils.byteArrayToObject(decsm4);
            Log.d("sm4dec:",tk.getAccexp().getInfo());*/
            LocalKey sign=new LocalKey();
            LocalKey bind=new LocalKey();
            Util.Gen_LocalKey(sign,bind,"北京市通州区",pik_pri,"zyc14588");

        } catch (Exception e) {
            e.printStackTrace();
        }
        String json= jsontrans.trans_tocken_to_json(tocken);
        Log.d("json",json);
        /*final Pull pull=new Pull("");
        final LinkedList<String> pulls = new LinkedList<String>();
        pull.setPullCallBack(new PullCallBack() {
            @Override
            public void onPullCallBackSuccessfully(Object data) {
                pulls.add((String)data);
            }

            @Override
            public void onPullCallBackFailed() {
                throw new NonReadableChannelException();
            }

        });
        pull.execute();
        Log.d("socket:",pulls.pop());*/
        webView=findViewById(R.id.webview);
        webView.setWebChromeClient(new WebChromeClient());
        WebSettings ws= webView.getSettings();
        //启用jsp脚本
        ws.setJavaScriptEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        ws.setDefaultTextEncodingName("utf-8");
        ws.setLoadsImagesAutomatically(true);
        ws.setSupportZoom(false);
        ws.setBuiltInZoomControls(false);
        ws.setDomStorageEnabled(true);
        ws.setAppCacheEnabled(true);
        ws.setAllowFileAccess(true);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new WebViewClient());
        //添加jsp的安卓接口内部类
        webView.addJavascriptInterface(new JavaScriptInterface(),"jspcallback");
        webView.loadUrl("file:////android_asset/dist/login-form/index.html");
        //webView.loadUrl("http://www.baidu.com");

    }
    @Override
    protected void onResume() {
        super.onResume();

    }
//安卓调用vue的jsp函数用的方法
    private void androidJSBridge(String methodName) {
        String url = "javascript:window." + methodName + "()";
        switch(methodName){
            case"back":{
                webView.evaluateJavascript(url, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                    }
                });
                break;
            }

        }
    }
    @Override
    public void onBackPressed() {
//    super.onBackPressed();
        androidJSBridge("back");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    //jsp的安卓接口
    private class JavaScriptInterface{
        @JavascriptInterface
        public void callAndroidMethod(int a, float b, String c, boolean d) {
            if (d) {
                String strMessage = "a+b+c=" + a + b + c;
                Log.d("jspcallback:",strMessage);
            }
        }
        @JavascriptInterface
        public void finish(String ssss) {
            MainActivity.this.finish();
        }




    }
}
