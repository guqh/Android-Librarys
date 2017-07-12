package com.john.librarys.uikit.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;


import com.john.librarys.R;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * 倒计时view
 */
public class CountDownView extends TextView {

    public final static int MODE_COUNT = 0;
    public final static int MODE_TIME = 1;

    protected int mMode = MODE_COUNT;//模式

    protected CountDownTimer mCountDownTimer;
    protected long mMillisUntilFinished;

    protected OnFinishListener mOnFinishListener = null;

    public CountDownView(Context context) {
        super(context);
        init(null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (null != attrs) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CountDownView);
            if (a.hasValue(R.styleable.CountDownView_mode)) {
                mMode = a.getInt(R.styleable.CountDownView_mode, MODE_COUNT);
            }
            if (a.hasValue(R.styleable.CountDownView_millisUntilFinished)) {
                mMillisUntilFinished = a.getInt(R.styleable.CountDownView_millisUntilFinished, 0);
            }
            a.recycle();
        }
        onTick(mMillisUntilFinished);
    }

    @Override
    protected void onFinishInflate() {
        start(mMillisUntilFinished);
        super.onFinishInflate();
    }

    /**
     * 设置间隔，并启动
     *
     * @param millisUntilFinished
     */
    public void setMillisUntilFinished(long millisUntilFinished) {
        start(millisUntilFinished);
    }

    /**
     * 到期时间
     *
     * @param date
     */
    public void start(Date date) {
        if (date != null) {
            long duration = date.getTime() - System.currentTimeMillis();
            start(duration);
        }
    }

    /**
     * 设置到期时间，这里一般提供给
     * databinding使用
     *
     * @param date
     */
    public void setFinishDate(Date date) {
        start(date);
    }

    /**
     * 启动
     *
     * @param millisUntilFinished
     */
    public void start(long millisUntilFinished) {
        long duration = millisUntilFinished;
        if (duration > 0) {
            start(duration, getInterval());
        }
    }

    /**
     * 启动每秒的倒计时，默认已一秒为间隔
     *
     * @param second
     */
    public void statForCount(long second) {
        if (second > 0) {
            start(second * 1000, 1000);
        }
    }


    /**
     * 启动
     *
     * @param duration
     * @param countDownInterval
     */
    protected void start(long duration, long countDownInterval) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        mMillisUntilFinished = duration;

        mCountDownTimer = new CountDownTimer(duration, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                CountDownView.this.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                CountDownView.this.onFinish();
            }
        };
        mCountDownTimer.start();
    }

    /**
     * 完成
     */
    protected void onFinish() {
        if (getOnFinishListener() != null) {
            getOnFinishListener().onFinish(this);
        }
    }

    protected void onTick(long millisUntilFinished) {
        mMillisUntilFinished = millisUntilFinished;
        switch (mMode) {
            case MODE_COUNT:
                refreshByCount();
                break;
            case MODE_TIME:
                refreshByTime();
                break;
        }
    }

    public void setMode(int mode) {
        mMode = mode;
    }

    /**
     * 获取更新倒计时间隔
     *
     * @return
     */
    public long getInterval() {
        switch (mMode) {
            case MODE_COUNT:
                return 1000;//当为计数的时候返回1秒
            case MODE_TIME:
                return 63;//当为时间倒计时返回 设置这个数字是以为 可以看出数字跳动而又不会太频繁，理论上10ms最合适，但是这个太消耗资源
        }
        return 1000;
    }

    protected void refreshByCount() {
        setText(String.valueOf((int) (mMillisUntilFinished / getInterval())));
    }

    protected void refreshByTime() {
        long day = mMillisUntilFinished / (24 * 60 * 60 * 1000);
        long hour = (mMillisUntilFinished / (60 * 60 * 1000) - day * 24);
//        long minute = ((mMillisUntilFinished / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long minute = ((mMillisUntilFinished / (60 * 1000)));
        long second = (mMillisUntilFinished / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);
        long millis = (mMillisUntilFinished - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - minute * 60 * 1000 - second * 1000);

        DecimalFormat format = new DecimalFormat("00");

        //必定显示 MM:ss:SS
        StringBuffer showText = new StringBuffer();
        if (day > 0) {
//            showText.append(String.valueOf(day)).append("d:");
        }
        if (hour > 0) {
//            showText.append(format.format(hour)).append("h:");
        }
        if (minute >= 0) {
            showText.append(format.format(minute)).append(':');
        }
        if (second >= 0) {
//            showText.append(format.format(second)).append(':');
            showText.append(format.format(second));
        }
//        if (millis >= 0) {
//            showText.append(format.format(millis / 10));
//        }

        setText(showText);
    }

    /**
     * 完成监听器
     */
    public static interface OnFinishListener {
        void onFinish(CountDownView countDownView);
    }

    public OnFinishListener getOnFinishListener() {
        return mOnFinishListener;
    }

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        mOnFinishListener = onFinishListener;
    }
}
