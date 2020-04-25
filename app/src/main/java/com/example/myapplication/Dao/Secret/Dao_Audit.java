package com.example.myapplication.Dao.Secret;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;

import com.example.myapplication.Activities.MainActivity;
import com.example.myapplication.Dao.Secret.Sql.AppSql;
import com.example.myapplication.DateStract.Audit;
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
*/public class Dao_Audit {
    private volatile static Dao_Audit sDao_Audit;
    private volatile static AppSql     mDatabase;
    private Dao_Audit(){}
    public static Dao_Audit getInstance(AppSql sqLiteDatabase){
        if(sDao_Audit==null){
            synchronized (Dao_Audit.class){
                if(sDao_Audit==null)
                    sDao_Audit=new Dao_Audit();
            }

        }
        if(sqLiteDatabase==null) {
            throw new Resources.NotFoundException();
        }
        mDatabase=sqLiteDatabase;
        return sDao_Audit;
    }

    public void InsertAudit(Audit audit) throws Exception {
        String key= Util.byteToHex(SM4Utils.genersm4key());
        byte[] Audit_byte= ByteUtils.objectToByteArray(audit);
        SM4Utils sm4=new SM4Utils();
        sm4.iv = "31313131313131313131313131313131";
        sm4.secretKey=key;
        sm4.hexString=true;
        byte[] Audit_inc=sm4.encryptData_CBC(Audit_byte);
        String path= MainActivity.path;
        String name="/Audit/"+ audit.getUuid();
        File Audit=new File(path,name);
        File dir=Audit.getParentFile();
        if(dir!=null&&!dir.exists())
            dir.mkdirs();
        if(!Audit.exists())
            try{
                Audit.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        //else throw new FileAlreadyExistsException("Audit has existed!");
        FileOutputStream fileOutputStream;
        try{
            fileOutputStream=new FileOutputStream(Audit);
            fileOutputStream.write(Audit_inc);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ContentValues values=new ContentValues();
        values.put("uuid", audit.getUuid());
        values.put("dekey",key);
        mDatabase.insert("Audit",values);
    }
    public Audit SelectAudit(String uuid) throws IOException {
        String[]selarg=new String[]{uuid};
        String key = null;
        Cursor cursor=mDatabase.query("Audit",new String[]{"dekey"},"uuid=?",selarg,null,null,null);
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
        String name="/Audit/"+uuid.substring(0,16);
        File Audit_file=new File(MainActivity.path+name);
        if(!Audit_file.exists())
            throw new FileNotFoundException();
        FileInputStream fileInputStream=new FileInputStream(Audit_file);
        byte[] Audit_inc=new byte[fileInputStream.available()];
        fileInputStream.read(Audit_inc);
        byte[]Audit_byte=sm4.decryptData_CBC(Audit_inc);
        return (Audit) ByteUtils.byteArrayToObject(Audit_byte);
    }
    public void DeleteAudit(String uuid) throws IOException {
        mDatabase.delete("Audit","uuid=?",new String[]{uuid});
        String name="/Audit/"+uuid.substring(0,16);
        File Audit_file=new File(MainActivity.path+name);
        if(!Audit_file.exists())
            throw new FileNotFoundException();
        Audit_file.delete();
    }
}
