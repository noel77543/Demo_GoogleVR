package com.sung.noel.tw.demo_googlevr.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Environment;

import com.sung.noel.tw.demo_googlevr.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtil {
    private static final String _DIRECTORY = "Demo_GoogleVR";
    private String fileName;
    //-----------

    /***
     * 水平合成多個 bitmap 為一張 Bitmap
     * @param margin
     * @param bitmaps
     * @return
     */
    public Bitmap addBitmaps(int margin, Bitmap... bitmaps) {
        int width = 0;
        int height = 0;
        int bitmapLength = bitmaps.length;
        for (int i = 0; i < bitmapLength; i++) {
            width += bitmaps[i].getWidth();
            width += margin;
            height = Math.max(height, bitmaps[i].getHeight());
        }
        width -= margin;
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        int left = 0;
        for (int i = 0; i < bitmapLength; i++) {
            if (i > 0) {
                left += bitmaps[i - 1].getWidth();
                left += margin;
            }
            canvas.drawBitmap(bitmaps[i], left, (height - bitmaps[i].getHeight() / 2), null);
        }
        return result;
    }
    //-----------

    /***
     * data 轉 bitmap
     * 並處理鏡像問題
     * @param data
     * @param angle
     */
    public Bitmap createPhoto(byte[] data, int angle) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Matrix mat = new Matrix();
        mat.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
    }

    //-------

    /***
     * 儲存至SD卡
     */
    public void save(Bitmap bitmap, int qualityPercent) {
        byte[] data = bitmapToByteArray(bitmap, qualityPercent);
        File dir = new File(Environment.getExternalStorageDirectory(), _DIRECTORY);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir, fileName + ".jpg");
        try {
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //------

    /***
     * bitmap 轉 byte array 並調整畫質百分比
     * Bitmap decodedBitmap,int qualityPercent
     */
    private byte[] bitmapToByteArray(Bitmap bitmap, int qualityPercent) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, qualityPercent, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    //------

    /***
     *  清除圖片檔
     */
    public void clearPhotoFile(File imageFile) {
        if (imageFile != null && imageFile.exists()) {
            imageFile.delete();
        }
    }

}
