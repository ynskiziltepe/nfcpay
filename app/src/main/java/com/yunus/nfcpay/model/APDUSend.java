package com.yunus.nfcpay.model;
/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  28.11.2020 - 11:05
 - Version:
 - File   : APDUSend.java
 - Description: Application Protocol Data Unit (APDU) send pojo
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public class APDUSend {

    public APDUSend() {

    }

    public static byte[] getApduBytes(int command, int p1p2, int lc, byte[] dataIn) {
        int index = 0;
        byte[] apdu = new byte[1 + 1 + 1 + 1 + 1 + + 1 + lc];
        apdu[index++] = (byte)(command / 0x100); // cla
        apdu[index++] = (byte)(command % 0x100); //ins
        apdu[index++] = (byte)(p1p2 / 0x100); //p1
        apdu[index++] = (byte)(p1p2 % 0x100); //p2
        apdu[index++] = (byte)lc;
        System.arraycopy(dataIn, 0, apdu, index, lc);
        index += dataIn.length;
        apdu[index] = 0; //le

        return apdu;
    }
}
