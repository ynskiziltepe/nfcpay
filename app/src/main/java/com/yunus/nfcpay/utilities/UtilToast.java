package com.yunus.nfcpay.utilities;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yunus.nfcpay.application.NfcApp;
import com.yunus.nfcpay.R;

import java.io.UnsupportedEncodingException;

/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  12.12.2020 - 09:28
 - Version:
 - File   : UtilToast.java
 - Description: Utility of custom toast message
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public class UtilToast {
    private static final String TAG = UtilToast.class.getSimpleName();
    private static long starttime = System.currentTimeMillis();

/**-------------------------------------------------------------------------------------------------
                                <showPopup>
 - Brief  : Function to show custom toast message
 - Detail : Function to show custom toast message
 - Parameters \
    -- <text1>  : First text of toast message
    -- <text2>  : Second text of toast message
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    public static void showPopup(String text1, String text2)
    {
        long millis = System.currentTimeMillis() - starttime;
        int seconds = (int)(millis / 100);

        Toast toast = Toast.makeText(NfcApp.getInstance(), text1 + "\n" + text2, Toast.LENGTH_SHORT);
        if((15 < seconds) || (0 == seconds))
        {
            starttime = System.currentTimeMillis();
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, -70);
        }
        else
        {
            return;
            //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70);
        }

        View view = toast.getView();
        if(null == view)
        {
            return;
        }
        view.getBackground().setColorFilter(0xFF009688, PorterDuff.Mode.SRC_ATOP);
        TextView text = view.findViewById(android.R.id.message);
        text.setTypeface(Typeface.DEFAULT_BOLD);
        text.setGravity(Gravity.CENTER);
        text.setWidth(500);
        text.setHeight(140);
        text.setTextSize(18);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            text.setTextColor(NfcApp.getInstance().getColor(R.color.white));
        }else {
            text.setTextColor(0xFFFFFFFF);
        }
        toast.show();
    }
/**-------------------------------------------------------------------------------------------------
                                            <showPopup>
 - Brief  : Function to show custom toast message
 - Detail : Function to show custom toast message
 - Parameters \
    -- <text1>  : First text of toast message
    -- <text2>  : Second text of toast message
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    public static void showPopup(byte[] text1, byte[] text2) throws UnsupportedEncodingException {
        String s1 = new String(text1, "cp1254");
        String s2 = new String(text2, "cp1254");
        showPopup(s1, s2);
    }
}
