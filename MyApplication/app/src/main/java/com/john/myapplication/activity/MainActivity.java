package com.john.myapplication.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.john.librarys.floatwindow.FloatWindowService;
import com.john.librarys.uikit.activity.BaseActivity;
import com.john.myapplication.R;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by guqh on 2017/8/2.
 *  权限处理
 *  启用1像素悬浮窗 保活程序进程
 */
public class MainActivity extends BaseActivity {

    @Bind(R.id.enableCamera)
    Button enableCamera;
    @Bind(R.id.chronometer)
    Chronometer chronometer;

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


        // 权限处理 必须在初始化阶段调用,例如onCreate()方法中
        new RxPermissions(this)
                .requestEach(Manifest.permission.CAMERA)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.granted) {
                            LogUtils.e("permission允许");
                        } else if (permission.shouldShowRequestPermissionRationale){
                            LogUtils.e("permission用户拒绝了该权限，没有选中『不再询问』");
                        } else {
                            LogUtils.e("permission禁止");
                        }
                    }
                });

        //点击触发 权限处理
        RxView.clicks(enableCamera)
                .compose(new RxPermissions(this).ensureEach(Manifest.permission.CAMERA))
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.granted) {
                            LogUtils.e("permission允许");
                            showToast("已授权");
                        } else if (permission.shouldShowRequestPermissionRationale){
                            LogUtils.e("permission用户拒绝了该权限，没有选中『不再询问』");
                            showToast("已拒绝");
                        } else {
                            LogUtils.e("permission禁止");
                            showToast("已禁止");
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

    //弹窗提示 权限授权
    private void showDialog() {
        new AlertDialog.Builder(this).setCancelable(false).setTitle("应用权限授权").setMessage("请开启悬浮窗的权限，否则部分功能不能正常使用！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }).show();
    }

    //返回值判断悬浮窗权限是否授权成功
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

    @OnClick(R.id.button)
    void openKotlinAndDataBindingActivity(){
        startActivity(new Intent(mContext,KotlinAndDataBindingActivity.class));
    }
    @OnClick(R.id.button2)
    void openDatabindingRecyclerActivity(){
        startActivity(new Intent(mContext,DatabindingRecyclerActivity.class));
    }
    @OnClick(R.id.button3)
    void openDatabindinglistviewActivity(){
        startActivity(new Intent(mContext,DatabindingListViewActivity.class));
    }

    @OnClick({R.id.btnStart,R.id.btnStop,R.id.btnReset,R.id.btn_format})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStart:
                chronometer.start();// 开始计时
                break;
            case R.id.btnStop:
                chronometer.stop();// 停止计时
                break;
            case R.id.btnReset:
                chronometer.setBase(SystemClock.elapsedRealtime());// 复位
                break;
            case R.id.btn_format:
                chronometer.setFormat("Time：%s");// 更改时间显示格式
                break;
        }
    }

}
