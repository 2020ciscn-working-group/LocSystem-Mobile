package com.example.myapplication.Dao.Secret;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;

import com.example.myapplication.Activities.MainActivity;
import com.example.myapplication.Dao.Secret.Sql.AppSql;
import com.example.myapplication.DateStract.RemoteKey;
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
*/public class Dao_Remotekey {
    private volatile static Dao_Remotekey sDao_Remotekey;
    private volatile static AppSql    mDatabase;
    private Dao_Remotekey(){}
    public static Dao_Remotekey getInstance(AppSql sqLiteDatabase){
        if(sDao_Remotekey==null){
            synchronized (Dao_Remotekey.class){
                if(sDao_Remotekey==null)
                    sDao_Remotekey=new Dao_Remotekey();
            }

        }
        if(sqLiteDatabase==null) {
            throw new Resources.NotFoundException();
        }
        mDatabase=sqLiteDatabase;
        return sDao_Remotekey;
    }

    public void InsertRemotekey(RemoteKey remotekey) throws Exception {
        String key= Util.byteToHex(SM4Utils.genersm4key());
        byte[] Remotekey_byte= ByteUtils.objectToByteArray(remotekey);
        SM4Utils sm4=new SM4Utils();
        sm4.iv = "31313131313131313131313131313131";
        sm4.secretKey=key;
        sm4.hexString=true;
        byte[] Remotekey_inc=sm4.encryptData_CBC(Remotekey_byte);
        String path= MainActivity.path;
        String name="/Remotekey/"+ remotekey.getUuid();
        File Remotekey=new File(path,name);
        File dir=Remotekey.getParentFile();
        if(dir!=null&&!dir.exists())
            dir.mkdirs();
        if(!Remotekey.exists())
            try{
                Remotekey.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        //else throw new FileAlreadyExistsException("Remotekey has existed!");
        FileOutputStream fileOutputStream;
        try{
            fileOutputStream=new FileOutputStream(Remotekey);
            fileOutputStream.write(Remotekey_inc);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ContentValues values=new ContentValues();
        values.put("uuid", remotekey.getUuid());
        values.put("dekey",key);
        mDatabase.insert("Remotekey",values);
    }
    public RemoteKey SelectRemotekey(String uuid) throws IOException {
        String[]selarg=new String[]{uuid};
        String key = null;
        Cursor cursor=mDatabase.query("Remotekey",new String[]{"dekey"},"uuid=?",selarg,null,null,null);
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
        String name="/Remotekey/"+uuid.substring(0,16);
        File Remotekey_file=new File(MainActivity.path+name);
        if(!Remotekey_file.exists())
            throw new FileNotFoundException();
        FileInputStream fileInputStream=new FileInputStream(Remotekey_file);
        byte[] Remotekey_inc=new byte[fileInputStream.available()];
        fileInputStream.read(Remotekey_inc);
        byte[]Remotekey_byte=sm4.decryptData_CBC(Remotekey_inc);
        return (RemoteKey) ByteUtils.byteArrayToObject(Remotekey_byte);
    }
    public void DeleteRemotekey(String uuid) throws IOException {
        mDatabase.delete("Remotekey","uuid=?",new String[]{uuid});
        String name="/Remotekey/"+uuid.substring(0,16);
        File Remotekey_file=new File(MainActivity.path+name);
        if(!Remotekey_file.exists())
            throw new FileNotFoundException();
        Remotekey_file.delete();
    }
}
