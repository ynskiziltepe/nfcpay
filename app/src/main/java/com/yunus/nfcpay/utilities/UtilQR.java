package com.yunus.nfcpay.utilities;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;

/**-------------------------------------------------------------------------------------------------
 - Author :  Yunus KIZILTEPE
 - Date   :  12.12.2020 - 09:28
 - Version:
 - File   : UtilQR.java
 - Description: Utility of QR
 - Warnings \
 --------------------------------------------------------------------------------------------------*/
public final class UtilQR {

/**-------------------------------------------------------------------------------------------------
                                            <text2QR>
 - Brief  : Function to convert text data to QR
 - Detail : Function to convert text data to QR
 - Parameters \
    -- <text2QR>  : text to be converted QR
    -- <qrWidth>  : QR width
    -- <qrHeight> : QR height
 - Returns  \
    -- <bitmap> : QR image
 --------------------------------------------------------------------------------------------------*/
    public static Bitmap text2QR(String text2QR, int qrWidth, int qrHeight) {
        BitMatrix bitMatrix;
        Bitmap bitmap;
        int matrixWidth;
        int matrixHeight;
        int[] pixels;

        try {
            bitMatrix = new MultiFormatWriter().encode(
                    text2QR, BarcodeFormat.DATA_MATRIX.QR_CODE, qrWidth, qrHeight, null);

        } catch (IllegalArgumentException | WriterException Illegalargumentexception) {

            return null;
        }

        matrixWidth = bitMatrix.getWidth();
        matrixHeight = bitMatrix.getHeight();

        pixels = new int[matrixWidth * matrixHeight];

        for (int y = 0; y < matrixHeight; y++) {
            int offset = y * matrixWidth;

            for (int x = 0; x < matrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
            }
        }

        bitmap = Bitmap.createBitmap(matrixWidth, matrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 600, 0, 0, matrixWidth, matrixHeight);
        return bitmap;
    }
/**-------------------------------------------------------------------------------------------------
                                                <text2QR>
 - Brief  : Function to convert array string data to QR
 - Detail : Function to convert array string data to QR
 - Parameters \
    -- <text2QR>  : array string text to be converted QR
    -- <qrWidth>  : QR width
    -- <qrHeight> : QR height
 - Returns  \
    -- <bitmap> : QR image
 --------------------------------------------------------------------------------------------------*/
    public static Bitmap textToQR(ArrayList<String> text2QR, int qrWidth, int qrHeight) {
        StringBuilder allLines = new StringBuilder();

        for (String line : text2QR){
            allLines.append(line);
        }
        return text2QR(allLines.toString(), qrWidth, qrHeight);
    }
}
