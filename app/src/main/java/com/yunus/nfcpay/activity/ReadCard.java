package com.yunus.nfcpay.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yunus.nfcpay.CardReadTask;
import com.yunus.nfcpay.R;
import com.yunus.nfcpay.fragments.PopDialog;
/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  25.11.2020 - 20:27
 - Version:

 - File   : ReadCard.java
 - Description: Class to Read smart card activity
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public class ReadCard extends AppCompatActivity {
    private static final String TAG = ReadCard.class.getSimpleName();
    private NfcAdapter cardNfcAdapter = null;
    private CountDownTimer countDownTimer = null;
    private double payAmount = 0;
    private static final int MAXLED_CNT = 4;

    private final NfcAdapter.ReaderCallback mReaderCallback = tag -> {
        countDownTimer.cancel();
        runOnUiThread(new CardReadTask(ReadCard.this, tag, payAmount));
    };

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

        setContentView(R.layout.read_card);

        WebView nfcPaymentGif = findViewById(R.id.nfctap);
        nfcPaymentGif.loadUrl("file:///android_asset/nfc.gif");

        int density = getResources().getDisplayMetrics().densityDpi;

        if((density >= DisplayMetrics.DENSITY_420) && (density <= DisplayMetrics.DENSITY_XXXHIGH))
        {
            nfcPaymentGif.setInitialScale(220);
        }
        else if(density <= DisplayMetrics.DENSITY_XHIGH)
        {
            nfcPaymentGif.setInitialScale(100);
        }

        payAmount = getIntent().getDoubleExtra(getString(R.string.payAmount), 0);

        ((TextView)(findViewById(R.id.txtPayAmount))).setText(String.format("€%s", payAmount));

        for(int i = 1; i <= MAXLED_CNT; i++){
            TextView led = findViewById(getResources()
                    .getIdentifier("led" + i, "id", getPackageName()));

            if(null != led){
                GradientDrawable bg = (GradientDrawable)led.getBackground();
                bg.mutate();
                bg.setSize(80, 80);

                if (1 == i) {
                    bg.setColor(Color.GREEN);
                } else {
                    bg.setColor(Color.WHITE);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "\"" + TAG + "\": Activity start");

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)) {
            // Try to get the NFC adapter directly
            try {
                cardNfcAdapter = NfcAdapter.getDefaultAdapter(this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                Log.e(TAG, e.toString());

                e.printStackTrace();
            }

            if (cardNfcAdapter == null) {
                NfcManager nfcManager = null;
                try {
                    nfcManager = (NfcManager) getSystemService(NFC_SERVICE);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    Log.e(TAG, e.toString());

                    e.printStackTrace();
                }

                if (nfcManager != null) {
                    try {
                        cardNfcAdapter = nfcManager.getDefaultAdapter();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Log.e(TAG, e.toString());

                        e.printStackTrace();
                    }
                }
            }

            if (cardNfcAdapter == null) {
                nfcNotSupported();
            } else if (!cardNfcAdapter.isEnabled()) {
                nfcNotEnabled();
            } else if (cardNfcAdapter.isEnabled()) {

                cardNfcAdapter.enableReaderMode(this, mReaderCallback,
                        NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_B, null);
                startPaymentTimer(30000, 1000);
            }
        } else {
            nfcNotSupported();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "\"" + TAG + "\": Activity stop");

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)) {
            if (cardNfcAdapter != null) {
                if (cardNfcAdapter.isEnabled()) {
                    Log.d(TAG, "Disabling reader mode...");
                    cardNfcAdapter.disableReaderMode(this);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "\"" + TAG + "\": Activity destroy");

        if (cardNfcAdapter != null) {
            cardNfcAdapter = null;
        }

        System.gc(); // All done, recycle unused objects (mainly because of thread)
    }
/**-------------------------------------------------------------------------------------------------
                                <nfcNotSupported>
 - Brief  : Function to show nfc not support popup
 - Detail : Function to show nfc not support popup
            After click popup's OK button this activity will finish
 - Parameters \
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    private void nfcNotSupported() {

        PopDialog dialog = new PopDialog(getString(R.string.nfcNotSupported));

        dialog.onViewListener(v -> {
            ((TextView)(v.findViewById(R.id.line1))).setText(R.string.notSupportNFC);
        });

        dialog.onBtnOkListener(v -> {
            finishAndRemoveTask();
        });

        dialog.setCancelClickOutside(false);

        dialog.show(getSupportFragmentManager(), PopDialog.class.getSimpleName());
    }
/**-------------------------------------------------------------------------------------------------
                                    <nfcNotEnabled>
 - Brief  : Function to show nfc not enable popup
 - Detail : Function to show nfc not enable popup
            After click popup's OK button, NFC enable page will be open
 - Parameters \
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    private void nfcNotEnabled() {

        PopDialog dialog = new PopDialog(getString(R.string.nfcNotEnabled));

        dialog.onViewListener(v -> {
            ((TextView)(v.findViewById(R.id.line1))).setText(R.string.plzEnableNFC);
        });

        dialog.onBtnOkListener(v -> {
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        });

        dialog.setCancelClickOutside(false);

        dialog.show(getSupportFragmentManager(), PopDialog.class.getSimpleName());
    }
/**-------------------------------------------------------------------------------------------------
                                    <startPaymentTimer>
 - Brief  : Function to count down timer until read card
 - Detail : Function to count down timer until read card
 - Parameters \
    -- <milisInFuture>      : start value of count down
    -- <countDownInterval>  : count interval
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    private void startPaymentTimer(long milisInFuture, long countDownInterval)
    {
        countDownTimer = new CountDownTimer(milisInFuture, countDownInterval)
        {
            final TextView tvTimer = findViewById(R.id.paymentTimer);
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                finishAndRemoveTask();
            }
        };

        countDownTimer.start();
    }
/**-------------------------------------------------------------------------------------------------
                         <setLedColor>
 - Brief  : Function to count down timer until read card
 - Detail : Function to count down timer until read card
 - Parameters \
    -- <ledNum>  : related payment led number
    -- <color>   : related payment led color
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    public void setLedColor(int ledNum, int color){

        if((0 > ledNum) || (4 < MAXLED_CNT))
            return;

        TextView led = findViewById(getResources().getIdentifier("led" + ledNum, "id",
                getPackageName()));

        if(null != led){
            GradientDrawable bg = (GradientDrawable)led.getBackground();
            bg.mutate();
            bg.setColor(color);
        }
    }
}
