package com.john.librarys.uikit.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.john.librarys.R;

import java.util.Collection;

/**
 * 画册view
 */
public class AlbumView extends FrameLayout {
    AlbumViewPager mAlbumViewPager;
    AlbumContentView mContentView;
    ViewGroup mContentConatiner;

    AlbumViewPagerAdapter mAlbumAdapter;

    private final int DEFAULT_OFFSCREEN_PAGES = 3;

    public AlbumView(Context context) {
        super(context);
        init(null);
    }

    public AlbumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AlbumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AlbumView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    void init(AttributeSet attrs) {
        setBackgroundColor(Color.BLACK);
        inflate(getContext(), R.layout.widget_album, this);
        mAlbumViewPager = (AlbumViewPager) findViewById(R.id.pic_viewpager);
        mAlbumViewPager.addOnPageChangeListener(mPageChangeListener);
        mAlbumViewPager.setOffscreenPageLimit(DEFAULT_OFFSCREEN_PAGES);

        mContentConatiner = (ViewGroup) findViewById(R.id.content_container);
        mContentView = (AlbumContentView) findViewById(R.id.content_view);

        mAlbumAdapter = new AlbumViewPagerAdapter(this);
        mAlbumViewPager.setAdapter(mAlbumAdapter);
    }

    /**
     * 设置图片
     *
     * @param albumPics
     */
    public void setAblumPic(Collection<AlbumPic> albumPics) {
        mAlbumAdapter.clear();
        mAlbumAdapter.addItems(albumPics);
        mAlbumAdapter.notifyDataSetChanged();

        mContentView.setTotal(mAlbumAdapter.getCount());
        mPageChangeListener.onPageSelected(mAlbumViewPager.getCurrentItem());
    }

    public static class AlbumPic {
        String imgUrl;
        String description;

        public AlbumPic(String imgUrl, String description) {
            this.imgUrl = imgUrl;
            this.description = description;
        }
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mContentView.setAlbumPic(position, mAlbumAdapter.getItem(position));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void setContentVisible(int visible) {
        mContentConatiner.setVisibility(visible);
    }

    public boolean isContentVisible() {
        return mContentConatiner.getVisibility() == VISIBLE;
    }

}
