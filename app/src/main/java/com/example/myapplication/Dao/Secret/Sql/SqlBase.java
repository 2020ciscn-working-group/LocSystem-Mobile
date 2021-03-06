package com.example.myapplication.Dao.Secret.Sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public abstract class SqlBase {
    private SQLiteDatabase mDatabase;

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    public void setDatabase(SQLiteDatabase database) {
        mDatabase = database;
    }

    public void insert(String tabname, ContentValues cValues){
        mDatabase.beginTransaction();
        try{
            mDatabase.insert(tabname,null,cValues);
            mDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            mDatabase.endTransaction();
        }
    }
    /*
    参数1  表名称
    参数2  删除条件
    参数3  删除条件值数组
     */
    public void delete(String tabname, String whereClause,String[] whereAtgs){
        mDatabase.beginTransaction();
        try{
            mDatabase.delete(tabname,whereClause,whereAtgs);
            mDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            mDatabase.endTransaction();
        }

    }


    /*
    参数1  表名称
    参数2  跟行列ContentValues类型的键值对Key-Value
    参数3  更新条件（where字句）
    参数4  更新条件数组
     */
    public void update(String table,ContentValues values,String  whereClause, String[]  whereArgs){
        mDatabase.beginTransaction();
        try{
            mDatabase.update(table,values,whereClause,whereArgs);
            mDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            mDatabase.endTransaction();
        }

    }

    /*
    参数table:表名称

    参数columns:列名称数组

    参数selection:条件字句，相当于where

    参数selectionArgs:条件字句，参数数组

    参数groupBy:分组列

    参数having:分组条件

    参数orderBy:排序列

    参数limit:分页查询限制

    参数Cursor:返回值，相当于结果集ResultSet
     */
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        mDatabase.beginTransaction();
        try{
            String[] arg=selectionArgs;
            Cursor cursor=mDatabase.query(table,columns,selection,arg,groupBy,having,orderBy,null);
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
            return cursor;
        } catch (Exception e) {
            e.printStackTrace();
            mDatabase.endTransaction();
        }

        return null;
    }
    /*
    参数tabname：要删除的表名
     */
    public void droptable(String tabname){
        mDatabase.beginTransaction();
        try{
            mDatabase.execSQL("DROP TABLE "+tabname);
            mDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            mDatabase.endTransaction();
        }
    }
}
