package com.sung.noel.tw.demo_googlevr;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;
import com.sung.noel.tw.demo_googlevr.util.LoadImageTask;

import java.io.FileNotFoundException;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by noel on 2019/1/10.
 */
@RuntimePermissions
public class MyPictureActivity extends AppCompatActivity implements View.OnClickListener, LoadImageTask.OnVRImageLoadedListener {
    private final int RESULT_CODE_PHOTO = 11;

    private VrPanoramaView vrPanoramaView;
    private VrPanoramaView.Options vrPanoramaViewOptions;
    private Button buttonLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_picture);
        init();
    }

    //--------

    private void init() {
        buttonLoad = findViewById(R.id.button_load);
        vrPanoramaView = findViewById(R.id.vr_panorama_view);
        buttonLoad.setOnClickListener(this);
        vrPanoramaViewOptions = new VrPanoramaView.Options();

        //類型設置  VrPanoramaView.Options.TYPE_MONO  ,  VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER
        vrPanoramaViewOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
        //隱藏最左邊信息按鈕
        vrPanoramaView.setInfoButtonEnabled(false);
        //隱藏立體模型按鈕
        vrPanoramaView.setStereoModeButtonEnabled(false);
        //關閉手勢拖動
//        vrPanoramaView.setTouchTrackingEnabled(false);
        //設置接口
        vrPanoramaView.setEventListener(new VrPanoramaEventListener() {
            /**
             * 圖片載入成功
             */
            @Override
            public void onLoadSuccess() {
                Toast.makeText(MyPictureActivity.this, "圖片載入成功", Toast.LENGTH_SHORT).show();
            }
            //---------

            /**
             * 圖片載入失敗
             */
            @Override
            public void onLoadError(String errorMessage) {
                Toast.makeText(MyPictureActivity.this, "圖片載入失敗 :" + errorMessage, Toast.LENGTH_SHORT).show();
            }

            //---------

            /***
             * 當模式改變 全螢幕 開/關
             * @param newDisplayMode
             */
            @Override
            public void onDisplayModeChanged(int newDisplayMode) {

            }
            //---------

            /***
             * 當vrPanoramaView被點擊
             */
            @Override
            public void onClick() {

            }
        });
    }

    //------

    @Override
    protected void onPause() {
        super.onPause();
        vrPanoramaView.pauseRendering();
    }

    //------

    @Override
    protected void onResume() {
        super.onResume();
        vrPanoramaView.resumeRendering();
    }

    //------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //釋放資源
        vrPanoramaView.shutdown();
    }
    //------

    /***
     * click 讀取
     * @param v
     */
    @Override
    public void onClick(View v) {
        //權限檢查
        MyPictureActivityPermissionsDispatcher.onAllowedExternalStorageWithPermissionCheck(this);
    }
    //------

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onAllowedExternalStorage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, RESULT_CODE_PHOTO);
    }
    //------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MyPictureActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
    //------

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onShowRationale(final PermissionRequest request) {
        request.proceed();
    }
    //------

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPermissionDenied() {
        Toast.makeText(this, getString(R.string.toast_permission_refuse), Toast.LENGTH_SHORT).show();

    }
    //------

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onNeverAskAgain() {
        goToSettingPermissions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //圖片選擇完成
        if (requestCode == RESULT_CODE_PHOTO && data != null) {
            //取得照片路徑uri
            Uri uri = data.getData();
            ContentResolver contentResolver = this.getContentResolver();

            //讀取照片，型態為Bitmap
            try {
                new LoadImageTask(this,contentResolver.openInputStream(uri)).setOnVRImageLoadedListener(this).execute();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    //---------

    /***
     * 當完成所選圖片讀取
     */
    @Override
    public void onVRImageLoaded(Bitmap bitmap) {
        setImage(bitmap);
    }

    //-----------------------------

    /***
     * 放入bitmap 在 vrPanoramaView上
     * @param bitmap
     */
    private void setImage(Bitmap bitmap) {
        vrPanoramaView.loadImageFromBitmap(bitmap, vrPanoramaViewOptions);
    }

    //-----------------------------

    /**
     * 前往開啟權限
     */
    private void goToSettingPermissions() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(getString(R.string.dialog_message_goto_setting));
        alert.setPositiveButton(getString(R.string.dialog_go), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent settings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                settings.addCategory(Intent.CATEGORY_DEFAULT);
                settings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(settings);
            }
        });
        alert.show();
    }


}
