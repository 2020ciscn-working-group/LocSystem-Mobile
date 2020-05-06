package com.example.myapplication.Dao.Internet.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.Dao.Secret.Sql.DatabaseHelper;

import java.io.Serializable;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class MessageDataBaseHelper extends DatabaseHelper implements Serializable {
    private static final String DB_NAME="LocSystem.db";
    public static int version=1;


    public MessageDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public MessageDataBaseHelper(Context context){
        super(context,DB_NAME,null,version);

    }
    @Override
    //数据加密后通过文件形式存储，数据库存sm4密钥
    public void onCreate(SQLiteDatabase db) {
        //创建数据库sql语句 并 执行
        String sql = "";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
    public boolean tabIsExist(String tabName){
        boolean result = false;
        if(tabName == null){
            return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();//此this是继承SQLiteOpenHelper类得到的
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tabName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if(cursor.moveToNext()){
                int count = cursor.getInt(0);
                if(count>0){
                    result = true;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

}
