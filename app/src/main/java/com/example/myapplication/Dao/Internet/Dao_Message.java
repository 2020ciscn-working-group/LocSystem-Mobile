package com.example.myapplication.Dao.Internet;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;

import com.example.myapplication.Activities.Models.Internet.Message;
import com.example.myapplication.Dao.Secret.Dao_Tocken;
import com.example.myapplication.Dao.Internet.sql.MessageDataBase;

import java.util.NoSuchElementException;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Dao_Message {
    private volatile static Dao_Message      sDao_message;
    private volatile static MessageDataBase mDatabase;
    private Dao_Message(){}
    public static Dao_Message getInstance(MessageDataBase sqLiteDatabase){
        if(sDao_message==null){
            synchronized (Dao_Tocken.class){
                if(sDao_message==null)
                    sDao_message=new Dao_Message();
            }

        }
        if(sqLiteDatabase==null) {
            throw new Resources.NotFoundException();
        }
        mDatabase=sqLiteDatabase;
        return sDao_message;
    }
    public void InsertMessage(Message message){
        ContentValues values=new ContentValues();
        values.put("hash", message.getHash());
        values.put("frienduid",message.getFrienduid());
        values.put("date",message.getDate());
        values.put("message",message.getMessage());
        mDatabase.insert("Message",values);
    }
    public Message SelectMessage(String hash){
        String frienduid;
        String date;
        String message;
        Cursor cursor=mDatabase.query("Message",new String[]{"hash"},"hash=?",new String[]{hash},null,null,null);
        boolean fist=cursor.moveToFirst();
        boolean isEmpty=cursor.getCount()==0;
        if(fist && !isEmpty){
            frienduid=cursor.getString(cursor.getColumnIndex("frienduid"));
            date=cursor.getString(cursor.getColumnIndex("date"));
            message=cursor.getString(cursor.getColumnIndex("message"));
        }
        else throw new NoSuchElementException();
        return new Message(hash,frienduid,date,message);
    }
    public void DeleteMessage(String hash){
        mDatabase.delete("Message","hash=?",new String[]{hash});
    }
}
