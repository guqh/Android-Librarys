package com.john.librarys.uikit.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.john.librarys.R;


/**
 * 幻灯片view
 * 会无限循环播放，自动播放
 * <p/>
 * 可以通过 设置ci_drawable，ci_drawable_unselected 来设置指示器的样式
 */
public class SlideView extends FrameLayout {

    LoopViewPager mViewPager;
    PagerAdapter mAdapter;
    LoopCircleIndicator mCircleIndicator;

    boolean mIsAutoScroll = false;//自动滚动？会在 init 方法中初始化

    int mInterval = 5000;//5秒间隔自动滑动

    public SlideView(Context context) {
        super(context);
        init(null);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.widget_slideview, this);
        mViewPager = (LoopViewPager) findViewById(R.id.viewpager);
        mCircleIndicator = (LoopCircleIndicator) findViewById(R.id.indicator);
        //指示器
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircleIndicator);
            mCircleIndicator.mIndicatorBackgroundResId = typedArray.getResourceId(R.styleable.CircleIndicator_ci_drawable,
                    R.drawable.white_radius);
            mCircleIndicator.mIndicatorUnselectedBackgroundResId = typedArray.getResourceId(
                    R.styleable.CircleIndicator_ci_drawable_unselected,
                    mCircleIndicator.mIndicatorUnselectedBackgroundResId);
            typedArray.recycle();
        }

        //启动自动滚动
        setAutoScroll(true);

    }

    /**
     * 设置adapter
     *
     * @param adapter
     */
    public void setAdapter(PagerAdapter adapter) {
        mAdapter = adapter;
        mViewPager.setAdapter(mAdapter);
        mCircleIndicator.setViewPager(mViewPager);
    }


    /**
     * 设置是否自动滚动
     *
     * @param scroll
     */
    public void setAutoScroll(boolean scroll) {
        mIsAutoScroll = scroll;
        mAutoScrollHandler.removeMessages(0);
        if (mIsAutoScroll) {
            postScrollMessage();
        }
    }

    private void postScrollMessage() {
        //延时5秒下次滚动
        mAutoScrollHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAutoScrollHandler.sendEmptyMessage(0);
            }
        }, mInterval);
    }

    ;

    Handler mAutoScrollHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what != 0){
                return true;
            }

            if (!mIsAutoScroll) {
                return true;
            }

            if (mViewPager != null && mViewPager.getAdapter().getCount() > 0) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
            }

            postScrollMessage();
            return true;
        }
    });

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下去的时候停止自动滚动
                mAutoScrollHandler.removeMessages(0);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //触摸事件 抬手，或者取消的时候重新 处理
                postScrollMessage();
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }


}
