package com.john.librarys.uikit.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.john.librarys.uikit.adapter.LoopViewPagerAdapter;

/**
 * 无限循环滚动viewPager
 */
public class LoopViewPager extends ViewPager {

    LoopViewPagerAdapter mWrappPagerAdapter;

    public LoopViewPager(Context context) {
        super(context);
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (!(adapter instanceof LoopViewPagerAdapter)) {
            LoopViewPagerAdapter wrappAdaper = new LoopViewPagerAdapter(adapter);
            adapter = wrappAdaper;
        }

        mWrappPagerAdapter = (LoopViewPagerAdapter) adapter;
        super.setAdapter(adapter);

        setCurrentItem(mWrappPagerAdapter.getCount() / 2);
    }

    public LoopViewPagerAdapter getWrappAdapter() {
        return mWrappPagerAdapter;
    }


    public PagerAdapter getAdapter() {
        return mWrappPagerAdapter.getActualAdapter();
    }

    protected int getActualPosition(int position){
        return mWrappPagerAdapter.getActualPosition(position);
    }
}
