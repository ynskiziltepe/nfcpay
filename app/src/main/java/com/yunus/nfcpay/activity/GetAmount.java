package com.yunus.nfcpay.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yunus.nfcpay.R;
import com.yunus.nfcpay.utilities.UtilToast;

import java.util.Locale;

/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  26.11.2020 - 19:49
 - Version:

 - File   : GetAmount.java
 - Description: Class to Get amount or correct amount from screen
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public class GetAmount extends AppCompatActivity {
    private static final String TAG = GetAmount.class.getSimpleName();
    TextView tvAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getInsetsController().hide(WindowInsets.Type.statusBars());
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.get_amount);

        tvAmount = findViewById(R.id.txtAmount);
    }

    @Override
    protected void onResume(){
        super.onResume();

        tvAmount.setText(String.format(Locale.US,"€%,.2f", 0.0));

    }
/**-------------------------------------------------------------------------------------------------
                                <onClick>
 - Brief  : Function will process clicked item
 - Detail : This function purpose to get amount or correct amount from screen
            After click to btnPAy button ReadCard activity will be start
 - Parameters \
    -- <view>   : related clicked view
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    public void onClick(View v) {
        double amount = Double.parseDouble(tvAmount.getText()
                .toString().replace(",", "").substring(1));
        int digitCnt = (int)Math.floor(Math.log10(amount) + 1);

        if((digitCnt > 7) && (v.getId() != R.id.btnClr)) {
            UtilToast.showPopup(getString(R.string.valueIsTooLong), "");
            return;
        }

        if(R.id.btn0 == v.getId()) {
             amount = amount * 10;
             tvAmount.setText(String.format(Locale.US, "€%,.2f", amount)); }
        else if(R.id.btn1 == v.getId()) {
            amount = (amount * 10) + 0.01;
            tvAmount.setText(String.format(Locale.US, "€%,.2f", amount)); }
        else if(R.id.btn2 == v.getId()) {
            amount = (amount * 10) + 0.02;
            tvAmount.setText(String.format(Locale.US, "€%,.2f", amount)); }
        else if(R.id.btn3 == v.getId()) {
            amount = (amount * 10) + 0.03;
            tvAmount.setText(String.format(Locale.US, "€%,.2f", amount)); }
        else if(R.id.btn4 == v.getId()) {
            amount = (amount * 10) + 0.04;
            tvAmount.setText(String.format(Locale.US, "€%,.2f", amount)); }
        else if(R.id.btn5 == v.getId()) {
            amount = (amount * 10) + 0.05;
            tvAmount.setText(String.format(Locale.US, "€%,.2f", amount)); }
        else if(R.id.btn6 == v.getId()) {
            amount = (amount * 10) + 0.06;
            tvAmount.setText(String.format(Locale.US, "€%,.2f", amount)); }
        else if(R.id.btn7 == v.getId()) {
            amount = (amount * 10) + 0.07;
            tvAmount.setText(String.format(Locale.US, "€%,.2f", amount)); }
        else if(R.id.btn8 == v.getId()) {
            amount = (amount * 10) + 0.08;
            tvAmount.setText(String.format(Locale.US, "€%,.2f", amount)); }
        else if(R.id.btn9 == v.getId()) {
            amount = (amount * 10) + 0.09;
            tvAmount.setText(String.format(Locale.US, "€%,.02f", amount)); }
        else if(R.id.btnClr == v.getId()) {
             int intAmount = (int) (amount * 10);
             amount = intAmount / 100.00;
             //Below line is make float round. Above lines are prevent rounding
             tvAmount.setText(String.format(Locale.US, "€%,.2f", amount)); }
        else if(R.id.btnSeprt == v.getId()) {
            if (digitCnt > 6) {
                UtilToast.showPopup(getString(R.string.valueIsTooLong), "");
                return;
            }
            amount = amount * 100;
            tvAmount.setText(String.format(Locale.US, "€%,.2f", amount)); }
        else if(R.id.btnPay == v.getId()) {
            if(amount <= 0)
            {
                UtilToast.showPopup(getString(R.string.plzEnterAmount), "");
                return;
            }
            Intent intent = new Intent(GetAmount.this, ReadCard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(getString(R.string.payAmount), amount);
            startActivity(intent);
        }
    }
}
