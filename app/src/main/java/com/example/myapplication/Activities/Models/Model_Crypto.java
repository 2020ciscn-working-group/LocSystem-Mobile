package com.example.myapplication.Activities.Models;

import android.app.Activity;

import com.example.myapplication.Activities.MainActivity;
import com.example.myapplication.Dao.Secret.Sql.AppSql;
import com.example.myapplication.DateStract.Guest;
import com.example.myapplication.DateStract.Hub;
import com.example.myapplication.DateStract.LocalKey;
import com.example.myapplication.DateStract.Owner;
import com.example.myapplication.Utils.ByteUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class Model_Crypto extends Model_Basic implements Serializable {
    private Owner mOwner;
    private LinkedList<Guest>mGuests;
    private AppSql mAppSql;

    Model_Crypto(Activity activity)  {
        mOwner=new Owner();
        mGuests= new LinkedList<>();
        mAppSql=new AppSql(activity);
    }

    @Override
    public byte[] getSM3() {
        return new byte[0];
    }
}
