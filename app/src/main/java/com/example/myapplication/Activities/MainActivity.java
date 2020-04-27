package com.example.myapplication.Activities;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.Activities.Models.Internet.Friend;
import com.example.myapplication.Activities.Models.Internet.Login;
import com.example.myapplication.Activities.Models.Internet.Message;
import com.example.myapplication.Activities.Models.Internet.User;
import com.example.myapplication.Activities.Models.Model_Crypto;
import com.example.myapplication.Activities.Models.Model_User;
import com.example.myapplication.Activities.Models.thread.Push;
import com.example.myapplication.Dao.Secret.Dao_Audit;
import com.example.myapplication.Dao.Secret.Dao_Hub;
import com.example.myapplication.Dao.Secret.Dao_Remotekey;
import com.example.myapplication.Dao.Secret.Dao_Tocken;
import com.example.myapplication.Dao.Secret.Sql.AppSql;
import com.example.myapplication.DateStract.Accexp;
import com.example.myapplication.DateStract.Accreq;
import com.example.myapplication.DateStract.Audit;
import com.example.myapplication.DateStract.Hub;
import com.example.myapplication.DateStract.LocalKey;
import com.example.myapplication.DateStract.RemoteKey;
import com.example.myapplication.DateStract.Tocken;
import com.example.myapplication.Defin.Defin_internet;
import com.example.myapplication.Interfaces.PushCallBackListener;
import com.example.myapplication.R;
import com.example.myapplication.Servers.PullService;
import com.example.myapplication.Utils.ByteUtils;
import com.example.myapplication.Utils.Gm_sm2_3;
import com.example.myapplication.Utils.Util;
import com.example.myapplication.Utils.jsontrans;
import com.example.myapplication.owner_date;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static String path;
    private WebView webView;
    private Model_User mModel_user;
    private Model_Crypto mModel_crypto;
    private AppSql sql;
    private ServiceConnection mServiceConnection;
    private PullService       mService;
    private String loginret;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("gm_sm2_master");
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        path=MainActivity.this.getFilesDir().toString();

        // Example of a call to a native method
        /*mModel_user=new Model_User();
        mServiceConnection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                PullService.LocalBinder binder=(PullService.LocalBinder)service;
                mService=binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService=null;
            }
        };
        final Intent intent = new Intent(this,PullService.class);
        bindService(intent,mServiceConnection, Service.BIND_AUTO_CREATE);*/
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
        //添加jsp的安卓接口内部类,VUE使用$APP符号即可调用给jsp的按安卓方法接口了
        webView.addJavascriptInterface(new JavaScriptInterface(),"$App");
        //dist文件夹
        //webView.loadUrl("file:////android_asset/dist/index.html");
        //vue调试的页面
        webView.loadUrl("http://10.0.2.2:8081");
    }


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

        String externalFilesDir = Objects.requireNonNull(MainActivity.this.getExternalCacheDir()).toString();
        owner_date own=new owner_date();
        own.setUuid("72351398430564");
        own.setInfo("fsdgwreyeturturtyuhgr");
        own.setOther("dsgwqgsdfwfasdfqw");
        own.setname("scdtx");
        try {
            Util.saveSm2Key(pub,pri,own,0,0,externalFilesDir);
        } catch (RuntimeException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("save error",e.getMessage());
        }
         sql=new AppSql(this);

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
            LocalKey sign=new LocalKey();
            LocalKey bind=new LocalKey();
            Util.Gen_LocalKey(sign,bind,"北京市通州区",pik_pri,"zyc14588");
            String json= jsontrans.trans_tocken_to_json(tkd);
            Log.d("json",json);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    //调用vue的返回
    @Override
    public void onBackPressed() {
//    super.onBackPressed();
        androidJSBridge("back");
    }

    //jsp的安卓接口
    private class JavaScriptInterface{
        @JavascriptInterface
        public void callAndroidMethod(int a, float b, String c, boolean d) {
            if (d) {
                String strMessage = "a+b+c=" + a + b + c;
                Log.d("jspcallback:",strMessage);
            }
        }
        public void login(final String uid, String passwd){
            Push push=new Push(Defin_internet.SeverAddress+Defin_internet.AppServerPort+Defin_internet.AppServerLogin, new PushCallBackListener() {
                @Override
                public void onPushSuccessfully(String data) {
                    Log.d("POST",data);
                    loginret=data;
                    //TODO:添加登陆成功后需要执行的的代码
                    String name="/Model/Crypto"+uid;
                    File Model_Crypto_file=new File(MainActivity.path+name);
                    try {
                        if(Model_Crypto_file.exists()){
                            FileInputStream fileInputStream = new FileInputStream(Model_Crypto_file);
                            byte[] Model_Crypto_bytes=new byte[fileInputStream.available()];
                            fileInputStream.read(Model_Crypto_bytes);
                            mModel_crypto=(Model_Crypto) ByteUtils.byteArrayToObject(Model_Crypto_bytes);
                        }else {

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onPushFailed(int code,String message) {
                    //TODO：登陆失败的代码
                    Log.d("POST ERROR CODE", message+code);
                    loginret=code+message;
                }
            });
            Login login=new Login();
            login.setPassword(passwd);
            login.setUid(uid);
            Log.d("login:",login.toJson());
            push.execute(login.toJson());
        }
        @JavascriptInterface
        public String getLoginret(){
            return loginret;
        }
        @JavascriptInterface
        public void finish() {
            MainActivity.this.finish();
        }
        @JavascriptInterface
        public String getUser(){
            return mModel_user.getUser().toJson();
        }
        @JavascriptInterface
        public String getFriend(String uid){
            LinkedList<Friend> Friendlist=mModel_user.getFriends();
            for(Friend ff:Friendlist){
                if(ff.getUid().equals(uid))
                    return ff.toJson();
            }
            return null;
        }
        @JavascriptInterface
        public String getMessages(String Frienduid){
            List<Message> messages= mModel_user.getUser().getFriend(Frienduid).getMessagehashList();
            StringBuffer json= new StringBuffer("[");
            for(Message message:messages){
                json.append(message.toJson()).append(",");
            }
            json = new StringBuffer(json.substring(0, json.length() - 1));
            json.append("]");
            return json.toString();
        }
        @JavascriptInterface
        public String getFriends(){
            StringBuffer friends= new StringBuffer("[");
            LinkedList<Friend> Friendlist=mModel_user.getFriends();
            for(Friend ff:Friendlist){
                friends.append(ff.toJson()).append(",");
            }
            friends = new StringBuffer(friends.substring(0, friends.length() - 1));
            friends.append("]");
            return friends.toString();
        }
        @JavascriptInterface
        public String getTocken(String uuid) throws IOException {
            Dao_Tocken dao_tocken=Dao_Tocken.getInstance(sql);
            Tocken tocken=dao_tocken.SelectTocken(uuid);
            Gson gson=new Gson();
            return gson.toJson(tocken,tocken.getClass());
        }
        @JavascriptInterface
        public String getAudit(String uuid) throws IOException {
            Dao_Audit dao_audit=Dao_Audit.getInstance(sql);
            Audit audit=dao_audit.SelectAudit(uuid);
            Gson gson=new Gson();
            return gson.toJson(audit,Audit.class);
        }
        @JavascriptInterface
        public String getHub(String uuid) throws IOException {
            Dao_Hub dao_hub=Dao_Hub.getInstance(sql);
            Hub hub=dao_hub.SelectHub(uuid);
            Gson gson=new Gson();
            return gson.toJson(hub,Hub.class);
        }
        @JavascriptInterface
        public String getRemoteKey(String uuid) throws IOException {
            Dao_Remotekey dao_remotekey=Dao_Remotekey.getInstance(sql);
            RemoteKey remoteKey=dao_remotekey.SelectRemotekey(uuid);
            Gson gson=new Gson();
            return gson.toJson(remoteKey,RemoteKey.class);
        }

    }
}
