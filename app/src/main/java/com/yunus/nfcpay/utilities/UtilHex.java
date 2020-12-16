package com.yunus.nfcpay.utilities;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  06.12.2020 - 13:55
 - Version:
 - File   : UtilHex.java
 - Description: Utility of hex data
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public class UtilHex {
    private static final String TAG = UtilHex.class.getName();
/**-------------------------------------------------------------------------------------------------
                                    <bytes2Hex>
 - Brief  : Function to convert bytes to hexadecimal
 - Detail : Function to convert bytes to hexadecimal
 - Parameters \
    -- <inputData> data to be converted hex data
 - Returns  \
 -- <hexData> : Converted hex data
 --------------------------------------------------------------------------------------------------*/
    public static String bytes2Hex(@NonNull byte[] inputData) {

        String hexData;

        StringBuilder stringBuilder = new StringBuilder();

        for (byte byteOut : inputData) {
            try {
                stringBuilder.append(String.format("%02X", byteOut));
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                Log.e(TAG, e.toString());

                e.printStackTrace();
            }
        }

        hexData = stringBuilder.toString();

        return hexData;
    }
/**-------------------------------------------------------------------------------------------------
                                <hex2Bytes>
 - Brief  : Function to convert hex data to bytes
 - Detail : Function to convert hex data to bytes
 - Parameters \
    --<hexData> data to be converted to bytes
 - Returns  \
 -- <byteData> : Converted bytes data
 --------------------------------------------------------------------------------------------------*/
    public static byte[] hex2Bytes(@NonNull String hexData) {

        byte[] byteData = null;

        try {
            byteData = new byte[hexData.length() / 2];
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Log.e(TAG, e.toString());

            e.printStackTrace();
        }

        if (byteData != null) {
            for (int i = 0; i < hexData.length(); i += 2) {
                try {
                    byteData[i / 2] = (byte) ((Character.digit(hexData.charAt(i), 16) << 4) + Character.digit(hexData.charAt(i + 1), 16));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    Log.e(TAG, e.toString());

                    e.printStackTrace();
                }
            }
        }

        return byteData;
    }
}
