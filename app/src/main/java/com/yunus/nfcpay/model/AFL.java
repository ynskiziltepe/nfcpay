package com.yunus.nfcpay.model;
/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  28.11.2020 - 11:05
 - Version:
 - File   : AFL.java
 - Description: Application File Locator (AFL) pojo
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public class AFL {
    private int sfi;
    private int recordNum;
    private byte[] readCmd;

    public AFL(){ }

    public AFL(int sfi, int recordNum, byte[] readCmd){
        this.sfi =sfi;
        this.recordNum = recordNum;
        this.readCmd = readCmd;
    }

    public void setSfi(int sfi) {
        this.sfi = sfi;
    }

    public int getSfi() {
        return sfi;
    }

    public void setRecordNumber(int recordNum) {
        this.recordNum = recordNum;
    }

    public int getRecordNumber() {
        return recordNum;
    }

    public void setReadCommand(byte[] readCmd) {
        this.readCmd = readCmd;
    }

    public byte[] getReadCommand() {
        return readCmd;
    }
}
