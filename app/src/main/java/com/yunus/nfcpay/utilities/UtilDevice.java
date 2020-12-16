package com.yunus.nfcpay.utilities;

import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.yunus.nfcpay.application.NfcApp;

import java.io.File;

/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  06.12.2020 - 10:11
 - Version:
 - File   : UtilDevice.java
 - Description: Utility of device
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public final class UtilDevice {

/**-------------------------------------------------------------------------------------------------
                                    <isDeviceRooted>
 - Brief  : Function to check device root status
 - Detail : Function to check device root status
 - Parameters \
 - Returns  \
 -- <true - false> : Root status
 --------------------------------------------------------------------------------------------------*/
    public static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/",
                "/system/sbin/", "/usr/bin/", "/vendor/bin/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }
/**-------------------------------------------------------------------------------------------------
                                    <isAdbEnabled>
 - Brief  : Function to check device adb enable status
 - Detail : Function to check device adb enable status
 - Parameters \
 - Returns  \
 -- <true - false> : ADB enable status
 --------------------------------------------------------------------------------------------------*/
    public static boolean isAdbEnabled() {
        return Settings.Secure.getInt(
                NfcApp.getInstance().getContentResolver(),
                Settings.Global.ADB_ENABLED, 0
        ) > 0;
    }
/**-------------------------------------------------------------------------------------------------
                                <isEmulator>
 - Brief  : Function to check emulator
 - Detail : Function to check emulator
 - Parameters \
 - Returns  \
 -- <true - false> : Emulator status
 --------------------------------------------------------------------------------------------------*/
    public static boolean isEmulator() {
        boolean checkProperty = Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
        if (checkProperty) return true;

        String operatorName = "";
        TelephonyManager tm = (TelephonyManager) NfcApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            String name = tm.getNetworkOperatorName();
            if (name != null) {
                operatorName = name;
            }
        }
        boolean checkOperatorName = operatorName.toLowerCase().equals("android");
        if (checkOperatorName) return true;

        String url = "tel:" + "123456";
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_DIAL);
        boolean checkDial = intent.resolveActivity(NfcApp.getInstance().getPackageManager()) == null;
        if (checkDial) return true;

        return false;
    }

}
