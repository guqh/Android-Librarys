package com.john.librarys.uikit.widget;


import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import com.john.librarys.R;


/**
 * 自定义带有删除功能的edittext实例
 */
public class EditTextWithDel extends EditText {
    private final static String TAG = EditTextWithDel.class.getSimpleName();
    private Drawable imgInable;
    private Drawable imgAble;
    private Context mContext;
    private boolean isNotDelete = true;

    public EditTextWithDel(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }


    public void setIsNotDelete(boolean isNotDelete) {
        this.isNotDelete = isNotDelete;
        setCompoundDrawablesWithIntrinsicBounds(null, null, imgInable, null);
    }

    private void init() {
        imgInable = mContext.getResources().getDrawable(R.mipmap.icon_delete);
        imgAble = mContext.getResources().getDrawable(R.mipmap.icon_delete);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
    }

    //设置删除图片
    private void setDrawable() {
        Drawable[] drawables = getCompoundDrawables();
        drawables[2] = length() < 1 ? null : imgAble;

        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

    // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isNotDelete) {
            if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP) {
                int eventX = (int) event.getRawX();
                int eventY = (int) event.getRawY();
                Log.e(TAG, "eventX = " + eventX + "; eventY = " + eventY);
                Rect rect = new Rect();
                getGlobalVisibleRect(rect);
                rect.left = rect.right - getPaddingRight() - 50 ;
                if (rect.contains(eventX, eventY))
                    setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }


    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        setSelection(getText().length());
    }
}
