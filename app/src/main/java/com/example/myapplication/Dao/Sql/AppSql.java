package com.example.myapplication.Dao.Sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class AppSql {
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabaseHelper;
    public AppSql(Context context){
        mDatabaseHelper=new DatabaseHelper(context);
        mDatabase=mDatabaseHelper.getWritableDatabase();
    }
}
