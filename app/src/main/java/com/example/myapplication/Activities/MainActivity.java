package com.example.myapplication.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.Activities.Models.Internet.Friend;
import com.example.myapplication.Activities.Models.Internet.FriendQuery;
import com.example.myapplication.Activities.Models.Internet.Login;
import com.example.myapplication.Activities.Models.Internet.Message;
import com.example.myapplication.Activities.Models.Internet.NewFriend;
import com.example.myapplication.Activities.Models.Internet.PullMessage;
import com.example.myapplication.Activities.Models.Internet.QueryFriend;
import com.example.myapplication.Activities.Models.Internet.SendMessage;
import com.example.myapplication.Activities.Models.Internet.SignUp;
import com.example.myapplication.Activities.Models.Internet.Sm4ex;
import com.example.myapplication.Activities.Models.Internet.User;
import com.example.myapplication.Activities.Models.Internet.Vue_Accreq;
import com.example.myapplication.Activities.Models.Model_Crypto;
import com.example.myapplication.Activities.Models.Model_User;
import com.example.myapplication.Activities.Models.thread.PullScheduledThreadPoolExecutor;
import com.example.myapplication.Activities.Models.thread.Push;
import com.example.myapplication.Dao.Internet.Dao_Message;
import com.example.myapplication.Dao.Secret.Dao_Audit;
import com.example.myapplication.Dao.Secret.Dao_Hub;
import com.example.myapplication.Dao.Secret.Dao_Remotekey;
import com.example.myapplication.Dao.Secret.Dao_Tocken;
import com.example.myapplication.Dao.Secret.Sql.AppSql;
import com.example.myapplication.DateStract.Accexp;
import com.example.myapplication.DateStract.Accreq;
import com.example.myapplication.DateStract.Audit;
import com.example.myapplication.DateStract.Auditexp;
import com.example.myapplication.DateStract.Cert;
import com.example.myapplication.DateStract.Guest;
import com.example.myapplication.DateStract.Hub;
import com.example.myapplication.DateStract.Loc;
import com.example.myapplication.DateStract.LocalHub;
import com.example.myapplication.DateStract.LocalKey;
import com.example.myapplication.DateStract.Owner;
import com.example.myapplication.DateStract.RemoteKey;
import com.example.myapplication.DateStract.Tocken;
import com.example.myapplication.Defin.Defin_crypto;
import com.example.myapplication.Defin.Defin_internet;
import com.example.myapplication.Interfaces.PushCallBackListener;
import com.example.myapplication.R;
import com.example.myapplication.Servers.PullService;
import com.example.myapplication.Utils.ByteUtils;
import com.example.myapplication.Utils.Gm_sm2_3;
import com.example.myapplication.Utils.UsbHelper;
import com.example.myapplication.Utils.Util;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static String path;
    private WebView webView;
    private Model_User mModel_user;
    private Model_Crypto mModel_crypto;
    private AppSql sql;
    private ServiceConnection mServiceConnection;
    private PullService       mService;
    private String loginret;
    private PullScheduledThreadPoolExecutor mPullScheduledThreadPoolExecutor;


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("gm_sm2_master");
    }
