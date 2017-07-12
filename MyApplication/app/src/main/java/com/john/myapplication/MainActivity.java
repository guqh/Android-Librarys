package com.john.myapplication;

import android.Manifest;
import android.os.Bundle;

import com.apkfuns.logutils.LogUtils;
import com.john.librarys.uikit.activity.BaseActivity;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.ButterKnife;
import rx.functions.Action1;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWillDoubleClickExitApp(true);
        ButterKnife.bind(this);


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
    }
}
