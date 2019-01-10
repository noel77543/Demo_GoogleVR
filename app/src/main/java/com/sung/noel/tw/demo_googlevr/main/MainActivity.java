package com.sung.noel.tw.demo_googlevr.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sung.noel.tw.demo_googlevr.MyPictureActivity;
import com.sung.noel.tw.demo_googlevr.R;
import com.sung.noel.tw.demo_googlevr.util.surfaceview.CustomVRSurfaceView;

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
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonTakePicture;
    private Button buttonMyPicture;
    private FrameLayout frameLayoutContent;
    private CustomSurfaceView customSurfaceView;
    private CustomVRSurfaceView customVRSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivityPermissionsDispatcher.onAllowedToTakePictureWithPermissionCheck(this);
    }


    //--------

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //我的圖片
            case R.id.button_my_picture:
                startActivity(new Intent(this, MyPictureActivity.class));
                break;
            //開始拍攝
            case R.id.button_take_picture:

                break;
        }
    }
    //--------

    /***
     * 當權限允許
     */
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onAllowedToTakePicture() {
        init();
    }

    //--------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
    //--------

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onShowRationale(final PermissionRequest request) {
        request.proceed();
    }
    //--------

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPermissionDenied() {
        Toast.makeText(this, getString(R.string.toast_permission_refuse), Toast.LENGTH_SHORT).show();

    }
    //--------

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onNeverAskAgain() {
        goToSettingPermissions();
    }

    //--------

    /***
     * 初始化
     */
    private void init() {

        buttonMyPicture = findViewById(R.id.button_my_picture);
        buttonTakePicture = findViewById(R.id.button_take_picture);
//        frameLayoutContent = findViewById(R.id.frame_layout_content);
//        customSurfaceView = findViewById(R.id.surface_view);

        customVRSurfaceView = findViewById(R.id.custom_v_r_surface_view);



        buttonTakePicture.setOnClickListener(this);
        buttonMyPicture.setOnClickListener(this);
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
