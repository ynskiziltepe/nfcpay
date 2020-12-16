package com.yunus.nfcpay.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yunus.nfcpay.R;
import com.yunus.nfcpay.model.CardPlain;
import com.yunus.nfcpay.utilities.UtilHex;
import com.yunus.nfcpay.utilities.UtilQR;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  10.12.2020 - 19:51
 - Version:
 - File   : Receipt.java
 - Description: Class to process payment receipt
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public class Receipt extends Fragment implements View.OnClickListener{
    private static final String TAG = Receipt.class.getSimpleName();
    private final CardPlain cardPlain;
    private ImageView rcptQR;
    private TextView rcptTv;
    private Button btnReceipt;
    private Button btnQR;
    private Button btnMail;
    private Button btnSms;

    public Receipt(CardPlain cardPlain) {
        this.cardPlain = cardPlain;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rcptView = inflater.inflate(R.layout.fragment_receipt, container, false);

        btnReceipt = rcptView.findViewById(R.id.btnReceipt);
        btnReceipt.setOnClickListener(this);
        btnQR = rcptView.findViewById(R.id.btnQR);
        btnQR.setOnClickListener(this);
        btnMail = rcptView.findViewById(R.id.btnMail) ;
        btnMail.setOnClickListener(this);
        btnSms = rcptView.findViewById(R.id.btnSMS);
        btnSms.setOnClickListener(this);

        rcptTv = rcptView.findViewById(R.id.prnStr);
        rcptQR = rcptView.findViewById(R.id.qr);
        return rcptView;
    }
/**-------------------------------------------------------------------------------------------------
                                    <onClick>
 - Brief  : Function will process clicked item
 - Detail : This function purpose to to prepare payment receipt. For example QR receipt, print receipt
 - Parameters \
    -- <view>   : related clicked view
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    @Override
    public void onClick(View v) {

            if(R.id.btnReceipt == v.getId()) {
                ArrayList<String> receipt = prepareReceipt();
                rcptTv.setLines(receipt.size() + 20);
                for (String line : receipt) {
                    rcptTv.append(line);
                }
                rcptTv.setVisibility(View.VISIBLE);
                rcptQR.setVisibility(View.GONE);
                btnReceipt.setSelected(true);
                btnQR.setSelected(false);
                btnMail.setSelected(false);
                btnSms.setSelected(false); }
            else if (R.id.btnQR == v.getId()) {
                Bitmap bmpQR = UtilQR.textToQR(prepareReceipt(), 600, 600);
                rcptQR.setImageBitmap(bmpQR);
                rcptQR.setVisibility(View.VISIBLE);
                rcptTv.setVisibility(View.GONE);
                btnReceipt.setSelected(false);
                btnQR.setSelected(true);
                btnMail.setSelected(false);
                btnSms.setSelected(false); }
            else if (R.id.btnMail == v.getId()){
                //TODO:process receipt via mail
            }
            else if(R.id.btnSMS == v.getId()) {
                //TODO:process receipt via SMS
            }
    }
/**-------------------------------------------------------------------------------------------------
                                            <prepareReceipt>
 - Brief  : Function to prapre string array receipt
 - Detail : This function purpose is prepare receipt as string array for print
 - Parameters \
 - Returns  \
    --receipt: array receipt to be print
 --------------------------------------------------------------------------------------------------*/
    private ArrayList<String> prepareReceipt() {
        ArrayList<String> receipt = new ArrayList<>();
        SharedPreferences appData = Objects.requireNonNull(getActivity())
                .getSharedPreferences("appName", Context.MODE_PRIVATE);
        String termId = appData.getString(getString(R.string.terminalId), "");
        String merchId = appData.getString(getString(R.string.merchantId), "");

        receipt.add("                         " + "\n");
        receipt.add("                         " + "\n");
        receipt.add("Adress:555. Street, 8888, Hamburg" + "\n"); //TODO: add input to menu for address
        receipt.add("Email:eeeeeeee@gmail.com" + "\n");//TODO: add input to menu for mail
        receipt.add("Phone:111-111-1111" + "\n"); //TODO: add input to menu for phone number
        receipt.add("                         " + "\n");
        receipt.add("                         " + "\n");
        receipt.add(String.format(Locale.US,"%s:%s", "Terminal ID", termId) + "\n");
        receipt.add(String.format(Locale.US,"%s:%s", "Merchant ID", merchId) + "\n");
        receipt.add("                         " + "\n");
        receipt.add("CARD HOLDER COPY - SALE" + "\n");
        receipt.add(String.format(Locale.US,"%s %s OF NA",
                cardPlain.getDate(), cardPlain.getTime()) + "\n");
        receipt.add("                         " + "\n");
        receipt.add(cardPlain.getPan() + "\n");
        receipt.add("                         " + "\n");
        receipt.add("                         " + "\n");
        receipt.add(String.format(Locale.US,"AMOUNT: €%,.2f", cardPlain.getAmount()) + "\n");
        receipt.add("                         " + "\n");
        receipt.add("                         " + "\n");
        receipt.add("TRANSACTION COMPLETED WITH CONTACTLESS" + "\n");
        receipt.add("STAN:0001" + "      " + "BATCH:0002" + "\n");
        receipt.add("                         " + "\n");
        receipt.add(String.format(Locale.US, "AID:%s",
                UtilHex.bytes2Hex(cardPlain.getAid())) + "\n");
        receipt.add("                         " + "\n");
        receipt.add("KEEP THIS PAPER" + "\n");
        receipt.add("                         " + "\n");
        receipt.add("                         " + "\n");
        receipt.add("                         " + "\n");

        return receipt;
    }
}
