package com.john.librarys.uikit.waveswiperefreshlayout;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * @author John Gu
 * @date 2017/11/22.
 */

public class DisplayUtil {
    private DisplayUtil(){}

    /**
     * 現在の向きが600dpを超えているかどうか
     *
     * @param context {@link Context}
     * @return 600dpを超えているかどうか
     */
    public static boolean isOver600dp(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return displayMetrics.widthPixels / displayMetrics.density >= 600;
    }
}