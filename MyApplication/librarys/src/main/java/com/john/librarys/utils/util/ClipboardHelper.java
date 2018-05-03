package com.john.librarys.utils.util;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import com.john.librarys.R;

/**
 * Created by LinYi.
 */
public class ClipboardHelper {


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
        Toast.makeText(context, context.getString(R.string.str_copy_success), Toast.LENGTH_SHORT).show();
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
