package com.john.librarys.uikit.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.john.librarys.R;


/**
 * Created by LinYi.
 */
public class TopBar extends LinearLayout {

    /**
     * 右侧类型
     */
    public enum RightType {
        IMAGE, TEXT
    }

    private boolean mShowLeftImage = false;//是否显示左侧图片（默认不显示）
    private int mLeftImage = 0;//左侧图片资源
    private String mLeftText = "";//左侧文字
    private int mLeftTextColor;//左侧文字颜色
    private String title = "";//中间标题文字
    private int mTitleColor;//中间标题颜色
    private int mTitleTextSize;//中间标题大小
    private int mRightImage;//右侧图片资源
    private String mRightText = "";//右侧文字
    private int mRightTextColor;//右侧文字颜色
    private int mRightTextSize=14;//右侧文字大小
    private RightType mRightType = RightType.TEXT;//右侧内容类型（图片或文字，默认为文字）


    private TextView mTvLeft, mTvTitle, mTvRight;
    private ImageView mIvLeft, mIvRight;
    private LinearLayout mLlLeft;//左侧容器

    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getValues(attrs);
        create(context);
    }

    private void create(Context context) {
        LayoutInflater.from(context).inflate(R.layout.top_bar, this);
        mTvLeft = (TextView) findViewById(R.id.tv_left);
        mTvRight = (TextView) findViewById(R.id.tv_right);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvLeft = (ImageView) findViewById(R.id.iv_left);
        mIvRight = (ImageView) findViewById(R.id.iv_right);
        mLlLeft = (LinearLayout) findViewById(R.id.ll_left);

        if (mShowLeftImage)
            mLlLeft.setOnClickListener(mBackListener);


        //左侧
        if (mShowLeftImage) {
            mIvLeft.setVisibility(View.VISIBLE);
            mIvLeft.setImageResource(mLeftImage);//左侧图
        } else {
            mIvLeft.setVisibility(View.GONE);
        }
        mTvLeft.setText(mLeftText);//左侧文字
        mTvLeft.setTextColor(mLeftTextColor);//左侧文字颜色
        //中间title
        if(TextUtils.isEmpty(title)){
            mTvTitle.setVisibility(View.GONE);
        }else{
            mTvTitle.setText(title);//title文字
        }
        mTvTitle.setTextColor(mTitleColor);//title颜色
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);//title大小
        //右侧
        if (mRightType == RightType.IMAGE) {
            mTvRight.setVisibility(View.GONE);
            mIvRight.setVisibility(View.VISIBLE);
            mIvRight.setImageResource(mRightImage);//右侧图
        } else {
            mIvRight.setVisibility(View.GONE);
            mTvRight.setVisibility(View.VISIBLE);
            mTvRight.setText(mRightText);//右侧文字
            mTvRight.setTextColor(mRightTextColor);//右侧文字颜色
            mTvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize);//右侧文字颜色
        }


    }


    private void getValues(AttributeSet attrs) {

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.TopBar);

        mShowLeftImage = array.getBoolean(R.styleable.TopBar_showLeftImage, false);
        mLeftImage = array.getResourceId(R.styleable.TopBar_leftImage, R.mipmap.top_bar_btn_back_dark);
        if (array.hasValue(R.styleable.TopBar_leftText))
            mLeftText = array.getString(R.styleable.TopBar_leftText);
        mLeftTextColor = array.getColor(R.styleable.TopBar_leftTextColor, Color.BLACK);
        if (array.hasValue(R.styleable.TopBar_tb_title)) {
            title = array.getString(R.styleable.TopBar_tb_title);
        }
        mTitleColor = array.getColor(R.styleable.TopBar_tb_titleTextColor, Color.BLACK);
        mTitleTextSize= array.getDimensionPixelSize(R.styleable.TopBar_tb_titleTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        int val = array.getInt(R.styleable.TopBar_rightType, 0);
        if (val == 1) {
            this.mRightType = RightType.TEXT;
        } else {
            this.mRightType = RightType.IMAGE;
        }
//        Log.e("val", "val = " + val);
//        Log.e("","type="+mRightType);
        mRightImage = array.getResourceId(R.styleable.TopBar_rightImage, R.mipmap.top_bar_btn_message);
        if (array.hasValue(R.styleable.TopBar_rightText)) {
            mRightText = array.getString(R.styleable.TopBar_rightText);
        }
        mRightTextColor = array.getColor(R.styleable.TopBar_rightTextColor, Color.BLACK);
        mRightTextSize = array.getDimensionPixelSize(R.styleable.TopBar_rightTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        array.recycle();
    }

    /**
     * 左侧点击监听
     *
     * @param listener
     */
    public void setLeftClickListener(OnClickListener listener) {
        if (null != listener)
            mLlLeft.setOnClickListener(listener);
    }

    /**
     * 中间标题点击监听
     *
     * @param listener
     */
    public void setTitleClickListener(OnClickListener listener) {
        if (null != listener)
            mTvTitle.setOnClickListener(listener);
    }

    /**
     * 右侧监听
     *
     * @param listener
     */
    public void setRightClickListener(OnClickListener listener) {
        if (null != listener) {
            mIvRight.setOnClickListener(listener);
            mTvRight.setOnClickListener(listener);
        }
    }

    /**
     * 设置左侧图片
     *
     * @param imageRes
     */
    public void setLeftImageRes(int imageRes) {
        this.setLeft(imageRes, null);
    }

    public void setLeftText(String text) {
        if (null != text)
            mTvLeft.setText(text);
    }

    /**
     * 设置左侧图片、文字
     *
     * @param imageRes
     * @param text
     */
    public void setLeft(int imageRes, String text) {
        mIvLeft.setVisibility(View.VISIBLE);
        mIvLeft.setImageResource(imageRes);
        if (null != text)
            mTvLeft.setText(text);
    }

    /**
     * 设置title文字
     *
     * @param text
     */
    public void setTitleText(String text) {
        if (null != text)
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(text);
    }

    /**
     * 设置右侧图片
     *
     * @param imageRes
     */
    public void setRightImageRes(int imageRes) {
        mIvRight.setVisibility(View.VISIBLE);
        mTvRight.setVisibility(View.GONE);
        mIvRight.setImageResource(imageRes);
    }

    /**
     * 设置右侧文字
     *
     * @param text
     */
    public void setRightText(String text) {
        mIvRight.setVisibility(View.GONE);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText(text);
    }
    /**
     * 设置右侧文字颜色
     *
     * @param color
     */
    public void setRightTextColor(int color) {
        mIvRight.setVisibility(View.GONE);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setTextColor(color);
    }
    /**
     * 设置右侧文字大小
     *
     * @param size
     */
    public void setRightTextSize(int size) {
        mIvRight.setVisibility(View.GONE);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize);//右侧文字颜色
    }

    private OnClickListener mBackListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // 默认是关闭当前activity
            if (getContext() instanceof Activity) {
                ((Activity) getContext()).finish();
            }
        }
    };

}
