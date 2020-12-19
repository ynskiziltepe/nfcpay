package com.yunus.nfcpay;

import com.yunus.nfcpay.model.AFL;
import java.security.SecureRandom;
import java.util.ArrayList;

/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  20.11.2020 - 19:40
 - Version:
 - File   : EMVNative.java
 - Description: Clas purpose is bridge between Java < -- >  NDK/JNI
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public class EMVNative {

    // Used to load the 'emvlib' library on application startup.
    static {
        System.loadLibrary("emvlib");
    }
/**-------------------------------------------------------------------------------------------------
                            <getSecureRandom>
 - Brief  : Function will create random data securely
 - Detail : Function will create random data securely
 - Parameters \
    -- <len>      : length of random data
 - Returns  \
    -- <randData> : created random data
 --------------------------------------------------------------------------------------------------*/
    public static byte[] getSecureRandom(int len){
        byte[] randData = new byte[len];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randData);

        return randData;
    }

    public static native byte[] getSW(byte [] data);
    public static native boolean checkEMVResponse(byte[] emvData);
    public static native byte[] getTlvValue(byte[] dataBytes, int emvTag);
    public static native ArrayList<byte[]> getAidList(byte[] dataBytes);
    public static native boolean checkDOL(byte[] dol, int tag);
    public static native byte[] prepareDOL(byte[] dol, int dolTag);
    public static native ArrayList<AFL> getAFLDataRecords(byte[] aflData);
}
