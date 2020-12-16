package com.yunus.nfcpay.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.yunus.nfcpay.R;
import com.yunus.nfcpay.model.IGetViewListener;

import java.util.Objects;

/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  10.12.2020 - 19:51
 - Version:
 - File   : PopDialog.java
 - Description: Class to show custom popup message to screen
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public class PopDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = PopDialog.class.getSimpleName();
    private final String header;
    private boolean cancelClickOutside = true;
    private View.OnClickListener okClickListener = null;
    private IGetViewListener viewListener = null;

/**-------------------------------------------------------------------------------------------------
                                    <PopDialog>
 - Brief  : Construct popup dialog class
 - Detail : Construct popup dialog class
 - Parameters \
    -- <header>   : Header of custom popup message
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    public PopDialog(String header)
    {
        this.header = header;
    }

/**-------------------------------------------------------------------------------------------------
                                <onBtnOkListener>
 - Brief  : Process operations after clicked popup's OK button
 - Detail : Process operations after clicked popup's OK button
 - Parameters \
 -- <View.OnClickListener okClickListener>   : listener of click
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    public void onBtnOkListener(View.OnClickListener okClickListener) {
        this.okClickListener = okClickListener;
    }
/**-------------------------------------------------------------------------------------------------
                                 <onViewListener>
 - Brief  : This function purpose is reach popup view items (line, headers) from created functions
 - Detail : This function purpose is reach popup view items (line, headers) from created functions
 - Parameters \
 -- <viewListener>   : listener of view
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    public void onViewListener(IGetViewListener viewListener)
    {
        this.viewListener = viewListener;
    }
/**-------------------------------------------------------------------------------------------------
                                <setCancelClickOutside>
 - Brief  : Function to enable/disable popup outside for close popup
 - Detail : Function to enable/disable popup outside for close popup
 - Parameters \
 -- <cancelClickOutside>   : outside click status
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    public void setCancelClickOutside(boolean cancelClickOutside) {
        this.cancelClickOutside = cancelClickOutside;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View popView = inflater.inflate(R.layout.pop_dialog, container, false);

        TextView btnOK = popView.findViewById(R.id.btnOK);
        TextView tvHeader = popView.findViewById(R.id.header);

        if(null != btnOK) {
            if(null == okClickListener){
                btnOK.setOnClickListener(v -> dismiss());
            } else{
                btnOK.setOnClickListener(okClickListener);
            }
        }

        if(null != tvHeader) {
            tvHeader.setText(header);
        }

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow())
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(cancelClickOutside);

        viewListener.getLayoutView(popView);
        return popView;
    }

/**-------------------------------------------------------------------------------------------------
                                            <onClick>
 - Brief  : Function will process clicked item
 - Detail : This function purpose to close popup so view ID not checked
 - Parameters \
    -- <view>   : related clicked view
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    @Override
    public void onClick(View v) {
        dismiss();
    }
}
