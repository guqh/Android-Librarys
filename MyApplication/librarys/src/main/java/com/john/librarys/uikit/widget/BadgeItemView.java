package com.john.librarys.uikit.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * 包括上标消息书的图标View
 * 置于右上角
 */
public class BadgeItemView extends FrameLayout {
    ImageView mImageView;
    BadgeView mBadgeView;

    public BadgeItemView(Context context) {
        super(context);
        init(null);
    }

    public BadgeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BadgeItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BadgeItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    void init(AttributeSet attrs){
        mImageView = new ImageView(getContext(),attrs);
        addView(mImageView);;
        mBadgeView = new BadgeView(getContext());
        mBadgeView.setTargetView(mImageView);
        mBadgeView.setHideOnNull(true);
    }

    boolean mInitLayout = false;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!mInitLayout) {//第一次计算宽高的时候去重新布局
            mInitLayout = true;

            mBadgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);

            measureChild(mBadgeView, widthMeasureSpec, heightMeasureSpec);
            int badgeViewWidth = mBadgeView.getMeasuredWidth();
            int badgeViewHeight = mBadgeView.getMeasuredHeight();

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mImageView.getLayoutParams());
            lp.topMargin = badgeViewHeight / 2;
            lp.rightMargin = badgeViewHeight / 2;
            mImageView.setLayoutParams(lp);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置数字
     * @param count
     */
    public void setCount(int count){
        mBadgeView.setBadgeCount(count);
    }

    /**
     * 获取数字
     * @return
     */
    public int getCount(){
        return mBadgeView.getBadgeCount();
    }


    public void setIcon(Drawable drawable){
        mImageView.setImageDrawable(drawable);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setIcon(int resid){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setIcon(getContext().getDrawable(resid));
        }else{
            setIcon(getContext().getResources().getDrawable(resid));
        }
    }
}
