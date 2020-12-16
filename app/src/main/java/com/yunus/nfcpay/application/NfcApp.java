package com.yunus.nfcpay.application;

import android.app.Application;
import android.content.Context;

public class NfcApp extends Application
{
    private static NfcApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static NfcApp getInstance() {
        return instance;
    }

    public Context getContext() {
        return this;
    }
}
