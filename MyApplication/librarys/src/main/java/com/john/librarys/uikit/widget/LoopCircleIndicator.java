package com.john.librarys.uikit.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

public class LoopCircleIndicator extends CircleIndicator {

    LoopViewPager mLoopViewPager;

    public LoopCircleIndicator(Context context) {
        super(context);
    }

    public LoopCircleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置viewpager，必须是LoopViewPager
     *
     * @param viewPager
     */
    @Override
    public void setViewPager(ViewPager viewPager) {
        if (viewPager instanceof LoopViewPager) {
            mLoopViewPager = (LoopViewPager) viewPager;
            super.setViewPager(viewPager);
        } else {
            Log.e("LoopCircleIndicator", "setViewPager muse be instanceof LoopViewPager");
        }
    }

    @Override
    protected void onPageSelcted(int position) {
        int pos = position;
        if (mLoopViewPager != null && mLoopViewPager.getWrappAdapter() != null) {
            pos = mLoopViewPager.getWrappAdapter().getActualPosition(position);
        }
        super.onPageSelcted(pos);
    }

    @Override
    public int getViewPageCurrentItem() {
        return getActualPosition(mLoopViewPager.getCurrentItem());
    }

    @Override
    public int getViewPageCount() {
        return mLoopViewPager.getAdapter().getCount();
    }

    private int getActualPosition(int position) {
        return mLoopViewPager.getWrappAdapter().getActualPosition(position);
    }
}
