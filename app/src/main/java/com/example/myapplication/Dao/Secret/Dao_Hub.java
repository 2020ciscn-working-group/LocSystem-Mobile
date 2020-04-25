package com.example.myapplication.Dao.Secret;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;

import com.example.myapplication.Activities.MainActivity;
import com.example.myapplication.Dao.Secret.Sql.AppSql;
import com.example.myapplication.DateStract.Hub;
import com.example.myapplication.Utils.ByteUtils;
import com.example.myapplication.Utils.Util;
import com.example.myapplication.Utils.utils.sm4.SM4Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Dao_Hub {
    private volatile static Dao_Hub sDao_Hub;
    private volatile static AppSql        mDatabase;
    private Dao_Hub(){}
    public static Dao_Hub getInstance(AppSql sqLiteDatabase){
        if(sDao_Hub==null){
            synchronized (Dao_Hub.class){
                if(sDao_Hub==null)
                    sDao_Hub=new Dao_Hub();
            }

        }
        if(sqLiteDatabase==null) {
            throw new Resources.NotFoundException();
        }
        mDatabase=sqLiteDatabase;
        return sDao_Hub;
    }

    public void InsertHub(Hub hub) throws Exception {
        String key= Util.byteToHex(SM4Utils.genersm4key());
        byte[] Hub_byte= ByteUtils.objectToByteArray(hub);
        SM4Utils sm4=new SM4Utils();
        sm4.iv = "31313131313131313131313131313131";
        sm4.secretKey=key;
        sm4.hexString=true;
        byte[] Hub_inc=sm4.encryptData_CBC(Hub_byte);
        String path= MainActivity.path;
        String name="/Hub/"+ hub.getUuid();
        File Hub=new File(path,name);
        File dir=Hub.getParentFile();
        if(dir!=null&&!dir.exists())
            dir.mkdirs();
        if(!Hub.exists())
            try{
                Hub.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        //else throw new FileAlreadyExistsException("Hub has existed!");
        FileOutputStream fileOutputStream;
        try{
            fileOutputStream=new FileOutputStream(Hub);
            fileOutputStream.write(Hub_inc);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ContentValues values=new ContentValues();
        values.put("uuid", hub.getUuid());
        values.put("dekey",key);
        mDatabase.insert("Hub",values);
    }
    public Hub SelectHub(String uuid) throws IOException {
        String[]selarg=new String[]{uuid};
        String key = null;
        Cursor cursor=mDatabase.query("Hub",new String[]{"dekey"},"uuid=?",selarg,null,null,null);
        boolean fist=cursor.moveToFirst();
        boolean isEmpty=cursor.getCount()==0;
        if(cursor!=null&&fist&&!isEmpty){
            key=cursor.getString(cursor.getColumnIndex("dekey"));
        }
        else throw new NoSuchElementException();
        cursor.close();
        SM4Utils sm4=new SM4Utils();
        sm4.iv = "31313131313131313131313131313131";
        sm4.secretKey=key;
        sm4.hexString=true;
        String name="/Hub/"+uuid.substring(0,16);
        File Hub_file=new File(MainActivity.path+name);
        if(!Hub_file.exists())
            throw new FileNotFoundException();
        FileInputStream fileInputStream=new FileInputStream(Hub_file);
        byte[] Hub_inc=new byte[fileInputStream.available()];
        fileInputStream.read(Hub_inc);
        byte[]Hub_byte=sm4.decryptData_CBC(Hub_inc);
        return (Hub) ByteUtils.byteArrayToObject(Hub_byte);
    }
    public void DeleteHub(String uuid) throws IOException {
        mDatabase.delete("Hub","uuid=?",new String[]{uuid});
        String name="/Hub/"+uuid.substring(0,16);
        File Hub_file=new File(MainActivity.path+name);
        if(!Hub_file.exists())
            throw new FileNotFoundException();
        Hub_file.delete();
    }
}
