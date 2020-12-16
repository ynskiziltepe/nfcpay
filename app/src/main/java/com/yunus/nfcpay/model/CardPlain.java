package com.yunus.nfcpay.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.yunus.nfcpay.consts.EMVConsts.*;
/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  29.11.2020 - 10:07
 - Version:
 - File   : CardPlain.java
 - Description: Card plain data pojo
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public class CardPlain implements Parcelable {
    private String pan;
    private String appLabel;
    private double amount;
    private byte[] aid;
    private int cardBrand;
    private String date;
    private String time;

    public CardPlain(String pan, String appLabel, byte[] aid,
                     CardBrand cardBrand, double amount,
                     String date, String time) {

        this.pan = null == pan ? "" : pan;
        this.appLabel = null == appLabel ? "" : appLabel;
        this.aid = aid;
        this.cardBrand = cardBrand.ordinal();
        this.amount = amount;
        this.date = null == date ? "" : date;
        this.time = null == time ? "" : time;
    }

    protected CardPlain(Parcel in) {
        pan = in.readString();
        appLabel = in.readString();
        amount = in.readDouble();
        aid = in.createByteArray();
        cardBrand = in.readInt();
        date = in.readString();
        time = in.readString();
    }

    public static final Creator<CardPlain> CREATOR = new Creator<CardPlain>() {
        @Override
        public CardPlain createFromParcel(Parcel in) {
            return new CardPlain(in);
        }

        @Override
        public CardPlain[] newArray(int size) {
            return new CardPlain[size];
        }
    };

    public byte[] getAid() {
        return aid;
    }

    public void setAid(byte[] aid) {
        this.aid = aid;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public CardBrand getCardBrand() {
        return CardBrand.values()[cardBrand];
    }

    public void setCardBrand(CardBrand cardBrand) {
        this.cardBrand = cardBrand.ordinal();
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCardBrand(int cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pan);
        dest.writeString(appLabel);
        dest.writeDouble(amount);
        dest.writeByteArray(aid);
        dest.writeInt(cardBrand);
        dest.writeString(date);
        dest.writeString(time);
    }
}
