package com.john.librarys.utils.util;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by LinYi.
 */
public class ClipboardHelper {

    private static final String COPY_SUCCESS = "复制成功";

    private ClipboardHelper() {

    }


    /**
     * 复制到剪贴板
     *
     * @param content
     * @param context
     */
    public static void copy(String content, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if(TextUtils.isEmpty(content)){
            content = "";
        }
        cmb.setText(content.trim());
        Toast.makeText(context, COPY_SUCCESS, Toast.LENGTH_SHORT).show();
    }

    /**
     * 粘贴
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }
}
