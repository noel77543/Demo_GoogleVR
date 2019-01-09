package com.sung.noel.tw.demo_googlevr;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.vr.cardboard.CardboardGLSurfaceView;
import com.google.vr.ndk.base.GvrLayout;
import com.google.vr.ndk.base.GvrSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardboardGLSurfaceView cardboardGLSurfaceView;
    private GvrSurfaceView gvrSurfaceView;
    private GvrLayout gvrLayout;
    private Button buttonTakePicture;
    private Button buttonMyPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        init();


    }

    //--------

    private void init() {
//        buttonMyPicture = findViewById(R.id.button_my_picture);
//        buttonTakePicture = findViewById(R.id.button_take_picture);
        gvrLayout = findViewById(R.id.gvr_layout);
        initGvr();

//        buttonTakePicture.setOnClickListener(this);
//        buttonMyPicture.setOnClickListener(this);
    }

    //--------

    private void initGvr() {
        gvrLayout = new GvrLayout(this);
        gvrSurfaceView = new GvrSurfaceView(this);
        gvrSurfaceView.setEGLContextClientVersion(2);
        gvrSurfaceView.setEGLConfigChooser(8, 8, 8, 0, 0, 0);
        gvrSurfaceView.setPreserveEGLContextOnPause(true);
        gvrSurfaceView.setRenderer(
                new GLSurfaceView.Renderer() {
                    @Override
                    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//                        nativeOnSurfaceCreated(nativeApp);
                    }

                    @Override
                    public void onSurfaceChanged(GL10 gl, int width, int height) {}

                    @Override
                    public void onDrawFrame(GL10 gl) {
//                        nativeOnDrawFrame(nativeApp);
                    }
                });
        gvrSurfaceView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);
                            gvrSurfaceView.queueEvent(
                                    new Runnable() {
                                        @Override
                                        public void run() {
//                                            nativeOnTriggerEvent(nativeApp);
                                        }
                                    });
                            return true;
                        }
                        return false;
                    }
                });
        gvrLayout.setPresentationView(gvrSurfaceView);
        setContentView(gvrLayout);
    }



    //--------

    @Override
    protected void onPause() {
        super.onPause();
//        gvrSurfaceView.queueEvent(pauseNativeRunnable);
        gvrSurfaceView.onPause();
        gvrLayout.onPause();
    }


    //---------

    @Override
    protected void onResume() {
        super.onResume();
        gvrLayout.onResume();
        gvrSurfaceView.onResume();
//        gvrSurfaceView.queueEvent(resumeNativeRunnable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gvrLayout.onBackPressed();
    }


    //--------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gvrLayout.shutdown();
    }


    //--------

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_my_picture:
                startActivity(new Intent(this, MyPictureActivity.class));
                break;
            case R.id.button_take_picture:
                break;
        }
    }
}
