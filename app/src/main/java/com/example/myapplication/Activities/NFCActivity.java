package com.example.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.Utils.Utils_nfc;

/*
    作者：zyc14588
    github地址:https://github.com/zyc14588
*/public class NFCActivity extends BaseActivity {
    private Utils_nfc utils_nfc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils_nfc=Utils_nfc.getInstance(this);

    }
    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils_nfc.mNfcAdapter.enableForegroundDispatch(this,Utils_nfc.mPendingIntent,Utils_nfc.mIntentFilter,Utils_nfc.mTechList);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //当该Activity接收到NFC标签时，运行该方法
        //调用工具方法，读取NFC数据

    }
    @Override
    protected void onPause() {

        super.onPause();
        Utils_nfc.mNfcAdapter.disableForegroundDispatch(this);
    }
}
