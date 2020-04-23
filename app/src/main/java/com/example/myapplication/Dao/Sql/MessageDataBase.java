package com.example.myapplication.Dao.Sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class MessageDataBase extends SqlBase {
    private MessageDataBaseHelper mMessageDataBaseHelper;
    public MessageDataBase(Context context){
        mMessageDataBaseHelper=new MessageDataBaseHelper(context);
        super.setDatabase(mMessageDataBaseHelper.getWritableDatabase());;
    }
    public void onUpgrade(int version){
        mMessageDataBaseHelper.onUpgrade(super.getDatabase(),DatabaseHelper.version,version);
    }
}
