package com.sung.noel.tw.demo_googlevr.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.StringDef;
import android.widget.Toast;


import com.sung.noel.tw.demo_googlevr.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by noel on 2017/12/20.
 */

public class SensorUtil implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private Context context;

    private boolean isHadSensor;
    private OnSensorAngleGetListener onSensorAngleGetListener;
    private float frontCorrectAngle;
    private String targetName;
    private double targetLat;
    private double targetLng;


    public static final String SENSOR_EAST = "東";
    public static final String SENSOR_E_S = "東南";
    public static final String SENSOR_SOUTH = "南";
    public static final String SENSOR_W_S = "西南";
    public static final String SENSOR_WEST = "西";
    public static final String SENSOR_W_N = "西北";
    public static final String SENSOR_NORTH = "北";
    public static final String SENSOR_N_E = "東北";

    @StringDef({SENSOR_EAST, SENSOR_WEST, SENSOR_SOUTH, SENSOR_NORTH, SENSOR_E_S, SENSOR_W_S, SENSOR_W_N, SENSOR_N_E})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SensorDirection {

    }

    public SensorUtil(Context context, String targetName, double targetLat, double targetLng) {
        this.context = context;
        this.targetName = targetName;
        this.targetLat = targetLat;
        this.targetLng = targetLng;

        init();
    }

    //----
    private void init() {


        //取得服務
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //設定傳感器類別
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        //註冊監聽
        isHadSensor = sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);

        //false 表示裝置無陀螺儀
        if (!isHadSensor) {
            unRigisterSensor();
            Toast.makeText(context, context.getString(R.string.none_sensor), Toast.LENGTH_SHORT).show();
        }
    }

    //----
    //event 介紹
    // http://blog.csdn.net/wlwlwlwl015/article/details/41759553
    // http://iluhcm.com/2015/09/03/how-to-detect-useful-sensors-for-android-devices/
    // http://androidbiancheng.blogspot.tw/2010/10/androidorientation-azimuth-pitch-roll.html
    // https://wizardforcel.gitbooks.io/w3school-android/content/145.html
    @Override
    public void onSensorChanged(SensorEvent event) {
        //取小數點後第二位
        // Log.e("方位角：", "" + (float) (Math.round(event.values[0] * 100)) / 100);
        // Log.e("倾斜角：", "" + (float) (Math.round(event.values[1] * 100)) / 100);
        // Log.e("滚动角：", "" + (float) (Math.round(event.values[2] * 100)) / 100);

        onSensorAngleGetListener.onSensorAngleGet(((float)( (Math.round(event.values[0] * 100)) / 100)));;

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }




    //------
    //解除註冊  需再使用傳感器的頁面onpause時調用此方法
    public void unRigisterSensor() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    //----

    public interface OnSensorAngleGetListener {
        void onSensorAngleGet(float angle);
    }

    public void setOnSensorAngleGetListener(OnSensorAngleGetListener onSensorAngleGetListener) {
        this.onSensorAngleGetListener = onSensorAngleGetListener;
    }
}
