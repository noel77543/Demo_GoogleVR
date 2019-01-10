package com.sung.noel.tw.demo_googlevr.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

public class ImageUtil {
    public ImageUtil() {

    }

    //-------

    /***
     *  byte[]  轉 bitmap
     * @param data
     * @return
     */
    public Bitmap toBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }
    //-------

    /***
     * bitmap 轉向
     * @param bitmap
     * @param angle
     * @return
     */
    public Bitmap rotate(Bitmap bitmap, int angle) {
        Matrix mat = new Matrix();
        mat.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
    }

    //---------

    /***
     *
     * @return
     */
    public ByteArrayOutputStream setQuality(Bitmap decodedBitmap, int qualityPercent) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        decodedBitmap.compress(Bitmap.CompressFormat.JPEG, qualityPercent, byteArrayOutputStream);
        return byteArrayOutputStream;
    }

}
