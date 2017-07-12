package com.john.librarys.utils.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.john.librarys.BuildConfig;

import java.util.List;

/**
 * 包工具
 */
public class PackageHelper {
    /**
     * Return true if Debug build. false otherwise.
     */
    public static boolean isDebugBuild() {
        return BuildConfig.DEBUG;
    }

    public static PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            return manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * Return version code, or 0 if it can't be read
     */
    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionCode;
        }
        return 0;
    }

    /**
     * Return version name, or the string "0" if it can't be read
     */
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionName;
        }
        return "0";
    }

    public static String getApplicationName(Context context) {
        PackageManager manager = context.getPackageManager();
        return (String) manager.getApplicationLabel(context.getApplicationInfo());
    }

    //检查该包名的应用是否已安装
    public static boolean CheckInstall(Context context, String packageName) {
        String checkResult = "未安装";
        boolean bl = false;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            if (packageInfo != null) {
                checkResult = "已经安装应用";
                bl = true;
            }
        } catch (Exception e) {
            checkResult = "是否安装应用失败  Exception";
        }
        Log.e(PackageHelper.class.getSimpleName(),checkResult);
        return bl;
    }
    //遍历所有的系统程序进程，匹配传过来的包名
    public static boolean appIsRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : list) {
            if (info.processName.equals(packageName) && info.processName.equals(packageName)) {
                Log.e(PackageHelper.class.getSimpleName(),"在运行=========");
                return true;
            }
        }
        Log.e(PackageHelper.class.getSimpleName(),"不在在运行=========");
        return false;
    }
}
