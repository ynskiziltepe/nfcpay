package com.yunus.nfcpay.utilities;
/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  28.11.2020 - 22:48
 - Version:
 - File   : UtilAID.java
 - Description: AID list
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public class UtilAID {

    // Mastercard (PayPass)
    public static byte[] A0000000041010 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x04,
            (byte) 0x10,
            (byte) 0x10
    };

    // Maestro (PayPass)
    public static byte[] A0000000043060 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x04,
            (byte) 0x30,
            (byte) 0x60
    };


    // Visa (PayWave)
    public static byte[] A0000000031010 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x03,
            (byte) 0x10,
            (byte) 0x10
    };


    // Visa Electron (PayWave)
    public static byte[] A0000000032010 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x03,
            (byte) 0x20,
            (byte) 0x10
    };

    // American Express
    public static byte[] A0000000250000 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x25,
            (byte) 0x00,
            (byte) 0x00
    };

}
