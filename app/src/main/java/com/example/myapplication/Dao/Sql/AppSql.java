package com.example.myapplication.Dao.Sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class AppSql extends SqlBase{
    private DatabaseHelper mDatabaseHelper;
    public AppSql(Context context){
        mDatabaseHelper=new DatabaseHelper(context);
        super.setDatabase(mDatabaseHelper.getWritableDatabase());;
    }
    public void onUpgrade(int version){
        mDatabaseHelper.onUpgrade(super.getDatabase(),DatabaseHelper.version,version);
    }

}
