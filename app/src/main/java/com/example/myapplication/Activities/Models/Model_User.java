package com.example.myapplication.Activities.Models;

import android.app.Activity;
import android.app.AlertDialog;
import android.se.omapi.Session;
import android.util.Log;

import com.example.myapplication.Activities.MainActivity;
import com.example.myapplication.Activities.Models.Internet.Friend;
import com.example.myapplication.Activities.Models.Internet.Message;
import com.example.myapplication.Activities.Models.Internet.SendMessage;
import com.example.myapplication.Activities.Models.Internet.SignUp;
import com.example.myapplication.Activities.Models.Internet.User;
import com.example.myapplication.Activities.Models.thread.Push;
import com.example.myapplication.Dao.Internet.sql.MessageDataBase;
import com.example.myapplication.DateStract.Tocken;
import com.example.myapplication.Defin.Defin_internet;
import com.example.myapplication.Interfaces.PushCallBackListener;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Gm_sm2_3;

import java.io.Serializable;
import java.util.LinkedList;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Model_User extends Model_Basic  {
    private String             UUID=null;
    private User               mUser;
    private MessageDataBase    mMessageDataBase;
    private Activity           mActivity;
    private Session mSession;

    public Model_User(Activity activity, SignUp signUp){
        mActivity=activity;
        mMessageDataBase=new MessageDataBase(mActivity);
        Model_userInit(signUp);
    }
    public Model_User(Activity activity, User user){
        mActivity=activity;
        mMessageDataBase=new MessageDataBase(mActivity);
        mUser=user;
    }

    public MessageDataBase getMessageDataBase() {
        return mMessageDataBase;
    }

    @Override
    public byte[] getSM3() {
        Gm_sm2_3 gm_sm2_3=Gm_sm2_3.getInstance();

        return new byte[0];
    }

    public Session getSession() {
        return mSession;
    }

    public void setSession(Session session) {
        mSession = session;
    }

    public final String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public final User getUser() {
        return mUser;
    }

    public void setUser(User users) {
        mUser = users;
    }

    public void addFriend(Friend friend){
        if(!mUser.getFriendUidList().contains(friend))mUser.getFriendUidList().add(friend);
    }

    public void sendMessage( String friuenduid,String mes,int type){
        Message message=new Message();
        message.setguest_id(friuenduid);
        message.sethost_id(UUID);
        message.setMessage(mes);
        message.setmsg_type(type);
        Push push=new Push(Defin_internet.SeverAddress+Defin_internet.AppServerPort+Defin_internet.AppServerSend, new PushCallBackListener() {
            @Override
            public void onPushSuccessfully(String data) {
                Log.d("POST",data);
                //TODO:添加注册成功后需要执行的的代码
            }

            @Override
            public void onPushFailed(int code,String message) {
                //TODO：注册失败的代码
                Log.d("POST ERROR CODE", message+code);
                String loginret=code+message;
                AlertDialog alertDialog1 = new AlertDialog.Builder(mActivity)
                        .setTitle("消息发送失败")//标题
                        .setMessage(loginret)//内容
                        .setIcon(R.mipmap.ic_launcher)//图标
                        .create();
                alertDialog1.show();
            }
        });
        push.execute(message.toJson());

    }
    private void Model_userInit(SignUp signUp){
        Gm_sm2_3 gm_sm2_3=Gm_sm2_3.getInstance();
        byte[] src=(signUp.getUid()+signUp.getUsername()+signUp.getPassword()+signUp.getPhoneNum()).getBytes();
        UUID=gm_sm2_3.sm3(src,src.length,new byte[32]);
        mUser=new User(signUp.getUid(),signUp.getUsername(),signUp.getPassword(),signUp.getPhoneNum());
    }
}