//TODO:将USB数据交换activity换成serve

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //无title
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

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

    }


    @Override
    protected void onStart() {
        super.onStart();
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
        //ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new WebViewClient());
        //添加jsp的安卓接口内部类,VUE使用$APP符号即可调用给jsp的按安卓方法接口了
        JavaScriptInterface javaScriptInterface=new JavaScriptInterface();
        webView.addJavascriptInterface(javaScriptInterface,"$APP");
        //dist文件夹
        webView.loadUrl("file:////android_asset/dist/index.html");
        //vue调试的页面
        //webView.loadUrl("http://192.168.0.100:8081");
        sql=new AppSql(this);

   /*     byte []pub=new byte[64];
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
*/

   Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Gm_sm2_3 gm=Gm_sm2_3.getInstance();
                Tocken tocken=new Tocken();
                Accexp accexp=new Accexp();
                Accreq accreq=new Accreq();
                byte[] sign_pub=new byte[64];
                byte[] sign_pri=new byte[32];
                gm.GM_GenSM2keypair(sign_pub,sign_pri);
                byte[] pub=new byte[64];
                byte[] pri=new byte[32];
                gm.GM_GenSM2keypair(pub,pri);

                byte[]sigd=new byte[64];
                accreq.setAccsee(5);
                accreq.setInfo("北京市通州区xx镇xxx街道xxx号" );
                Calendar calendar= Calendar.getInstance();
                calendar.add(Calendar.MONTH,1);
                //SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
                accreq.setTime(String.valueOf(calendar.getTimeInMillis()));
                accreq.setInfolen(accreq.getInfo().length());
                accreq.setsigndatalen(0);
                accreq.setsigndata(null);
                accreq.setHubuuid("ACCDCBAEFFAAV");
                accreq.setPub(sign_pub);
                byte[] src_=ByteUtils.objectToByteArray(accreq);
                gm.GM_SM2Sign(sigd,src_,src_.length,"zyc14588".toCharArray() ,"zyc14588".toCharArray().length ,sign_pri);
                accreq.setsigndata(sigd);
                accreq.setsigndatalen(sigd.length);

                accexp.setAccreq(accreq);
                //accexp.setAccendtime(dateFormat.format(calendar.getTime()) );
                calendar.add(Calendar.MONTH,1);
                accexp.setAccendtime(String.valueOf(calendar.getTimeInMillis()));
                accexp.setAccess(5 );
                accexp.setInfo("北京市通州区xxx镇xx01街道xx号" );
                accexp.setRootKey(pub);
                accexp.setSignkey(sign_pub);
                Gson gson=new Gson();
                byte[]src__=ByteUtils.objectToByteArray(accexp);
                tocken.setAccexp(accexp);
                tocken.setUuid(gm.GM_SM2Sign(sigd,src__,src__.length,"zyc14588".toCharArray() ,"zyc14588".toCharArray() .length,pri).substring(0,16) );
                tocken.setSigndata(sigd);
                tocken.setSingdatalen(sigd.length);
                Audit audit=new Audit();
                Auditexp auditexp=new Auditexp();
                auditexp.setAccexp(accexp);
                Random random=new Random();
                auditexp.setRNG(String.valueOf(random.nextLong()));
                auditexp.setUuid_Tocken(tocken.getUuid());
                auditexp.setTime(String.valueOf(calendar.getTimeInMillis()));
                audit.setAuditexp(auditexp);
                byte[]src=ByteUtils.objectToByteArray(auditexp);
                audit.setUuid(gm.GM_SM2Sign(sigd,src,src.length,"zyc14588".toCharArray() ,"zyc14588".toCharArray() .length,pri).substring(0,16));
                audit.setsigndata(src);
                audit.setsigndatalen(64);
                Log.d("tocken:",Base64.encodeToString(tocken.toJson().getBytes(),Base64.DEFAULT));
                Log.d("Audit:",audit.toJson().substring(0,2048));
                Log.d("Audit:",audit.toJson().substring(2048));
                Sm4ex sm4ex=new Sm4ex();
                try {
                    sm4ex.keygen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("sm4ex:",sm4ex.toJson());
                Hub hub=new Hub();
                hub.setUuid_ow("WAACDEFBBACA");
                hub.setUuid("qwqqweqweq1we");
                hub.setInfo("dfhsedhsedrh");
                hub.setId("sdfsdfgearf");
                hub.setDesc("xxxxx");
                LinkedList<Loc> locs=new LinkedList<>();
                Loc loc=new Loc();
                loc.setDesc("xxx");
                loc.setHub_uuid(hub.getUuid());
                loc.setLock_id(1);
                locs.add(loc);
                hub.setLocs(locs);
                Log.d("hub",hub.toJson());
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();


/*
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
*/

    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        save();
    }
    public void save(){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Gson gson=new Gson();
                String name_c="/Model/Crypto/"+mModel_user.getUser().getUid();
                String name_u="/Model/User/"+mModel_user.getUser().getUid();
                File Model_Crypto_file=new File(MainActivity.path+name_c);
                File Model_user_file=new File(MainActivity.path+name_u);
                File cr_dir=Model_Crypto_file.getParentFile();
                File us_dir=Model_user_file.getParentFile();
                if(cr_dir!=null&&!cr_dir.exists())
                    cr_dir.mkdirs();
                if(us_dir!=null&&!us_dir.exists())
                    us_dir.mkdirs();
                if(!Model_Crypto_file.exists()){
                    try {
                        Model_Crypto_file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(!Model_user_file.exists()){
                    try {
                        Model_user_file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileOutputStream fileOutputStream;
                try{
                    Log.d("owsave:",gson.toJson(mModel_crypto.getOwner(), Owner.class));
                    fileOutputStream=new FileOutputStream(Model_Crypto_file);
                    fileOutputStream.write(gson.toJson(mModel_crypto.getOwner(), Owner.class).getBytes());
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    fileOutputStream=new FileOutputStream(Model_user_file);
                    fileOutputStream.write(gson.toJson(mModel_user.getUser(),User.class).getBytes());
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("save","complete");
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();
    }

//安卓调用vue的jsp函数用的方法
    private void androidJSBridge(String methodName) {
        String url = "javascript:window." + methodName + "()";
        switch(methodName){
            case"back":
            case"signin_success":
            case"pullmessage_success":
            case"signup_success":{
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
    private void androidJSBridge(String methodName,String data) {
        String url = "javascript:window." + methodName + "(\'"+data+"\')";
        switch(methodName){
            case"back":
            case"signin_success":
            case"addFriend_success":
            case"pullmessage_success":
            case "chatItem_receieve":
            case "accreq":
            case"signup_success":{
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
    private void load(final String uid, final String passwd){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                //TODO:添加登陆成功后需要执行的的代码
                Gson gson = new Gson();
                String name_c = "/Model/Crypto/" + uid;
                String name_u = "/Model/User/" + uid;
                File Model_Crypto_file = new File(MainActivity.path + name_c);
                File Model_user_file = new File(MainActivity.path + name_u);
                if (Model_Crypto_file.exists() && Model_user_file.exists()) {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(Model_Crypto_file);
                        byte[] Model_Crypto_bytes = new byte[fileInputStream.available()];
                        fileInputStream.read(Model_Crypto_bytes);
                        Owner owner = gson.fromJson(new String(Model_Crypto_bytes), Owner.class);
                        mModel_crypto = new Model_Crypto(MainActivity.this, owner);
                        Log.d("owner:",new String(Model_Crypto_bytes));
                        fileInputStream = new FileInputStream(Model_user_file);
                        byte[] Model_User_bytes = new byte[fileInputStream.available()];
                        fileInputStream.read(Model_User_bytes);
                        User user = gson.fromJson(new String(Model_User_bytes), User.class);
                        mModel_user = new Model_User(MainActivity.this, user);
                        Log.d("user:",new String(Model_User_bytes));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();
    }
    //调用vue的返回
    @Override
    public void onBackPressed() {
//    super.onBackPressed();
        //androidJSBridge("back");
    }

    //jsp的安卓接口
    private class JavaScriptInterface{
        @JavascriptInterface
        public void negoreq(String friuenduid) throws Exception {
            for (Friend friend1 : mModel_user.getUser().getFriendUidList()) {
                if (friend1.getFirend_uid().equals(friuenduid)) {
                    List<RemoteKey> remoteKeys=mModel_crypto.getGuest(mModel_user.getUser().getFriend(friuenduid).getGuestid()).getRemoteKey();
                    if(!remoteKeys.isEmpty()){
                        for(RemoteKey remoteKey:remoteKeys){
                            if(remoteKey.getType()==Defin_crypto.BIND){
                                Sm4ex sm4ex=new Sm4ex();
                                sm4ex.keygen();
                                sendMessage(Util.sm2inc(sm4ex.toJson(),remoteKey.getPubkey()),Defin_internet.nego_req,friuenduid);
                                return;
                            }
                        }
                    }

                }
            }
            AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("保密通讯密钥交换错误")//标题
                    .setMessage("未找到对方绑定密钥，请等待对方添加您为好友")//内容
                    .setIcon(R.mipmap.ic_launcher)//图标
                    .create();
            alertDialog1.show();
        }
        @JavascriptInterface
        public void accreq( final String json,final String frienduid){

            /*Runnable runnable=new Runnable() {
                @Override
                public void run() {
                    }
            };
            Thread thread=new Thread(runnable);
            thread.start();*/
            RemoteKey remoteKey=mModel_crypto.getGuest(mModel_user.getUser().getFriend(frienduid).getGuestid()).getremotekey(Defin_crypto.BIND);
            if(remoteKey==null) {
                Log.d("accreq","remotekey not found");
                return;
            }
            if(json==null){
                Log.d("vue err","vue json null");
                return;
            }
            Log.d("vue insetr",json);
            Gson gson=new Gson();
            Gm_sm2_3 gm_sm2_3=Gm_sm2_3.getInstance();
            Vue_Accreq vue_accreq=gson.fromJson(json, Vue_Accreq.class);
            Accreq accreq=new Accreq();
            accreq.setHubuuid(vue_accreq.getHubuuid());
            accreq.setAccsee(vue_accreq.getAccsee());
            accreq.setFrienduid(frienduid);
            accreq.setTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
            accreq.setInfo(mModel_crypto.getOwner().getInfo());
            accreq.setPub(mModel_crypto.getOwner().getlocalkey(Defin_crypto.SIGN).getPubkey());
            accreq.setsigndatalen(64);
            accreq.setsigndata(null);
            accreq.setInfolen(mModel_crypto.getOwner().getInfo().length());
            byte[]src=gson.toJson(accreq,Accreq.class).getBytes();
            byte[]sigd=new byte[64];
            gm_sm2_3.GM_SM2Sign(sigd,src,src.length,mModel_user.getUser().getUid().toCharArray() ,mModel_user.getUser().getUid().toCharArray().length ,mModel_crypto.getOwner().getlocalkey(Defin_crypto.SIGN).getPrikey());
            accreq.setsigndata(sigd);
            sendMessage(Util.sm2inc(gson.toJson(accreq,Accreq.class),remoteKey.getPubkey()),Defin_internet.accreq,frienduid);

        }
        private void sendRemoteKey(final String friuenduid) throws Exception {
            /*Runnable runnable=new Runnable() {
                @Override
                public void run() {

                }
            };
            Thread thread=new Thread(runnable);
            thread.start();*/
            try {
                for(LocalKey localKey:mModel_crypto.getOwner().getLocalkey()){
                    if(localKey.getType()==Defin_crypto.ROOT){
                        RemoteKey remoteKey=mModel_crypto.GenRemoteKey(localKey);
                        sendMessage(remoteKey.toJson(), Defin_internet.remotekey,friuenduid);
                        //Thread.sleep(1000);
                        Cert cert=mModel_crypto.GenCert(localKey);
                        Log.d("pushremotekey:",remoteKey.toJson()+cert.toJson());
                        sendMessage(cert.toJson(),Defin_internet.cert,friuenduid);
                        //Thread.sleep(1000);
                    }
                    if(localKey.getType()==Defin_crypto.SIGN){
                        RemoteKey remoteKey=mModel_crypto.GenRemoteKey(localKey);
                        sendMessage(remoteKey.toJson(),Defin_internet.remotekey,friuenduid);
                        //Thread.sleep(1000);
                        Cert cert=mModel_crypto.GenCert(localKey);
                        Log.d("pushremotekey:",remoteKey.toJson()+cert.toJson());
                        sendMessage(cert.toJson(),Defin_internet.cert,friuenduid);
                        //Thread.sleep(1000);
                    }
                    if(localKey.getType()==Defin_crypto.BIND){
                        RemoteKey remoteKey=mModel_crypto.GenRemoteKey(localKey);
                        sendMessage(remoteKey.toJson(),Defin_internet.remotekey,friuenduid);
                        //Thread.sleep(1000);
                        Cert cert=mModel_crypto.GenCert(localKey);
                        Log.d("pushremotekey:",remoteKey.toJson()+cert.toJson());
                        sendMessage(cert.toJson(),Defin_internet.cert,friuenduid);
                        //Thread.sleep(1000);
                    }
                }
                        /*
                        Thread.sleep(1000);*/
                Gson gson=new Gson();
                if(!mModel_crypto.getOwner().getLocalkey().isEmpty()){
                    StringBuilder hubs= new StringBuilder("[ ");
                    for(LocalHub localHub:mModel_crypto.getOwner().getLocalHub()) {
                        hubs.append(gson.toJson(localHub.genhub(), Hub.class)).append(",");
                    }
                    hubs = new StringBuilder(hubs.substring(0, hubs.length() - 1) + "]");
                    sendMessage(hubs.toString(),Defin_internet.remotehub,friuenduid);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        @JavascriptInterface
        public void acceptaccreq(String accreq_json,String frienduid){
            Gson gson=new Gson();
            Accreq accreq=gson.fromJson(accreq_json,Accreq.class);
            Accexp accexp=new Accexp();
            accexp.setAccreq(accreq);
            Calendar calendar= Calendar.getInstance();
            calendar.add(Calendar.MONTH,1);
            accexp.setAccendtime(String.valueOf(calendar.getTimeInMillis()));
            calendar.add(Calendar.DATE,Defin_crypto.time_shot);
            accexp.setAccendtime(String.valueOf(calendar.getTimeInMillis()));
            accexp.setInfo(mModel_crypto.getOwner().getInfo());
            accexp.setAccess(accreq.getAccsee());
            byte[] sign=null;
            for(LocalKey localKey:mModel_crypto.getOwner().getLocalkey()){
                if(localKey.getType()==Defin_crypto.ROOT)
                    accexp.setRootKey(localKey.getPubkey());
            }
            for(LocalKey localkey2:mModel_crypto.getOwner().getLocalkey()){
                if(localkey2.getType()==Defin_crypto.SIGN){
                    accexp.setRootKey(localkey2.getPubkey());
                    sign=localkey2.getPrikey();
                }
            }
            Tocken tocken=new Tocken();
            tocken.setAccexp(accexp);
            Gm_sm2_3 gm_sm2_3=Gm_sm2_3.getInstance();
            byte[] src=(gson.toJson(accexp,Accexp.class).getBytes());
            tocken.setUuid(frienduid+" "+(new Date().getTime()));
            byte[]signd=new byte[64];
            if(sign==null)throw new NoSuchElementException();
            gm_sm2_3.GM_SM2Sign(signd,src,src.length,mModel_user.getUser().getUsername().toCharArray(),mModel_user.getUser().getUsername().toCharArray().length,sign);
            tocken.setSigndata(signd);
            tocken.setSingdatalen(64);
            //TODO:添加发送令牌的代码
            String json=tocken.toJson();
            Log.d("tocken to friend",tocken.toJson());
            byte[]pub=null;
            for(RemoteKey remoteKey:mModel_crypto.getGuest(mModel_user.getUser().getFriend(frienduid).getGuestid()).getRemoteKey()){
                if(remoteKey.getType()==Defin_crypto.BIND){
                    pub=remoteKey.getPubkey();
                    break;
                }
            }
            if(pub==null)throw new NullPointerException();
            Friend friend=mModel_user.getUser().getFriend(frienduid);
            sendMessage(Util.sm2inc(json,pub),Defin_internet.tocken,friend.getFirend_uid());
            Dao_Tocken dao_tocken=Dao_Tocken.getInstance(sql);
            try {
                dao_tocken.InsertTocken(tocken);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @JavascriptInterface
        public void denide(String accreq_json,String frienduid){
            sendMessage(accreq_json,Defin_internet.accdenied,frienduid);
        }
        private void accreqact(Message message,Friend friend){
            Gson gson=new Gson();
            String json=Util.sm2dec(message.getMessage(),mModel_crypto.getOwner().getlocalkey(Defin_crypto.BIND).getPrikey());
            Accreq accreq=gson.fromJson(json,Accreq.class);
            List<LocalHub> localHubs=mModel_crypto.getOwner().getLocalHub();
            LocalHub localHub = null;
            for(LocalHub localHub1:localHubs){
                if(localHub1.getUuid().equals(accreq.getHubuuid())){
                    localHub=localHub1;
                    break;
                }

            }
            if(localHub==null)return;//这里暂时丢弃了该申请，未来返回申请错误的信息
            if(mModel_crypto.AccreqVerfi(accreq,friend)) {
                Log.d("accreq to vue",gson.toJson(accreq,Accreq.class));
                androidJSBridge("accreq",gson.toJson(accreq,Accreq.class));
/*
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("门禁授权申请");// 设置标题
                // builder.setIcon(R.drawable.ic_launcher);//设置图标
                builder.setMessage(friend.getFirend_uid()+"("+accreq.getInfo()+")"+"正在申请"+localHub.getId()+"的"+accreq.getAccsee()+"权限于北京时间"+accreq.getTime());// 为对话框设置内容
                // 为对话框设置取消按钮
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                    }
                });
                // 为对话框设置确定按钮
                final Friend finalFriend = friend;
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                                Accexp accexp=new Accexp();
                                accexp.setAccreq(accreq);
                                Calendar calendar= Calendar.getInstance();
                                calendar.add(Calendar.MONTH,1);
                                accexp.setAccendtime(String.valueOf(calendar.getTimeInMillis()));
                                calendar.add(Calendar.DATE,Defin_crypto.time_shot);
                                accexp.setAccendtime(String.valueOf(calendar.getTimeInMillis()));
                                accexp.setInfo(mModel_crypto.getOwner().getInfo());
                                accexp.setAccess(accreq.getAccsee());
                                byte[] sign=null;
                                for(LocalKey localKey:mModel_crypto.getOwner().getLocalkey()){
                                    if(localKey.getType()==Defin_crypto.ROOT)
                                        accexp.setRootKey(localKey.getPubkey());
                                }
                                for(LocalKey localkey2:mModel_crypto.getOwner().getLocalkey()){
                                    if(localkey2.getType()==Defin_crypto.SIGN){
                                        accexp.setRootKey(localkey2.getPubkey());
                                        sign=localkey2.getPrikey();
                                    }
                                }
                                Tocken tocken=new Tocken();
                                tocken.setAccexp(accexp);
                                Gm_sm2_3 gm_sm2_3=Gm_sm2_3.getInstance();
                                byte[] src=(gson.toJson(accexp,Accexp.class).getBytes());
                                tocken.setUuid(gm_sm2_3.sm3(src,src.length,new byte[32]).substring(0,16));
                                byte[]signd=new byte[32];
                                if(sign==null)throw new NoSuchElementException();
                                gm_sm2_3.GM_SM2Sign(signd,src,src.length,mModel_user.getUser().getUsername().toCharArray(),mModel_user.getUser().getUsername().toCharArray().length,sign);
                                tocken.setSigndata(signd);
                                tocken.setSingdatalen(64);
                                //TODO:添加发送令牌的代码
                                String json=gson.toJson(tocken,Tocken.class);
                                byte[] enc=Util.sm4inc(json.getBytes(),finalFriend.getSm4key(),finalFriend.getSm4iv());
                                sendMessage(Util.byteToHex(enc),Defin_internet.tocken,finalFriend.getFirend_uid());
                                Dao_Tocken dao_tocken=Dao_Tocken.getInstance(sql);
                                try {
                                    dao_tocken.InsertTocken(tocken);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                    }
                });
                builder.create().show();// 使用show()方法显示对话框
                */
            }
        }
        private void  messageapl(final Message[] messages){
            /*Runnable runnable=new Runnable() {
                @Override
                public void run() {

                }
            };
            Thread thread=new Thread(runnable);
            thread.start();*/
            Gson gson=new Gson();
            for (Message message : messages) {
                //message.setMessage(new String(Base64.decode(message.getMessage(),Base64.DEFAULT)));
                Friend friend = null;
                for (Friend friend1 : mModel_user.getUser().getFriendUidList()) {
                    if (friend1.getFirend_uid().equals(message.gethost_id())) {
                        friend1.getMessagehashList().add(message);
                        friend = friend1;
                    }
                }
                if (friend == null)
                    return;
                switch (message.getmsg_type()) {
                    case Defin_internet.friendreq: {
                        break;
                    }
                    case Defin_internet.friendapl: {
                        break;
                    }
                    case Defin_internet.accdenied:{
                        Accreq accreq=gson.fromJson(message.getMessage(),Accreq.class);
                        AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("申请授权失败")//标题
                                .setMessage("好友："+friend.getFirend_uid()+"\nhub:"+accreq.getHubuuid()+"\n描述："+accreq.getInfo()+"\n权限："+accreq.getAccsee())//内容
                                .setIcon(R.mipmap.ic_launcher)//图标
                                .create();
                        alertDialog1.show();
                    }
                    case Defin_internet.nego_req: {
                        byte[] pri = null;
                        for (LocalKey localKey : mModel_crypto.getOwner().getLocalkey()) {
                            if (localKey.getType() == Defin_crypto.BIND)
                                pri = localKey.getPrikey();
                        }
                        if(pri==null)break;
                        byte[] pub = null;
                        for (RemoteKey remoteKey : mModel_crypto.getGuest(friend.getGuestid()).getRemoteKey()){
                            if (remoteKey.getType() == Defin_crypto.BIND)
                                pub = remoteKey.getPubkey();
                        }
                        if(pub==null){
                            break;
                        }
                        String json=Util.sm2dec(message.getMessage(), pri);
                        Sm4ex sm4ex = gson.fromJson(json, Sm4ex.class);
                        //Sm4ex sm4ex=gson.fromJson(message.getMessage(),Sm4ex.class);
                        friend.setSm4key(sm4ex.getSm4k());
                        friend.setSm4iv(sm4ex.getSm4iv());
                        message.setmsg_type(Defin_internet.nego_apl);
                        message.setMessage(Util.sm2inc(sm4ex.toJson(), pub));//message.setMessage(Util.sm2inc(message.getMessage(),pub));
                        sendMessage(message.toJson(), Defin_internet.nego_apl, friend.getFirend_uid());
                        break;
                    }
                    case Defin_internet.nego_apl: {
                        byte[] pri = null;
                        for (LocalKey localKey : mModel_crypto.getOwner().getLocalkey()) {
                            if (localKey.getType() == Defin_crypto.BIND)
                                pri = localKey.getPrikey();
                        }
                        if(pri==null)break;
                        String json=Util.sm2dec(message.getMessage(), pri);
                        Log.d("negoreq",json);
                        Sm4ex sm4ex = gson.fromJson(json, Sm4ex.class);
                        //Sm4ex sm4ex=gson.fromJson(message.getMessage(),Sm4ex.class);
                        friend.setSm4key(sm4ex.getSm4k());
                        friend.setSm4iv(sm4ex.getSm4iv());
                        break;
                    }
                    case Defin_internet.remotekey: {
                        boolean flag=false;
                        Cert cert_=null;
                        Guest guest=mModel_crypto.getGuest(friend.getGuestid());
                        RemoteKey remoteKey=gson.fromJson(message.getMessage(), RemoteKey.class);
                        Log.d("remotekey",message.getMessage());
                        List<Cert> certs=guest.getCath_remotecert();
                        if(certs.isEmpty()){
                            Log.d("remotekey ","certs is null save to catch");
                            guest.getCatch_remotekey().add(remoteKey);
                            break;
                        }
                        for(Cert cert:certs){
                            if (mModel_crypto.VerifyRemoteKey(remoteKey,cert,mModel_user.getUser().getFriend(cert.getuuid()))) {
                                guest.getRemoteKey().add(remoteKey);
                                guest.getCerts().add(cert);
                                Log.d("remotecerify success",remoteKey.toJson()+cert.toJson());
                                flag=true;
                                cert_=cert;
                                break;
                            }
                        }
                        if(!flag){
                            Log.d("remotekey ","verify field save to catch");
                            guest.getCatch_remotekey().add(remoteKey);
                            break;
                        }
                        if(cert_!=null)
                            if(guest.getCath_remotecert().contains(cert_))
                                guest.getCath_remotecert().remove(cert_);
                        break;
                    }
                    case Defin_internet.cert: {
                        boolean flag=false;
                        RemoteKey remoteKey_=null;
                        Guest guest=mModel_crypto.getGuest(friend.getGuestid());
                        Log.d("message cert",message.toJson());
                        Cert cert=gson.fromJson(message.getMessage(), Cert.class);
                        Log.d("cert:",message.getMessage());
                        List<RemoteKey>remoteKeys=guest.getCatch_remotekey();
                        if(remoteKeys.isEmpty()){
                            Log.d("cert ","remotes is null save to catch");
                            guest.getCath_remotecert().add(cert);
                            break;
                        }
                        for(RemoteKey remoteKey:remoteKeys){
                            if (mModel_crypto.VerifyRemoteKey(remoteKey,cert,mModel_user.getUser().getFriend(cert.getuuid()))) {
                                guest.getRemoteKey().add(remoteKey);
                                guest.getCerts().add(cert);
                                Log.d("remotecerify success",remoteKey.toJson()+cert.toJson());
                                flag=true;
                                remoteKey_=remoteKey;
                                break;
                            }
                        }
                        if(!flag){
                            Log.d("cert ","verify field save to catch");
                            guest.getCath_remotecert().add(cert);
                            break;
                        }
                        if(remoteKey_ !=null)
                            if(guest.getCatch_remotekey().contains(remoteKey_))
                                guest.getCatch_remotekey().remove(remoteKey_);
                        break;
                    }
                    case Defin_internet.tocken: {
                        String mes = message.getMessage();
                        byte[]pri=null;
                        for(LocalKey localKey:mModel_crypto.getOwner().getLocalkey()){
                            if(localKey.getType()==Defin_crypto.BIND){
                                pri=localKey.getPrikey();
                                break;
                            }
                        }
                        if(pri==null)throw new NullPointerException();
                        String tocken_json=Util.sm2dec(mes,pri);
                        Log.d("acctocken",tocken_json);
                        Tocken tocken = gson.fromJson(tocken_json, Tocken.class);
                        if (mModel_crypto.TockenVerify(tocken, friend)) {
                            mModel_crypto.getGuest(friend.getGuestid()).getTockens().add(tocken);
                        }
                        break;
                    }
                    case Defin_internet.accreq: {
                        accreqact(message, friend);
                        break;
                    }
                    case Defin_internet.remotehub: {
                        Hub[] hub = gson.fromJson(message.getMessage(), Hub[].class);
                        mModel_crypto.getGuest(friend.getGuestid()).getHubs().addAll(Arrays.asList(hub));
                        break;
                    }
                    default:{break;}
                }
            }
        }
        @JavascriptInterface
        public void callAndroidMethod(int a, float b, String c, boolean d) {
            if (d) {
                String strMessage = "a+b+c=" + a + b + c;
                Log.d("jspcallback:",strMessage);
            }
        }
        @JavascriptInterface
        public void login(final String uid, final String passwd){
            Push push=new Push(Defin_internet.SeverAddress+Defin_internet.AppServerPort+Defin_internet.AppServerLogin, new PushCallBackListener() {
                @Override
                public void onPushSuccessfully(String data) {
                    Log.d("POST",data);
                    loginret=data;
                    load(uid, passwd);
                    androidJSBridge("signin_success");
                }

                @Override
                public void onPushFailed(int code,String message) {
                    //TODO：登陆失败的代码
                    Log.d("POST ERROR CODE", message+code);
                    loginret=code+message;
                    AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("登陆失败")//标题
                            .setMessage("状态码:"+message+"push代码："+code)//内容
                            .setIcon(R.mipmap.ic_launcher)//图标
                            .create();
                    alertDialog1.show();
                }
            });
            Login login=new Login();
            login.setPassword(passwd);
            login.setUid(uid);
            Log.d("login:",login.toJson());
            push.execute(login.toJson());
        }
        @JavascriptInterface
        public void signup(final String email, String uname, String passwd, final String phnum){
            final SignUp signUp=new SignUp(email,uname,passwd,phnum);
            Push push=new Push(Defin_internet.SeverAddress+Defin_internet.AppServerPort+Defin_internet.AppServerSignin, new PushCallBackListener() {
                @Override
                public void onPushSuccessfully(String data) throws InterruptedException {
                    Log.d("POST",data);
                    loginret=data;
                    //TODO:添加注册成功后需要执行的的代码
                    Defin_crypto.changeInfo(phnum);
                    mModel_crypto=new Model_Crypto(MainActivity.this,signUp);
                    mModel_user=new Model_User(MainActivity.this,signUp);
                    save();
                    androidJSBridge("signup_success");
                }
                @Override
                public void onPushFailed(int code,String message) {
                    //TODO：注册失败的代码
                    Log.d("POST ERROR CODE", message+code);
                    loginret=code+message;
                    AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("注册失败")//标题
                            .setMessage(loginret)//内容
                            .setIcon(R.mipmap.ic_launcher)//图标
                            .create();
                    alertDialog1.show();
                }
            });
            push.execute(signUp.toJson());
        }
        @JavascriptInterface
        public void pullmessage(String hostid, final String guestid){
            Push push=new Push(Defin_internet.SeverAddress+Defin_internet.AppServerPort+Defin_internet.AppServerGetMsg, new PushCallBackListener() {
                @Override
                public void onPushSuccessfully(String data) {
                    Gson gson=new Gson();
                    Log.d("data:",data);
                    Message[] messages=gson.fromJson(data,Message[].class);
                    //messageapl(messages);
                    for(Message message:messages) {
                        Friend friend = mModel_user.getUser().getFriend(message.gethost_id());
                        if (friend == null)
                            return;
                        message.setMessage(new String(Base64.decode(message.getMessage(),Base64.DEFAULT)));
                        if(message.getmsg_type()==Defin_internet.str) {
                            androidJSBridge("chatItem_receieve",message.getMessage());
                            friend.getMessagehashList().add(message);
                            Dao_Message dao_message=Dao_Message.getInstance(mModel_user.getMessageDataBase());
                            dao_message.InsertMessage(message);
                        }
                    }
                    messageapl(messages);
                    //TODO:添加查询成功后需要执行的的代码
                    save();
                    androidJSBridge("pullmessage_success");
                }

                @Override
                public void onPushFailed(int code,String message) {
                    //TODO：查询失败的代码
                    Log.d("POST ERROR CODE", message+code);
                    loginret=code+message;
                    AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("获取消息失败")//标题
                            .setMessage(loginret)//内容
                            .setIcon(R.mipmap.ic_launcher)//图标
                            .create();
                    alertDialog1.show();
                }
            });
            Log.d("pull:",hostid+guestid);
            PullMessage pullMessage=new PullMessage(hostid,guestid);
            Log.d("pullmessage:",pullMessage.toJson());
            push.execute(pullMessage.toJson());
        }
        @JavascriptInterface
        public void addFriend(final String friuenduid){
            final NewFriend newFriend=new NewFriend(mModel_user.getUser().getUid(),friuenduid);
            final Push push=new Push(Defin_internet.SeverAddress+Defin_internet.AppServerPort+Defin_internet.AppServerFindFriend, new PushCallBackListener() {
                @Override
                public void onPushSuccessfully(String data) {
                    Log.d("POSTDATA ",data);
                    loginret=data;
                    //TODO:添加注册成功后需要执行的的代码
                    Push push1=new Push(Defin_internet.SeverAddress + Defin_internet.AppServerPort + Defin_internet.AppServerQueryFriend, new PushCallBackListener() {
                        @Override
                        public void onPushSuccessfully(String data) throws Exception {
                            //pullmessage(friuenduid,mModel_user.getUser().getUid());
                            Log.d("POSTDATA ",data);
                            Gson gson=new Gson();
                            FriendQuery[] friendQuery=gson.fromJson(data,FriendQuery[].class);
                            Friend friend=new Friend(friendQuery[0].getFriend_uid(),friendQuery[0].getPhoneNum(),friendQuery[0].getUsername());
                            mModel_user.getUser().addFriend(friend);
                            Guest guest=new Guest();
                            guest.setDesc(friend.getPhnum());
                            guest.setId(friend.getUsername());
                            guest.setInfo(friend.getFirend_uid());
                            guest.setUuid(friend.getSM3str().substring(0,16));
                            friend.setGuestid(guest.getUuid());
                            mModel_crypto.getOwner().getGuests().add(guest);
                            sendRemoteKey(friuenduid);
                            Log.d("to vue:",friend.toJson());
                            androidJSBridge("addFriend_success",friend.toJson());
                            save();
                        }
                        @Override
                        public void onPushFailed(int code, String message) {

                        }
                    });
                    QueryFriend queryFriend=new QueryFriend();
                    queryFriend.setFriend_uid(friuenduid);
                    Log.d("queryfriend:",queryFriend.toJson());
                    push1.execute(queryFriend.toJson());
                }

                @Override
                public void onPushFailed(int code,String message) {
                    Log.d("POST ERROR CODE", message+code);
                    loginret=code+message;
                    AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("添加好友失败")//标题
                            .setMessage(loginret)//内容
                            .setIcon(R.mipmap.ic_launcher)//图标
                            .create();
                    alertDialog1.show();
                }
            });
            Log.d("addfriend:",newFriend.toJson());
            push.execute(newFriend.toJson());
        }
        @JavascriptInterface
        public void sendMessage(String mess,int type,String frienduid){
            SendMessage sendMessage=new SendMessage();
            sendMessage.setGuest_id(frienduid);
            sendMessage.setHost_id(mModel_user.getUser().getUid());
            sendMessage.setMessage(Base64.encodeToString(mess.getBytes(), Base64.DEFAULT));
            sendMessage.setMsg_type(type);
            Push push1=new Push(Defin_internet.SeverAddress + Defin_internet.AppServerPort + Defin_internet.AppServerSend, new PushCallBackListener() {
                @Override
                public void onPushSuccessfully(String data) {
                    Log.d("sendmessage:",data);
                }

                @Override
                public void onPushFailed(int code, String message) {
                    //TODO：查询失败的代码
                    Log.d("POST ERROR CODE", message+code);
                    AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("推送消息失败")//标题
                            .setMessage(message+code)//内容
                            .setIcon(R.mipmap.ic_launcher)//图标
                            .create();
                    alertDialog1.show();
                }
            });
            Log.d("push:",sendMessage.toJson());
            push1.execute(sendMessage.toJson());
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
            List<Friend> Friendlist=mModel_user.getUser().getFriendUidList();
            for(Friend ff:Friendlist){
                if(ff.getFirend_uid().equals(uid))
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
            if(mModel_user.getUser().getFriendUidList()==null||mModel_user.getUser().getFriendUidList().isEmpty()){
                Log.d("getfriend","faild");
                return null;
            }
            List<Friend> Friendlist=mModel_user.getUser().getFriendUidList();
            for(Friend ff:Friendlist){
                friends.append(ff.toJson()).append(",");
            }
            friends = friends.delete(friends.length()-1,friends.length());
            friends.append("]");
            Log.d("vue friends:",friends.toString());
            return friends.toString();
        }
        @JavascriptInterface
        public String getHub(String frienduid){
            for(Friend friend:mModel_user.getUser().getFriendUidList()){
                if(friend.getFirend_uid().equals(frienduid)){
                    Guest guest=mModel_crypto.getGuest(friend.getGuestid());
                    List<Hub> list=guest.getHubs();
                    if(list.isEmpty())return null;
                    StringBuffer hubs= new StringBuffer("[");
                    for(Hub hub:list){
                        hubs.append(hub.toJson()).append(",");
                    }
                    hubs=hubs.delete(hubs.length()-1,hubs.length());
                    hubs.append("]");
                    Log.d("hub to vue",hubs.toString());
                    return hubs.toString();
                }
            }
            return null;
        }
        @JavascriptInterface
        public String getTocken(String uuid) throws IOException {
            Dao_Tocken dao_tocken=Dao_Tocken.getInstance(sql);
            Tocken tocken=dao_tocken.SelectTocken(uuid);
            Gson gson=new Gson();
            return gson.toJson(tocken,tocken.getClass());
        }
        @JavascriptInterface
        public String getTockens() throws IOException {
            Dao_Tocken dao_tocken=Dao_Tocken.getInstance(sql);
            return dao_tocken.SelectTockens();
        }
        @JavascriptInterface
        public String getAudit(String uuid) throws IOException {
            Dao_Audit dao_audit=Dao_Audit.getInstance(sql);
            Audit audit=dao_audit.SelectAudit(uuid);
            Gson gson=new Gson();
            return gson.toJson(audit,Audit.class);
        }
        @JavascriptInterface
        public String getRemoteKey(String uuid) throws IOException {
            Dao_Remotekey dao_remotekey=Dao_Remotekey.getInstance(sql);
            RemoteKey remoteKey=dao_remotekey.SelectRemotekey(uuid);
            Gson gson=new Gson();
            return gson.toJson(remoteKey,RemoteKey.class);
        }
        @JavascriptInterface
        public String getAudits(){
            List<Audit>audits=mModel_crypto.getOwner().getAudits();
            if(audits.isEmpty())return null;
            StringBuffer ret=new StringBuffer("[");
            for(Audit ff:audits){
                ret.append(ff.toJson()).append(",");
            }
            ret = ret.delete(ret.length()-1,ret.length());
            ret.append("]");
            return ret.toString();
        }
    }
    @JavascriptInterface
    public void Reqacc(String frienduid){
        Friend friend=mModel_user.getUser().getFriend(frienduid);

    }
@JavascriptInterface
    public void deleteTocken(String tockenuuid) throws IOException {
    Dao_Tocken dao_tocken=Dao_Tocken.getInstance(sql);
    Tocken tocken=dao_tocken.SelectTocken(tockenuuid);
    dao_tocken.DeleteTocken(tockenuuid);
    mModel_crypto.getGuest(
            mModel_user.getUser()
                .getFriend(
                        tocken.getAccexp()
                                .getAccreq()
                                    .getFrienduid())
                    .getGuestid())
                        .getTockens()
                            .remove(tocken);
}



}
