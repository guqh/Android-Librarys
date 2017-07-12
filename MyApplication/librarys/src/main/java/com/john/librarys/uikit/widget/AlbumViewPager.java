package com.john.librarys.uikit.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class AlbumViewPager extends ViewPager {

    AlbumPicView mCurrentPicView;

    public AlbumViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlbumViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
        if (action == MotionEvent.ACTION_MOVE && !((AlbumViewPagerAdapter) getAdapter()).getPrimaryItem().viewPageCanScroll(ev)) {//不能滑动的时候就不拦截
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
