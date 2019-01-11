package com.sung.noel.tw.demo_googlevr.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


import java.io.IOException;
import java.io.InputStream;

public class LoadImageTask extends AsyncTask<Void, Void, Void> {

    private OnVRImageLoadedListener onVRImageLoadedListener;
    private InputStream inputStream;
    private Bitmap bitmap;
    private Context context;

    public LoadImageTask(Context context, InputStream inputStream) {
        this.context =context;
        this.inputStream = inputStream;
    }

    //-------

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    //-------

    @Override
    protected Void doInBackground(Void... voids) {
        bitmap =  BitmapFactory.decodeStream(inputStream);

//        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.andes);

        return null;
    }
    //-------

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        onVRImageLoadedListener.onVRImageLoaded(bitmap);
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //-------

    public interface OnVRImageLoadedListener {
        void onVRImageLoaded(Bitmap bitmap);
    }

    //-------

    public LoadImageTask setOnVRImageLoadedListener(OnVRImageLoadedListener onVRImageLoadedListener) {
        this.onVRImageLoadedListener = onVRImageLoadedListener;
        return this;
    }
}
