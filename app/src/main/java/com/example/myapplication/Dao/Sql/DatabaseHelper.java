package com.example.myapplication.Dao.Sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 *作者：zyc14588
 *github地址：https://github.com/zyc14588
 */public class DatabaseHelper extends SQLiteOpenHelper {
     private static final String DB_NAME="LocSystem.db";
     private static int version=1;
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DatabaseHelper(Context context){
        super(context,DB_NAME,null,version);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库sql语句 并 执行
        String sql = "create table user(name varchar(20))";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
 }
