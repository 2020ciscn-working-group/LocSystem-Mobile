package com.example.myapplication.Dao.Secret;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;

import com.example.myapplication.Activities.MainActivity;
import com.example.myapplication.Dao.Secret.Sql.AppSql;
import com.example.myapplication.DateStract.Guest;
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
*/public class Dao_Guest {
    private volatile static Dao_Guest sDao_guest;
    private volatile static AppSql     mDatabase;
    private Dao_Guest(){}
    public static Dao_Guest getInstance(AppSql sqLiteDatabase){
        if(sDao_guest==null){
            synchronized (Dao_Guest.class){
                if(sDao_guest==null)
                    sDao_guest=new Dao_Guest();
            }

        }
        if(sqLiteDatabase==null) {
            throw new Resources.NotFoundException();
        }
        mDatabase=sqLiteDatabase;
        return sDao_guest;
    }
    public void InsertGuest(Guest guest) throws Exception {
        String key= Util.byteToHex(SM4Utils.genersm4key());
        byte[] guest_byte= ByteUtils.objectToByteArray(guest);
        SM4Utils sm4=new SM4Utils();
        sm4.iv = "31313131313131313131313131313131";
        sm4.secretKey=key;
        sm4.hexString=true;
        byte[] guest_inc=sm4.encryptData_CBC(guest_byte);
        String path= MainActivity.path;
        String name="/Guest/"+ guest.getUuid();
        File Guest=new File(path,name);
        File dir= Guest.getParentFile();
        if(dir!=null&&!dir.exists())
            dir.mkdirs();
        if(!Guest.exists())
            try{
                Guest.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        //else throw new FileAlreadyExistsException("Tocken has existed!");
        FileOutputStream fileOutputStream;
        try{
            fileOutputStream=new FileOutputStream(Guest);
            fileOutputStream.write(guest_inc);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ContentValues values=new ContentValues();
        values.put("uuid", guest.getUuid());
        values.put("dekey",key);
        mDatabase.insert("Guest",values);
    }
    public Guest SelectGuest(String uuid) throws IOException {
        String[]selarg=new String[]{uuid};
        String key = null;
        Cursor cursor=mDatabase.query("Guest",new String[]{"dekey"},"uuid=?",selarg,null,null,null);
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
        String name="/Guest/"+uuid.substring(0,16);
        File Guest_file=new File(MainActivity.path+name);
        if(!Guest_file.exists())
            throw new FileNotFoundException();
        FileInputStream fileInputStream=new FileInputStream(Guest_file);
        byte[] Guest_inc=new byte[fileInputStream.available()];
        fileInputStream.read(Guest_inc);
        byte[]Guest_byte=sm4.decryptData_CBC(Guest_inc);
        return (Guest)ByteUtils.byteArrayToObject(Guest_byte);
    }
    public void DeleteGuest(String uuid) throws IOException {
        mDatabase.delete("Guest","uuid=?",new String[]{uuid});
        String name="/Guest/"+uuid.substring(0,16);
        File Guest_file=new File(MainActivity.path+name);
        if(!Guest_file.exists())
            throw new FileNotFoundException();
        Guest_file.delete();
    }
}
