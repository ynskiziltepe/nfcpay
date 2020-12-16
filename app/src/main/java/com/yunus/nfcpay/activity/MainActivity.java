package com.yunus.nfcpay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yunus.nfcpay.R;
import com.yunus.nfcpay.fragments.PopDialog;
import com.yunus.nfcpay.utilities.UtilDevice;

/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  20.11.2020 - 22:40
 - Version:

 - File   : MainActivity.java
 - Description: Main activity
 - Warnings \
 --------------------------------------------------------------------------------------------------*/

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    PopDialog dialog = null;
    private static final int LEN_MERCHANT = 8;
    private static final int LEN_TERMID = 8;
    private static final int MINLEN_PWD = 4;

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


        setContentView(R.layout.activity_main);

        if(UtilDevice.isDeviceRooted())
        {
            showWarning(getString(R.string.rootedDevice),
                    getString(R.string.appCantRun), getString(R.string.onRootedDevice));
            if(null != dialog)
            {
                dialog.onBtnOkListener(v -> finish());
            }
        }

        if(UtilDevice.isEmulator())
        {
            showWarning(getString(R.string.emulatorDetected),
                    getString(R.string.appCantRun), getString(R.string.onEmulator));
            if(null != dialog)
            {
                dialog.onBtnOkListener(v -> finish());
            }
        }

        if(UtilDevice.isAdbEnabled())
        {
            /*showWarning(getString(R.string.adbEnabled),
                    getString(R.string.plzDisableADB), getString(R.string.onThisDevice));
            if(null != dialog)
            {
                dialog.onBtnOkListener(v -> finish());
            }*/
        }

    }
/**-------------------------------------------------------------------------------------------------
                                    <showWarning>
 - Brief  : Function to show warning popup
 - Detail : Function will show warning popup on screen
 - Parameters \
    -- <header>   : header of popup
    -- <msg1>     : related message 1
    -- <msg1>     : related message 2
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    private void showWarning(String header, String msg1, String msg2)
    {
        dialog = new PopDialog(header);

        dialog.onViewListener(v -> {
            ((TextView)(v.findViewById(R.id.line1))).setText(msg1);
            ((TextView)(v.findViewById(R.id.line2))).setText(msg2);
        });

        dialog.setCancelClickOutside(false);
        dialog.show(getSupportFragmentManager(), PopDialog.class.getSimpleName());
    }
/**-------------------------------------------------------------------------------------------------
                                        <onClick>
 - Brief  : Function will process clicked item
 - Detail : This function purpose to get customer info
            Function will get customer data and check values
            if every thing is ok, customer data will be save via SharedPreferences
 - Parameters \
    -- <view>   : related clicked view
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    public void onClick(View view) {

        EditText edtMerchId =  findViewById(R.id.edtMerchId);
        EditText edtTermId =  findViewById(R.id.edtTermId);
        EditText edtPwd =  findViewById(R.id.edtPwd);


        if(LEN_MERCHANT > edtMerchId.getText().toString().length()) {
            edtMerchId.setError(getString(R.string.valueShort));
            edtMerchId.requestFocus();
            return;
        }

        if(LEN_TERMID > edtTermId.getText().toString().length()) {
            edtTermId.setError(getString(R.string.valueShort));
            edtTermId.requestFocus();
            return;
        }

        if(MINLEN_PWD > edtPwd.getText().toString().length()) {
            edtPwd.setError(getString(R.string.valueShort));
            edtPwd.requestFocus();
            return;
        }

        SharedPreferences appData = getSharedPreferences("appName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = appData.edit();
        editor.putString(getString(R.string.merchantId),  edtMerchId.getText().toString());
        editor.putString(getString(R.string.terminalId),  edtTermId.getText().toString());
        editor.apply();

        Intent intent = new Intent(MainActivity.this, GetAmount.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
