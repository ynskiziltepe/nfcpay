package com.yunus.nfcpay.utilities;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  01.12.2020 - 21:17
 - Version:
 - File   : UtilDateTime.java
 - Description: Utility of date time
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public final class UtilDateTime {
    private static final String TAG = UtilDateTime.class.getSimpleName();
/**-------------------------------------------------------------------------------------------------
                                <getDate>
 - Brief  : Function to get formatted date
 - Detail : Function to get formatted date
 - Parameters \
 -- <format>    : Format of date. Example:dd-MM-yyyy
 - Returns  \
 -- <dateFormat>: Formatted data. If fail function will return null
 --------------------------------------------------------------------------------------------------*/
    public static String getDate(String format) {
        Date date = new Date();

        SimpleDateFormat simpleDateFormat = null;
        try {
            simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        if (simpleDateFormat != null) {
            String dateFormat;
            dateFormat = simpleDateFormat.format(date);

            return dateFormat;
        }

        return "";
    }
/**-------------------------------------------------------------------------------------------------
                                <getTime>
 - Brief  : Function to get formatted time
 - Detail : Function to get formatted time
 - Parameters \
 -- <format>      : Format of time. Example:HH:mm:ss
 - Returns  \
 -- <timeFormat> : Formatted time. If fail function will return null
 --------------------------------------------------------------------------------------------------*/
    public static String getTime(String format) {
        Date date = new Date();
        SimpleDateFormat simpleTimeFormat = null;

        try {
            simpleTimeFormat = new SimpleDateFormat(format, Locale.getDefault());
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        if (simpleTimeFormat != null) {
            String timeFormat;
            timeFormat = simpleTimeFormat.format(date);

            return timeFormat;
        }

        return "";
    }

}

