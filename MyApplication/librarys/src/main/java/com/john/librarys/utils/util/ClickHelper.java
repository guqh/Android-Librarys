package com.john.librarys.utils.util;

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
    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


}
