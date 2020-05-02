package com.example.myapplication.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.myapplication.Activities.Models.Internet.PullMessage;
import com.example.myapplication.Activities.Models.Internet.SendMessage;
import com.example.myapplication.Activities.Models.Internet.SignUp;
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
import com.example.myapplication.DateStract.Cert;
import com.example.myapplication.DateStract.Guest;
import com.example.myapplication.DateStract.Hub;
import com.example.myapplication.DateStract.LocalKey;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
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
//TODO:将USB数据交换activity换成serve

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
        accexp.setRootKey(pik_pub);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();

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
        @JavascriptInterface
        public void login(final String uid, String passwd){
            Push push=new Push(Defin_internet.SeverAddress+Defin_internet.AppServerPort+Defin_internet.AppServerLogin, new PushCallBackListener() {
                @Override
                public void onPushSuccessfully(String data) {
                    Log.d("POST",data);
                    loginret=data;
                    //TODO:添加登陆成功后需要执行的的代码
                    String name_c="/Model/Crypto/"+uid;
                    String name_u="/Model/User/"+uid;
                    File Model_Crypto_file=new File(MainActivity.path+name_c);
                    File Model_user_file=new File(MainActivity.path+name_u);
                    try {
                        FileInputStream fileInputStream = new FileInputStream(Model_Crypto_file);
                        byte[] Model_Crypto_bytes=new byte[fileInputStream.available()];
                        fileInputStream.read(Model_Crypto_bytes);
                        mModel_crypto=(Model_Crypto) ByteUtils.byteArrayToObject(Model_Crypto_bytes);

                        fileInputStream=new FileInputStream(Model_user_file);
                        byte[] Model_User_bytes=new byte[fileInputStream.available()];
                        fileInputStream.read(Model_User_bytes);
                        mModel_user=(Model_User)ByteUtils.byteArrayToObject(Model_User_bytes);
                        androidJSBridge("login_success");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    androidJSBridge("login_success");
                }

                @Override
                public void onPushFailed(int code,String message) {
                    //TODO：登陆失败的代码
                    Log.d("POST ERROR CODE", message+code);
                    loginret=code+message;
                    AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("登陆失败")//标题
                            .setMessage(loginret)//内容
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
        public void signup(final String email, String uname, String passwd, String phnum){
            final SignUp signUp=new SignUp(email,uname,passwd,phnum);
            Push push=new Push(Defin_internet.SeverAddress+Defin_internet.AppServerPort+Defin_internet.AppServerLogin, new PushCallBackListener() {
                @Override
                public void onPushSuccessfully(String data) {
                    Log.d("POST",data);
                    loginret=data;
                    //TODO:添加注册成功后需要执行的的代码
                    Defin_crypto.changeInfo(email);
                    mModel_crypto=new Model_Crypto(MainActivity.this,signUp);
                    mModel_user=new Model_User(MainActivity.this,signUp);
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
        public void pullmessage(String hostid,String guestid){
            PullMessage pullMessage=new PullMessage(hostid,guestid);
            Push push=new Push(Defin_internet.SeverAddress+Defin_internet.AppServerPort+Defin_internet.AppServerLogin, new PushCallBackListener() {
                @Override
                public void onPushSuccessfully(String data) {
                    Log.d("POST",data);
                    final Gson gson=new Gson();
                    Message message=gson.fromJson(data,Message.class);
                    Friend friend=null;
                    for(Friend friend1:mModel_user.getUser().getFriendUidList()){
                        if(friend1.getFirend_uid().equals(message.gethost_id())){
                            friend1.getMessagehashList().add(message);
                            friend=friend1;
                        }
                    }
                    if(friend==null&&message.getmsg_type()==Defin_internet.friendreq)
                        return;
                    switch (message.getmsg_type()){
                        case Defin_internet.friendreq:{
                            break;
                        }
                        case Defin_internet.str:{
                            break;
                        }
                        case Defin_internet.nego_req:{
                            break;
                        }
                        case Defin_internet.nego_apl:{
                            break;
                        }
                        case Defin_internet.remotekey:{
                            List<Guest> guests=mModel_crypto.getOwner().getGuests();
                            for(Guest guest1:guests){
                                if(guest1.getUuid().equals(friend.getGuestid())) {
                                    guest1.getRemoteKey().add(gson.fromJson(message.getMessage(),RemoteKey.class));
                                }
                            }
                            break;
                        }
                        case Defin_internet.cert:{
                            List<Guest> guests=mModel_crypto.getOwner().getGuests();
                            for(Guest guest1:guests){
                                if(guest1.getUuid().equals(friend.getGuestid())) {
                                    guest1.getCerts().add(gson.fromJson(message.getMessage(), Cert.class));
                                }
                            }
                            break;
                        }
                        case Defin_internet.tocken:{
                            break;
                        }
                        case Defin_internet.accreq:{
                            final Accreq accreq=gson.fromJson(message.getMessage(),Accreq.class);
                            for(final Guest guest1:mModel_crypto.getOwner().getGuests()){
                                if(guest1.getUuid().equals(friend.getGuestid())) {
                                    for(RemoteKey remoteKey:guest1.getRemoteKey()){
                                        if(remoteKey.getType()==Defin_crypto.SIGN)
                                            if(mModel_crypto.AccereqVerify(accreq,remoteKey)) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                builder.setTitle("门禁授权申请");// 设置标题
                                                // builder.setIcon(R.drawable.ic_launcher);//设置图标
                                                builder.setMessage(friend.getFirend_uid()+"("+accreq.getInfo()+")"+"正在申请"+accreq.getAccsee()+"于北京时间"+accreq.getTime());// 为对话框设置内容
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
                                                       SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
                                                       accexp.setAccendtime(dateFormat.format(calendar.getTime()));
                                                       calendar.add(Calendar.DATE,Defin_crypto.time_shot);
                                                       accexp.setAccendtime(dateFormat.format(calendar.getTime()));
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
                                                        SendMessage sendMessage=new SendMessage();
                                                        sendMessage.setGuest_id( finalFriend.getFirend_uid());
                                                        sendMessage.setHost_id(mModel_user.getUUID());
                                                        sendMessage.setMessage(gson.toJson(tocken,Tocken.class));
                                                        sendMessage.setMsg_type(Defin_internet.tocken);
                                                        Push push1=new Push(Defin_internet.SeverAddress + Defin_internet.AppServerPort + Defin_internet.AppServerSend, new PushCallBackListener() {
                                                            @Override
                                                            public void onPushSuccessfully(String data) {

                                                            }

                                                            @Override
                                                            public void onPushFailed(int code, String message) {
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
                                                        push1.execute(sendMessage.toJson());
                                                       Dao_Tocken dao_tocken=Dao_Tocken.getInstance(sql);
                                                        try {
                                                            dao_tocken.InsertTocken(tocken);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                                builder.create().show();// 使用show()方法显示对话框
                                            }
                                    }
                                }
                            }
                            break;
                        }
                    }
                    //TODO:添加查询成功后需要执行的的代码
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
            push.execute(pullMessage.toJson());
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
            List<Friend> Friendlist=mModel_user.getUser().getFriendUidList();
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
        @JavascriptInterface
        public String getAudit(){
            List<Audit>audits=mModel_crypto.getOwner().getAudits();
            StringBuffer ret=new StringBuffer("[");
            for(Audit ff:audits){
                ret.append(ff.toJson()).append(",");
            }
            ret = new StringBuffer(ret.substring(0, ret.length() - 1));
            ret.append("]");
            return ret.toString();
        }
    }
    @JavascriptInterface
    public void Reqacc(String frienduid){
        Friend friend=mModel_user.getUser().getFriend(frienduid);

    }



}
