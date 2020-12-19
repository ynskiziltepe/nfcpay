package com.yunus.nfcpay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yunus.nfcpay.activity.PayResult;
import com.yunus.nfcpay.activity.ReadCard;
import com.yunus.nfcpay.consts.EMVConsts;
import com.yunus.nfcpay.consts.EMVConsts.*;
import com.yunus.nfcpay.fragments.PopDialog;
import com.yunus.nfcpay.model.AFL;
import com.yunus.nfcpay.model.APDUSend;
import com.yunus.nfcpay.model.CardPlain;
import com.yunus.nfcpay.utilities.UtilAID;
import com.yunus.nfcpay.utilities.UtilDateTime;
import com.yunus.nfcpay.utilities.UtilHex;

import java.util.ArrayList;
import java.util.Arrays;
/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  28.11.2020 - 09:08
 - Version:
 - File   : CardReadTask.java
 - Description: Class to read smart card
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public class CardReadTask implements Runnable {
    private static final String TAG = CardReadTask.class.getSimpleName();
    private final double payAmount;
    private final Context context;
    private IsoDep nfcHandle = null;
    private CardBrand cardBrand;
    private CardTech cardTech;
/**-------------------------------------------------------------------------------------------------
                                    <CardReadTask>
 - Brief  : Construct read smart card class
 - Detail : Construct read smart card class
 - Parameters \
    -- <context>   : Instance of called class
    -- <Tag>       : Represents an NFC tag that has been discovered
    -- <payAmount> : Amount to be pay via smart card
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    public CardReadTask(@NonNull Context context, @NonNull Tag tag, double payAmount) {
        this.context = context;
        this.payAmount = payAmount;

        try {
            nfcHandle = IsoDep.get(tag);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Log.e(TAG, e.toString());

            e.printStackTrace();
        }

        Vibrator vibrator = null;
        try {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Log.e(TAG, e.toString());

            e.printStackTrace();
        }

        if (vibrator != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));

                } else {
                    vibrator.vibrate(200);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                Log.e(TAG, e.toString());

                e.printStackTrace();
            }
        }
    }
/**-------------------------------------------------------------------------------------------------
                                    <showPopup>
 - Brief  : Function to show warning popup
 - Detail : Function will show warning popup on screen
 - Parameters \
    -- <msg1>     : related message 1
    -- <msg1>     : related message 2
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    private void showPopup(String txt1, String txt2) {

        PopDialog dialog = new PopDialog("Card Read Status");
        dialog.onViewListener(v -> {
            ((TextView)(v.findViewById(R.id.line1))).setText(txt1);
            ((TextView)(v.findViewById(R.id.line2))).setText(txt2);
        });

        dialog.setCancelClickOutside(false);
        dialog.onBtnOkListener(v -> {
            ((AppCompatActivity)context).finish();
        });

        dialog.show(((AppCompatActivity)context).getSupportFragmentManager(),
                                        PopDialog.class.getSimpleName());
    }
/**-------------------------------------------------------------------------------------------------
                                        <sendReceive>
 - Brief  : Function to send/receive data to/from smart card via NFC
 - Detail : Function to send/receive data to/from smart card via NFC
 - Parameters \
    -- <send>    : bytes to be send to smart card
 - Returns  \
    -- <rcv>    : received data to be return
 --------------------------------------------------------------------------------------------------*/
    private byte[] sendReceive(byte[] send) {
        byte[] rcv = null;
        try {
            rcv = nfcHandle.transceive(send);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Log.e(TAG, e.toString());

            e.printStackTrace();
        }
        return rcv;
    }
/**-------------------------------------------------------------------------------------------------
                                <run>
 - Brief  : NFC task
 - Detail : NFC task
 - Parameters \
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    @Override
    public void run() {
        byte[] aid = null; // AID (Application Identifier)
        ArrayList<byte[]> aidList = null; // AID (Application Identifier) list
        byte[] appLabel = null; // Application Label
        byte[] pan = null; // Application PAN (Primary Account Number)
        byte[] cardholderName = null; // Cardholder Name
        byte[] appExpDate = null; // Application Expiration Date
        byte[] cFci = null, rFci = null; // FCI (File Control Information)
        byte[] aflData = null; // AFL (Application File Locator) [GPO] Data
        byte[] cdol1 = null; // CDOL1 (Card Risk Management Data Object List 1)
        byte[] cdol2 = null; // CDOL2 (Card Risk Management Data Object List 2)

        if (nfcHandle == null) {
            return;
        }

        if (nfcHandle.getTag() == null) {
            return;
        }

        Log.d(TAG, "Compatible NFC tag: " + nfcHandle.getTag());

        // Connect to NFC
        connect2NFC();

        // NfcA (ISO 14443-3A)
        byte[] historicalBytes = null;

        try {
            historicalBytes = nfcHandle.getHistoricalBytes();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Log.e(TAG, e.toString());

            e.printStackTrace();
        }

        if (historicalBytes != null && historicalBytes.length > 0) {
            Log.d(TAG, "ISO-DEP - " + context.getString(R.string.nfcA) + ": Supported");

            Log.d(TAG, "ISO-DEP - " + context.getString(R.string.nfcA) + " Hexadecimal: " + UtilHex.bytes2Hex(historicalBytes));
        } else {
            Log.w(TAG, "ISO-DEP - " + context.getString(R.string.nfcA) + ": Not supported");
        }

        // NfcB (ISO 14443-3B)
        byte[] hiLayerResponse = null;

        try {
            hiLayerResponse = nfcHandle.getHiLayerResponse();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Log.e(TAG, e.toString());

            e.printStackTrace();
        }

        if (hiLayerResponse != null && hiLayerResponse.length > 0) {
            Log.d(TAG, "ISO-DEP - " + context.getString(R.string.nfcB) + ": Supported");

            Log.d(TAG, "ISO-DEP - " + context.getString(R.string.nfcB) + " Hexadecimal: " + UtilHex.bytes2Hex(hiLayerResponse));
        } else {
            Log.w(TAG, "ISO-DEP - " + context.getString(R.string.nfcB) + ": Not supported");
        }


        // PPSE (Proximity Payment System Environment)
        byte[] cPpse;
        byte[] rPpse;

        cPpse = APDUSend.getApduBytes(EMVConsts.SELECT, 0x0400, EMVConsts.PPSE.length(), EMVConsts.PPSE.getBytes());

        Log.d(TAG, "EMV - PPSE Command:" + UtilHex.bytes2Hex(cPpse));
        rPpse = sendReceive(cPpse);

        if (rPpse != null) {
            Log.d(TAG, "EMV - PPSE Response:" + UtilHex.bytes2Hex(rPpse));
            if (EMVNative.checkEMVResponse(rPpse)) {
                Log.d(TAG, "EMV - PPSE Response Success:" + UtilHex.bytes2Hex(rPpse));
            } else {
                Log.w(TAG, "EMV - PPSE Response unsuccesful");
                showPopup("Cant read card", "PPSE Response unsuccesful");
                return;
            }
        } else{
            showPopup("Cant read card", "PPSE not read");
            return;
        }

        if(context instanceof ReadCard)
            ((ReadCard) context).setLedColor(2, Color.GREEN);

        try {
            aidList = EMVNative.getAidList(rPpse);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        if (aidList != null) {
            for(byte[] currrAid: aidList) {
                Log.d(TAG, "EMV - AID:" + UtilHex.bytes2Hex(currrAid));
            }
        } else {
            showPopup("Cant read card", "AID not read");
            return;
        }

        for(byte[] currAid: aidList) {
            byte[] subAid = Arrays.copyOfRange(currAid, 0, 7);
            if (Arrays.equals(subAid, UtilAID.A0000000041010)) {
                cFci = APDUSend.getApduBytes(EMVConsts.SELECT, 0x0400, currAid.length, currAid); // Mastercard (PayPass)
                cardBrand = CardBrand.MASTER;
                cardTech = CardTech.PAYPASS;
                Log.d(TAG, "Found AID:" + Arrays.toString(currAid));
            } else if (Arrays.equals(subAid, UtilAID.A0000000043060)) {
                cFci = APDUSend.getApduBytes(EMVConsts.SELECT, 0x0400, currAid.length, currAid); // Maestro (PayPass)
                cardBrand = CardBrand.MAESTRO;
                cardTech = CardTech.PAYPASS;
                Log.d(TAG, "Found AID:" + Arrays.toString(currAid));
            } else if (Arrays.equals(subAid, UtilAID.A0000000031010)) {
                cFci = APDUSend.getApduBytes(EMVConsts.SELECT, 0x0400,
                        currAid.length, currAid); // Visa (PayWave)
                cardBrand = CardBrand.VISA;
                cardTech = CardTech.PAYWAVE;
                Log.d(TAG, "Found AID:" + Arrays.toString(currAid));
            } else if (Arrays.equals(subAid, UtilAID.A0000000032010)) {
                cFci = APDUSend.getApduBytes(EMVConsts.SELECT, 0x0400,
                        currAid.length, currAid); // Visa Electron (PayWave)
                cardBrand = CardBrand.VISA_ELECTRON;
                cardTech = CardTech.PAYWAVE;
                Log.d(TAG, "Found AID:" + Arrays.toString(currAid));
            }

            if (cFci != null) {
                rFci = sendReceive(cFci);
                Log.d(TAG, "EMV - FCI Command:" + UtilHex.bytes2Hex(cFci));
            }

            if (rFci != null) {
                if (EMVNative.checkEMVResponse(rFci)) {
                    Log.d(TAG, "EMV - FCI Response Successful:" + UtilHex.bytes2Hex(rFci));
                    aid = currAid;
                    break;
                }
            }
        }

        // Application not selected.
        if(null == aid) {
            Log.d(TAG, "EMV - AID not selected");
            showPopup("Card AID not selected", "FCI fail or FCI format error");
            return;
        }

        // - FCI (File Control Information)
        // Application Label (May be ASCII convertible)
        appLabel = EMVNative.getTlvValue(rFci, EMVConsts.APPLICATION_LABEL);

        if (appLabel != null) {
            Log.d(TAG, "EMV - Application Label:" + new String(appLabel));
        }


        // PDOL (Processing Options Data Object List)
        byte[] pdol = null;
        byte[] tempPdol = EMVNative.getTlvValue(rFci, EMVConsts.PDOL);

        if (tempPdol != null && EMVNative.checkDOL(tempPdol, EMVConsts.PDOL)) {
            pdol = tempPdol;
            Log.d(TAG, "EMV - PDOL:" + UtilHex.bytes2Hex(pdol));
        }

        // prepare PDOL
        byte[] pdolPrepared = EMVNative.prepareDOL(pdol, EMVConsts.PDOL);
        if (pdolPrepared != null) {
            Log.d(TAG, "EMV - PDOL Prepared:" + Arrays.toString(pdolPrepared));
        } else {
            showPopup("Cant read card", "PDOL cant prepare");
            return;
        }

        // GPO (Get Processing Options)
        byte[] cGpo = APDUSend.getApduBytes(EMVConsts.GET_PROCESSING_OPTIONS,  0x0000,
                pdolPrepared.length, pdolPrepared);
        byte[] rGpo = null;

        Log.d(TAG, "EMV GPO Command:" + UtilHex.bytes2Hex(cGpo));
        rGpo = sendReceive(cGpo);

        if (rGpo != null) {
            if (EMVNative.checkEMVResponse(rGpo)) {
                Log.d(TAG, "EMV (GPO Response Successful:" + UtilHex.bytes2Hex(rGpo));
            } else {
                Log.d(TAG, "EMV (GPO Response Unsuccessful:");
                showPopup("Cant read card", "GPO response is fail");
                return;
            }
        } else {
            showPopup("Cant read card", "GPO not read");
            return;
        }

        if(context instanceof ReadCard)
            ((ReadCard) context).setLedColor(3, Color.GREEN);

        // PayWave Only
        if (cardTech == CardTech.PAYWAVE) {
            // Application PAN
            pan = EMVNative.getTlvValue(rGpo, EMVConsts.APPLICATION_PAN);
            if (pan != null)
                Log.d(TAG, "EMV - Application Pan" + UtilHex.bytes2Hex(pan));
        }

        // Cardholder Name
        cardholderName = EMVNative.getTlvValue(rGpo, EMVConsts.CARDHOLDER_NAME);
        if (cardholderName != null)
            Log.d(TAG, "EMV - Card Holder Name:" + new String(cardholderName));


        // Application Expiration Date
        appExpDate = EMVNative.getTlvValue(rGpo, EMVConsts.APPLICATION_EXPIRATION_DATE);
        if (appExpDate != null)
            Log.d(TAG, "EMV - Application Expiration Date:" + new String(appExpDate));


        // Response message template 1
        if (0 == Integer.compare(EMVConsts.GPO_RMT1, (int)rGpo[0])) {
            Log.d(TAG,"EMV - GPO - Response message template 1");

            byte[] gpoData80 = null;
        }

        // Response message template 2
        if (rGpo[0] == EMVConsts.GPO_RMT2) {
            Log.d(TAG,"EMV - GPO - Response message template 2");

            byte[] gpoData77 = EMVNative.getTlvValue(rGpo, EMVConsts.GPO_RMT2);

            if (gpoData77 != null) {

                aflData = EMVNative.getTlvValue(rGpo, EMVConsts.AFL);
            }
        }

        if (aflData != null) {

            Log.d(TAG, "EMV " + UtilHex.bytes2Hex(aflData));
        } else {
            showPopup("Cant read card", "AFL is null");
            return;
        }

        // AFL (Application File Locator) Record(s)
        ArrayList<AFL> aflArray = EMVNative.getAFLDataRecords(aflData);

        if (aflArray != null && !aflArray.isEmpty()) {
            for (AFL aflItem : aflArray) {
                byte[] cReadRecord = aflItem.getReadCommand();
                byte[] rReadRecord = null;

                if (cReadRecord != null) {
                    Log.d(TAG, "EMV - Read Record Command:" + UtilHex.bytes2Hex(cReadRecord));
                    rReadRecord = sendReceive(cReadRecord);
                }

                if (rReadRecord != null) {
                    boolean succeedLe = false;

                    Log.d(TAG, "EMV - Response Read Record:" + UtilHex.bytes2Hex(rReadRecord));

                    if (EMVNative.checkEMVResponse(rReadRecord)) {
                        succeedLe = true;
                    } else if (EMVNative.getSW(rReadRecord)[0] == (byte) 0x6C) {
                        cReadRecord[cReadRecord.length - 1] = (byte) (rReadRecord.length - 1); // Custom Le

                        Log.d(TAG, "EMV - Read Record Command:" + UtilHex.bytes2Hex(cReadRecord));
                        rReadRecord = sendReceive(cReadRecord);

                        if (rReadRecord != null) {
                            Log.d(TAG, "EMV - Read Record Response:" + UtilHex.bytes2Hex(rReadRecord));

                            if (EMVNative.checkEMVResponse(rReadRecord)) {
                                succeedLe = true;
                            }
                        }
                    }

                    if (succeedLe) {
                        Log.d(TAG, "EMV - Read Record - Successful");

                        // CDOL1 (Card Risk Management Data Object List 1)
                        if (cdol1 == null) {
                            byte[] tempCdol1 = EMVNative.getTlvValue(rReadRecord, EMVConsts.CDOL1);
                            if (tempCdol1 != null && EMVNative.checkDOL(tempCdol1, EMVConsts.CDOL1)) {
                                cdol1 = tempCdol1;
                            }
                        }

                        // CDOL2 (Card Risk Management Data Object List 2)
                        if (cdol2 == null) {
                            byte[] tempCdol2 = EMVNative.getTlvValue(rReadRecord, EMVConsts.CDOL2);
                            if (tempCdol2 != null && EMVNative.checkDOL(tempCdol2, EMVConsts.CDOL2)) {
                                cdol2 = tempCdol2;
                            }
                        }

                        // Application PAN (Primary Account Number)
                        if (pan == null) {
                            pan = EMVNative.getTlvValue(rReadRecord, EMVConsts.APPLICATION_PAN);
                            if (pan != null) {
                                Log.d(TAG, "EMV - Application PAN" + UtilHex.bytes2Hex(pan));
                            }
                        }

                        // Cardholder Name (May be ASCII convertible)
                        if (cardholderName == null) {
                            cardholderName = EMVNative.getTlvValue(rReadRecord, EMVConsts.CARDHOLDER_NAME);
                            if (cardholderName != null) {
                                Log.d(TAG, "EMV - Card Holder Name:" + new String(cardholderName));
                            }
                        }

                        // Application Expiration Date
                        if (appExpDate == null) {
                            appExpDate = EMVNative.getTlvValue(rReadRecord, EMVConsts.APPLICATION_EXPIRATION_DATE);

                            if (appExpDate != null) {
                                Log.d(TAG, "EMV - Application Expitation Date" + UtilHex.bytes2Hex(appExpDate));
                            }
                        }

                        // PayPass Only
                        if (cardTech == CardTech.PAYPASS) {
                            // Without CVM; Signature; Offline -> Proceed with UNs
                            byte[] pUnAtcTrack1 = EMVNative.getTlvValue(rReadRecord, EMVConsts.P_UN_ATC_TRACK1);
                            byte[] nAtcTrack1 = EMVNative.getTlvValue(rReadRecord, EMVConsts.N_ATC_TRACK1);
                            byte[] pUnAtcTrack2 = EMVNative.getTlvValue(rReadRecord, EMVConsts.P_UN_ATC_TRACK2);
                            byte[] nAtcTrack2 = EMVNative.getTlvValue(rReadRecord, EMVConsts.N_ATC_TRACK2);

                            // PayPass cloning information
                            if (pUnAtcTrack1 != null && nAtcTrack1 != null && pUnAtcTrack2 != null && nAtcTrack2 != null) {
                                Log.d(TAG, "This PayPass paycard can be cloned");

                                // Cloning information
                                int kTrack1 = 0, tTrack1 = nAtcTrack1[0];
                                for (byte byteOut : pUnAtcTrack1) {
                                    int i = byteOut;
                                    if (i < 0) {
                                        i += 256;
                                    }

                                    kTrack1 += Integer.bitCount(i);
                                }

                                int kTrack2 = 0, tTrack2 = nAtcTrack2[0];
                                for (byte byteOut : pUnAtcTrack2) {
                                    int i = byteOut;
                                    if (i < 0) {
                                        i += 256;
                                    }

                                    kTrack2 += Integer.bitCount(i);
                                }

                                int unDigits = Math.max(kTrack1 - tTrack1, kTrack2 - tTrack2);
                                Log.d(TAG, "UN Digits: " + unDigits);

                                double totalUns = Math.pow(10, unDigits);
                                Log.d(TAG, "Total UNs: " + totalUns);
                            } else {
                                Log.d(TAG, "This PayPass paycard cannot be cloned");
                            }
                        }
                    } else {
                        Log.w(TAG, "EMV - Read Record not succeed");
                    }
                }
            }
        } else {
            Log.w(TAG, "AFL data cannot read or empty");

            showPopup("Cant read card", "AFL data not parsed");
            return;
        }

        if (cdol1 != null) {
            Log.d(TAG, "EMV - CDOL1:" + UtilHex.bytes2Hex(cdol1));

            byte[] cdol1Constructed = EMVNative.prepareDOL(cdol1, EMVConsts.CDOL1);
            if (cdol1Constructed != null) {
                Log.d(TAG, "EMV Prepared CDOL1: " + UtilHex.bytes2Hex(cdol1Constructed));
            }
            else {
                showPopup("CDOL1 is empty", "");
                return;
            }
            // - CDOL1 Constructed
            // First GAC (Generate Application Cryptogram)
            byte[] cFirstGac = APDUSend.getApduBytes(EMVConsts.GENERATE_AC, 0x0000,
                    cdol1Constructed.length, cdol1Constructed);
            byte[] rFirstGac = null; // C-APDU & R-APDU

            Log.d(TAG, "EMV -  GAC Command:" + UtilHex.bytes2Hex(cFirstGac));
            rFirstGac = sendReceive(cFirstGac);

            if (rFirstGac != null) {

                if (EMVNative.checkEMVResponse(rFirstGac)) {
                    Log.d(TAG, "EMV - Response GAC Successul:" + UtilHex.bytes2Hex(rFirstGac));
                } else {
                    Log.d(TAG, "EMV - Response GAC Unsuccessul:" + UtilHex.bytes2Hex(rFirstGac));
                }
            }
        }

        if (cdol2 != null) {
            Log.d(TAG, "EMV - CDOL2:" + UtilHex.bytes2Hex(cdol2));
        }

        closeNFCConnect();

        if(context instanceof ReadCard)
            ((ReadCard) context).setLedColor(4, Color.GREEN);

        if(null == pan){
            showPopup("PAN is empty", "");
            return;
        }

        String panAscii = UtilHex.bytes2Hex(pan);
        String panMask = "******";
        panAscii = panAscii.substring(0, 6) + panMask + panAscii.substring(12, 16);
        panAscii = panAscii.substring(0, 4) + " " + panAscii.substring(4, 8)
                   + " " + panAscii.substring(8, 12) + " " + panAscii.substring(12, 16);

        String date = UtilDateTime.getDate("dd-MM-yyyy"); //TODO: get in transaction
        String time = UtilDateTime.getTime("HH:mm:ss"); //TODO: get in transaction

        //This application is a test application. So the payAmount is not sent to the card
        CardPlain cardPlain = new CardPlain(panAscii, new String(appLabel),
                aid, cardBrand, payAmount, date, time);

        Intent intent = new Intent(context, PayResult.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(CardPlain.class.getSimpleName(), cardPlain);
        context.startActivity(intent);
        ((AppCompatActivity)context).finish();
    }
/**-------------------------------------------------------------------------------------------------
                                    <connect2NFC>
 - Brief  : Function to connect NFC hardware
 - Detail : Function to connect NFC hardware
 - Parameters \
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    private void connect2NFC() {
        if (nfcHandle == null) {
            Log.w(TAG, "ISO-DEP - Connect failed, no actionable instance found");

            return;
        }

        if (nfcHandle.getTag() == null) {
            Log.w(TAG, "ISO-DEP - Connect failed, tag not found");

            return;
        }

        // Try to enable I/O operations to the tag
        Log.d(TAG, "ISO-DEP - Trying to enable I/O operations to the tag...");
        try {
             nfcHandle.connect();
        } catch (Exception e) {
            Log.e(TAG, "ISO-DEP - Exception while trying to enable I/O operations to the tag");
            Log.e(TAG, e.getMessage());
        } finally {
            if (nfcHandle.isConnected()) {
                Log.d(TAG, "ISO-DEP - Enabled I/O operations to the tag");
            } else {
                Log.w(TAG, "ISO-DEP - Not enabled I/O operations to the tag");
            }
        }
    }
/**-------------------------------------------------------------------------------------------------
                                <closeNFCConnect>
 - Brief  : Function to close NFC connection
 - Detail : Function to close NFC connection
 - Parameters \
 - Returns  \
 --------------------------------------------------------------------------------------------------*/
    private void closeNFCConnect() {
        if (nfcHandle == null) {
            Log.w(TAG, "ISO-DEP - Close failed");
            return;
        }

        if (nfcHandle.getTag() == null) {
            Log.w(TAG, "ISO-DEP - Close failed, tag not found");
            return;
        }

        try {
            nfcHandle.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Log.e(TAG, e.toString());
            e.printStackTrace();
        } finally {
            if (nfcHandle.isConnected()) {
                Log.w(TAG, "ISO-DEP - Not disabled I/O operations to the tag");
            } else {
                Log.d(TAG, "ISO-DEP - Disabled I/O operations to the tag");
            }
        }
    }
}
