package com.john.librarys.utils.util;

<<<<<<< HEAD
=======

>>>>>>> 134b005c9181e93638e722461191e34d6a35d5d5
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
<<<<<<< HEAD

        view.setClickable(true);
    }

=======
>>>>>>> 134b005c9181e93638e722461191e34d6a35d5d5

        view.setClickable(true);
    }
}
