package com.yunus.nfcpay.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.yunus.nfcpay.R;
import com.yunus.nfcpay.fragments.PopDialog;
import com.yunus.nfcpay.fragments.Receipt;
import com.yunus.nfcpay.model.CardPlain;

import java.util.Locale;

/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  29.11.2020 - 13:11
 - Version:

 - File   : PayResult.java
 - Description: Class to show payment result
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public class PayResult extends AppCompatActivity {
    private static final String TAG = PayResult.class.getSimpleName();
    private CardPlain cardPlain;

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
        setContentView(R.layout.activity_payresult);

        cardPlain = getIntent().getParcelableExtra(CardPlain.class.getSimpleName());

        if(null != cardPlain) {
            ImageView cardLogo = findViewById(R.id.cardLogo);
            ((TextView) findViewById(R.id.pan)).setText(cardPlain.getPan());
            ((TextView) findViewById(R.id.appLabel)).setText(cardPlain.getAppLabel());
            ((TextView) findViewById(R.id.valAmount))
                    .setText(String.format(Locale.US, "€%,.2f", cardPlain.getAmount()));
            ((TextView) findViewById(R.id.valDate)).setText(cardPlain.getDate());
            ((TextView) findViewById(R.id.valTime)).setText(cardPlain.getTime());

            switch (cardPlain.getCardBrand())
            {
                case VISA:
                    cardLogo.setBackground(ContextCompat.getDrawable(this, R.drawable.visa));
                    break;
                case VISA_ELECTRON:
                    cardLogo.setBackground(ContextCompat.getDrawable(this, R.drawable.visa_electron));
                    break;
                case MASTER:
                    cardLogo.setBackground(ContextCompat.getDrawable(this, R.drawable.mastercard));
                    break;
                case MAESTRO:
                    cardLogo.setBackground(ContextCompat.getDrawable(this, R.drawable.maestro));
                    break;
                case AMEX:
                    cardLogo.setBackground(ContextCompat.getDrawable(this, R.drawable.amex));
                    break;
            }
        }
    }
/**-------------------------------------------------------------------------------------------------
                                        <onClick>
 - Brief  : Function will process clicked item
 - Detail : This function purpose to finish sale or open receipt page
 - Parameters \
    -- <view>   : related clicked view
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    public void onClick(View v) {

        if(v.getId() == R.id.btnRcpt) {
            Receipt fragmentProgram = new Receipt(cardPlain);
            getSupportFragmentManager().beginTransaction().replace(R.id.payresult, fragmentProgram).commit(); }
        else if(v.getId() == R.id.btnDone) {
            PopDialog popDialog = new PopDialog(getString(R.string.payment));

            popDialog.onViewListener(vpop -> ((TextView) (vpop.findViewById(R.id.line1)))
                    .setText(getString(R.string.yourPaymentSuccess)));

            popDialog.onBtnOkListener(v1 -> finish());

            popDialog.show(getSupportFragmentManager(), PopDialog.class.getSimpleName());
        }
    }
}
