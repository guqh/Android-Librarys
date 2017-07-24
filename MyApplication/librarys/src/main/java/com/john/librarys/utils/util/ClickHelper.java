package com.john.librarys.utils.util;


import android.view.View;

/**
 * 单击点击控制类
 * Created by Administrator on 2016/1/28.
 */
public class ClickHelper {
    private static long lastClickTime;

    /**
     * 防止事件被多次触发
     * @return
     */
    public static void setFastClick(View view) {
        view.setClickable(false);
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return ;
        }
        lastClickTime = time;

        view.setClickable(true);
    }
}
