package com.sung.noel.tw.demo_googlevr.util.surfaceview;

import android.content.Context;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.google.vr.ndk.base.GvrSurfaceView;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by noel on 2019/1/10.
 */
public class CustomVRSurfaceView extends GvrSurfaceView implements GLSurfaceView.Renderer, Camera.PictureCallback {

    private String fileName;
    private Context context;
    private Camera camera;

    public CustomVRSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }


    //--------------

    private void init() {


        //設置為openGLE 2.0
        setEGLContextClientVersion(2);


        //EGL設置 須在調用 setRenderer之前
        //颜色、深度、模板等等設置
        //setEGLConfigChooser();
        //窗口設置
        //setEGLWindowSurfaceFactory();
        //EGLContext設置
        //setEGLContextFactory();

        setRenderer(this);

        //渲染方式設置
        //RENDERMODE_WHEN_DIRTY    表示被動渲染，只有在調用requestRender或者onResume等方法時才會進行渲染。
        //RENDERMODE_CONTINUOUSLY  表示持續渲染
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }


    //---------
    /***
     * 當surfaceview 被創建
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        camera.setDisplayOrientation(getDisplayOrientation());
        try {
            camera.setPreviewDisplay(getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();

    }
    //---------
    /***
     * 自動對焦設置
     * @param holder
     * @param format
     * @param width
     * @param height
     */

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    camera.cancelAutoFocus();
                }
            }
        });
    }
    //---------
    /***
     * 當surface被銷毀 回收資源
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    //-------

    /***
     * 相機預覽的旋轉角度需要根據相機預覽目前的旋轉角度，以及屏幕的旋轉角度計算得到
     * @return
     */
    private int getDisplayOrientation() {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        Camera.CameraInfo camInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, camInfo);

        return (camInfo.orientation - degrees + 360) % 360;
    }

    //------

    /***
     *  拍照
     */
    public void takePicture(String fileName) {
        this.fileName = fileName;
        //拍照 進入callBak進行存檔
        camera.takePicture(null, null, this);
    }

    //-------

    /***
     * 按下拍照後
     * @param data
     * @param camera
     */
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

    }
    //-------

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }
    //-------

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }
    //-------

    @Override
    public void onDrawFrame(GL10 gl) {

    }
    //-------

    @Override
    public void surfaceRedrawNeededAsync(SurfaceHolder holder, Runnable drawingFinished) {

    }

}
