package com.john.librarys.uikit.adapter;

import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * 无限循环滚动adapter
 */
public class LoopViewPagerAdapter extends PagerAdapter {

    PagerAdapter mPagerAdapter;

    Map<Integer, View> mPositionMap = new HashMap<>();

    public LoopViewPagerAdapter(PagerAdapter pagerAdapter) {
        mPagerAdapter = pagerAdapter;
        mPagerAdapter.registerDataSetObserver(mDataSetObserver);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public PagerAdapter getActualAdapter() {
        return mPagerAdapter;
    }

    public boolean isActualAdapterEmpty() {
        return mPagerAdapter == null || mPagerAdapter.getCount() == 0;
    }

    public int getActualPosition(int pos) {
        return isActualAdapterEmpty() ? -1 : pos % mPagerAdapter.getCount();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (isActualAdapterEmpty()) {
            container.addView(new FrameLayout(container.getContext()));
            return null;
        }
        Object item = mPagerAdapter.instantiateItem(container, getActualPosition(position));
        //必须是view
        View view = (View) item;
        mPositionMap.put(position, view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View v = mPositionMap.get(position);
        mPositionMap.remove(position);
        container.removeView(v);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            LoopViewPagerAdapter.this.notifyDataSetChanged();
        }
    };

}
