package com.john.librarys.utils.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.provider.Settings;

/**
 * 网络工具
 * requires android.permission.ACCESS_NETWORK_STATE
 */

public class NetworkHelper {
    public static final int TYPE_UNKNOWN = -1;

    /**
     * returns information on the active network connection
     */
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        if (context == null) {
            return null;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return null;
        }
        // note that this may return null if no network is currently active
        return cm.getActiveNetworkInfo();
    }

    /**
     * returns the ConnectivityManager.TYPE_xxx if there's an active connection, otherwise
     * returns TYPE_UNKNOWN
     */
    public static int getActiveNetworkType(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        if (info == null || !info.isConnected()) {
            return TYPE_UNKNOWN;
        }
        return info.getType();
    }

    /**
     * returns true if a network connection is available
     */
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * returns true if the user is connected to WiFi
     */
    public static boolean isWiFiConnected(Context context) {
        return (getActiveNetworkType(context) == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * returns true if airplane mode has been enabled
     */
    @TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressWarnings("deprecation")
    public static boolean isAirplaneModeOn(Context context) {
        // prior to JellyBean 4.2 this was Settings.System.AIRPLANE_MODE_ON, JellyBean 4.2
        // moved it to Settings.Global
        if (Build.VERSION.SDK_INT < VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    /**
     * returns true if there's an active network connection
     */
    public static boolean checkConnection(Context context) {
        if (context == null) {
            return false;
        }
        if (isNetworkAvailable(context)) {
            return true;
        }
        return false;
    }
}
