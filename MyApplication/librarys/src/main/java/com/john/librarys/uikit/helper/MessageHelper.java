package com.john.librarys.uikit.helper;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;


import com.apkfuns.logutils.LogUtils;
import com.john.librarys.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误消息helper
 */
public class MessageHelper {
    protected Context mContext;
    //类型
    private static MessageHelper mInstance;
    protected Map<Integer, Map<Integer, String>> mErrorMap = new HashMap<>();

    public static MessageHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MessageHelper(context);
        }
        return mInstance;
    }

    protected MessageHelper(Context context) {
        mContext = context;
    }

    protected String getString(Integer resid) {
        return mContext.getString(resid);
    }

    /**
     * 生成公用错误map
     *
     * @return
     */
    protected Map<Integer, String> generateCommonErrorMap() {
        Map<Integer, String> commonErrorMap = new HashMap<>();
        //TODO 注入公共错误，比如网络超时等
        commonErrorMap.put(300, getString(R.string.error_unkonw_fail));
        commonErrorMap.put(400, getString(R.string.error_network_error));
        return commonErrorMap;
    }

    /**
     * 获取消息
     *
     * @param type      错误类型
     * @param errorCode
     * @return
     */
    public String getErrorMessage(Integer type, Integer errorCode) {
        String message = null;
        Map<Integer, String> typeErrorMap = mErrorMap.get(type);
        if (typeErrorMap != null) {
            message = typeErrorMap.get(errorCode);
        }

        //还是找不到错误信息，默认显示错误号
        if (message == null) {
            message = String.format(getString(R.string.error_unkonw_fail), type, errorCode);
        }
        return message;
    }

    /**
     * 显示错误消息
     *
     * @param type      类型 类似
     * @param errorCode 错误号
     * @param window    当前窗口
     */
    public void showErrorMessage(Integer type, Integer errorCode, Window window) {
        View containerView = window.getDecorView().findViewById(android.R.id.content);
        showErrorMessage(type, errorCode, containerView);
    }

    /**
     * 显示错误消息
     *
     * @param type          类型 类似
     * @param errorCode     错误号
     * @param containerView 附着的view
     */
    public void showErrorMessage(Integer type, Integer errorCode, View containerView) {
        String msg = getErrorMessage(type, errorCode);
        showMessage(msg, containerView);
    }

    /**
     * 显示消息
     *
     * @param msg
     * @param containerView
     */
    public void showMessage(String msg, View containerView) {
        if (containerView != null && containerView.getWindowVisibility() == View.VISIBLE) {
            //TODO 这里可以替换实现，比如使用Toast
            Snackbar.make(containerView, msg, Snackbar.LENGTH_SHORT).show();
        } else {
            LogUtils.d("showMessage faild,containerView is null or containerView already Detach from windows? " + msg);
        }
    }

    /**
     * 显示消息
     *
     * @param msg
     * @param window
     */
    public void showMessage(String msg, Window window) {
        View containerView = window.getDecorView().findViewById(android.R.id.content);
        showMessage(msg, containerView);
    }

    /**
     * 显示消息
     *
     * @param msgResId
     * @param containerView
     */
    public void showMessage(Integer msgResId, View containerView) {
        String msg = getString(msgResId);
        showMessage(msg, containerView);
    }

    /**
     * 显示消息
     *
     * @param msgResId
     * @param window
     */
    public void showMessage(Integer msgResId, Window window) {
        showMessage(msgResId, window.getDecorView());
    }
}
