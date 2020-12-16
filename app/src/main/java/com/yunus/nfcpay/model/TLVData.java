package com.yunus.nfcpay.model;

import java.util.ArrayList;
/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  21.11.2020 - 22:58
 - Version:
 - File   : TLVData.java
 - Description: TLV data pojo
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public class TLVData {
    private final int tag;
    private final int tlvLen;
    private final byte[] value;
    private final int tagLen;
    private final int lengthSize;
    private final int extraFlag;
    private final int subFlag;
    private final ArrayList<TLVData> subData;

    public TLVData(int tag, int tlvLen, byte[] value, int tagLen,
                   int lengthSize, int subFlag, int extraFlag) {
        this.tag = tag;
        this.tlvLen = tlvLen;
        this.value = value;
        this.tagLen = tagLen;
        this.lengthSize = lengthSize;
        this.subFlag = subFlag;
        this.extraFlag = extraFlag;
        this.subData = new ArrayList<>();
    }

    public int getTag() {
        return tag;
    }

    public int getTlvLen() {
        return tlvLen;
    }

    public byte[] getValue() {
        return value;
    }

    public void addSubData(TLVData subData) {
        this.subData.add(subData);
    }

    public TLVData getSubData(int index) {

        if(index <= subData.size()){
            return subData.get(index);
        }
        return null;
    }

    public int getSubFlag() {
        return subFlag;
    }

    public int getExtraFlag() {
        return extraFlag;
    }

    public int getSubLen() {
        int sublen = 0;
        for(TLVData sub : subData)
        {
            sublen += (sub.tagLen + sub.tlvLen + sub.lengthSize);
        }
        return sublen;
    }

    public int getSubCount(){
        return subData.size();
    }
}


