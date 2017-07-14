package com.john.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.john.librarys.floatwindow.FloatWindowService;
import com.john.librarys.uikit.activity.BaseActivity;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class MainActivity extends BaseActivity {

    @Bind(R.id.enableCamera)
    Button enableCamera;

    private Intent floatIntent;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWillDoubleClickExitApp(true);
        ButterKnife.bind(this);

        //启动1像素悬浮窗
        floatIntent=new Intent(this, FloatWindowService.class);
        askForePrmission();


        // 必须在初始化阶段调用,例如onCreate()方法中
        RxPermissions.getInstance(this)
                .requestEach(Manifest.permission.CAMERA)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.granted) {
                            LogUtils.e("permission允许");
                        } else {
                            LogUtils.e("permission禁止");
                        }
                    }
                });

        RxView.clicks(enableCamera)
                .compose(RxPermissions.getInstance(this).ensureEach(Manifest.permission.CAMERA))
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.granted) {
                            LogUtils.e("permission允许");
                        } else {
                            LogUtils.e("permission禁止");
                        }
                    }
                });
    }

    /**
     * 请求用户给予悬浮窗的权限
     */
    public void askForePrmission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                showDialog();
            } else {
                startService(floatIntent);
            }
        }else {
            startService(floatIntent);
        }
    }

    private void showDialog() {
        new AlertDialog.Builder(this).setCancelable(false).setTitle("应用权限授权").setMessage("请开启悬浮窗的权限，否则部分功能不能正常使用！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "开启失败，悬浮窗功能不能正常使用", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "开启成功！", Toast.LENGTH_SHORT).show();
                    //启动FxService
                    startService(floatIntent);
                }
            }
        }
    }
}
