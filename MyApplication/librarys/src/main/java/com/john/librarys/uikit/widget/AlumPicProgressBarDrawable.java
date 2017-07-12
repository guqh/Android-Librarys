package com.john.librarys.uikit.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.facebook.drawee.drawable.DrawableUtils;

public class AlumPicProgressBarDrawable extends Drawable {

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //private int mColor = Color.rgb(151, 192, 61);
    private int mColor = Color.WHITE;
    private int mBarWidth = 20;
    private int mLevel = 0;
    private boolean mHideWhenZero = false;

    private final int MAX_LEVEL = 10000;

    private int mSize = -1;

    public AlumPicProgressBarDrawable() {
        super();
    }

    public AlumPicProgressBarDrawable(int size) {
        super();
        mSize = size;
    }

    @Override
    public int getIntrinsicWidth() {
        return mSize;
    }

    @Override
    public int getIntrinsicHeight() {
        return mSize;
    }

    /**
     * Sets the progress bar color.
     */
    public void setColor(int color) {
        if (mColor != color) {
            mColor = color;
            invalidateSelf();
        }
    }

    /**
     * Gets the progress bar color.
     */
    public int getColor() {
        return mColor;
    }

    /**
     * Sets the progress bar width.
     */
    public void setBarWidth(int barWidth) {
        if (mBarWidth != barWidth) {
            mBarWidth = barWidth;
            invalidateSelf();
        }
    }

    /**
     * Gets the progress bar width.
     */
    public int getBarWidth() {
        return mBarWidth;
    }

    /**
     * Sets whether the progress bar should be hidden when the progress is 0.
     */
    public void setHideWhenZero(boolean hideWhenZero) {
        mHideWhenZero = hideWhenZero;
    }

    /**
     * Gets whether the progress bar should be hidden when the progress is 0.
     */
    public boolean getHideWhenZero() {
        return mHideWhenZero;
    }

    @Override
    protected boolean onLevelChange(int level) {
        mLevel = level;
        invalidateSelf();
        return true;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return DrawableUtils.getOpacityFromColor(mPaint.getColor());
    }

    @Override
    public void draw(Canvas canvas) {
        if (mHideWhenZero && mLevel == 0) {
            return;
        }

        //drawBar(canvas, MAX_LEVEL, mBackgroundColor);
        drawBar(canvas, mLevel, mColor);
        drawText(canvas, mLevel);
    }

    private Rect getDrawBounds() {
        Rect bounds = getBounds();
        int left = bounds.width() / 2 - mSize / 2;
        int right = bounds.width() / 2 + mSize / 2;
        int top = bounds.height() / 2 - mSize / 2;
        int bottom = bounds.height() / 2 + mSize / 2;
        return new Rect(left, top, right, bottom);
    }

    private void drawBar(Canvas canvas, int level, int color) {
        Rect bounds = getDrawBounds();
        RectF rectF = new RectF(bounds);

        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(mBarWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(color);
        canvas.drawArc(rectF, -90, ((float) level / MAX_LEVEL) * 360, false, mPaint);
    }

    private void drawText(Canvas canvas, int level) {
        Rect bounds = getDrawBounds();

        // 绘制进度文案显示
        int strokeWidth = 2;
        int textColor = Color.WHITE;
        mPaint.setColor(textColor);
        mPaint.setStrokeWidth(strokeWidth);
        String text = (int) ((float) level / MAX_LEVEL * 100) + "%";
        int textHeight = bounds.height() / 4;
        mPaint.setTextSize(textHeight);
        int textWidth = (int) mPaint.measureText(text, 0, text.length());
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, bounds.width() / 2 - textWidth / 2, bounds.height() / 2 + textHeight / 2, mPaint);
    }

}
