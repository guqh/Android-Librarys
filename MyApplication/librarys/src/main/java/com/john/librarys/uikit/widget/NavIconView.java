package com.john.librarys.uikit.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.john.librarys.R;


/**
 * 导航栏button
 * <p/>
 * xml属性：</br>
 * pageId:对应的页面ID</br>
 * src:icon图片</br>
 * title:标题</br>
 *
 * @author chenzipeng
 */
public class NavIconView extends FrameLayout {

    private int pageId = -1;
    private Drawable src = null;
    private String title = null;
    private ImageView mIconView;
    private TextView mTextView;
    private BadgeView mBadgeView;

    public NavIconView(Context context) {
        super(context);
        init(null);
    }

    public NavIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NavIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavIconView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.NavIconView);

            if (a.hasValue(R.styleable.NavIconView_pageId)) {
                setPageId(a.getInt(R.styleable.NavIconView_pageId, -1));
            }

            if (a.hasValue(R.styleable.NavIconView_src)) {
                setSrc(a.getDrawable(R.styleable.NavIconView_src));
            }

            if (a.hasValue(R.styleable.NavIconView_title)) {
                setTitle(a.getString(R.styleable.NavIconView_title));
            }
        }

        inflate(getContext(), R.layout.navicon, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIconView = (ImageView) findViewById(R.id.navicon_icon);
        mTextView = (TextView) findViewById(R.id.navicon_title);
        mBadgeView = (BadgeView) findViewById(R.id.navicon_badge);

        mIconView.setImageDrawable(getSrc());
        // 没有title时隐藏 titleview
        if (title == null) {
            mTextView.setVisibility(GONE);
        }

        mTextView.setText(getTitle());

    }

    public Drawable getSrc() {
        return src;
    }

    public void setSrc(Drawable src) {
        this.src = src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    /**
     * 获取数字
     *
     * @return
     */
    public int getCount() {
        return mBadgeView.getBadgeCount();
    }

    /**
     * 设置数字
     *
     * @param count
     */
    public void setCount(int count) {
        mBadgeView.setBadgeCount(count);
    }
    /**
     * 设置数字字体颜色
     */
    public void setTextColor(int color){
        mBadgeView.setTextColor(color);
    }
}
