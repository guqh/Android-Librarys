package com.john.myapplication.application;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.john.librarys.net.core.ApiHttpClient;
import com.john.librarys.utils.util.CrashHandler;
import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by guqh on 2017/7/12.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.getLogConfig()
            .configAllowLog(true)
            .configShowBorders(true); //初始化log打印

//        CrashHandler.getInstance().init(this); //初始化异常捕获

        AutoLayoutConifg.getInstance().useDeviceSize(); //初始化自动适配

        Fresco.initialize(this);//初始化图片加载库

        ApiHttpClient.init(this); //初始化网络请求

    }
}
