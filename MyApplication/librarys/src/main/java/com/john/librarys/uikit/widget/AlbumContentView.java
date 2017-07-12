package com.john.librarys.uikit.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.widget.TextView;

public class AlbumContentView extends TextView {

    private int mPosFontSize = 20;

    private int mTotal = 0;
    private AlbumView.AlbumPic mAlbumPic;

    public AlbumContentView(Context context) {
        super(context);
        init(null);
    }

    public AlbumContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AlbumContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AlbumContentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    void init(AttributeSet attrs) {
        mPosFontSize = (int) (getTextSize() * 1.5);
    }

    public void setTotal(int total) {
        mTotal = total;
    }

    public void setAlbumPic(int pos, AlbumView.AlbumPic pic) {
        mAlbumPic = pic;

        SpannableStringBuilder content = null;
        if (pic == null) {
            content = new SpannableStringBuilder();
        } else {
            String s = (pos + 1) + "/" + mTotal;
            String desciption = s + ("\t\t" + pic.description);
            content = new SpannableStringBuilder(desciption);
            content.setSpan(new AbsoluteSizeSpan(mPosFontSize), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        setText(content);
    }

}
