package com.john.librarys.uikit.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.john.librarys.R;

/**
 * Created by LinYi.
 * <p/>
 * =============================================================================
 * <com.zthink.demo.widget.DraweeView
 * android:layout_margin="10dp"
 * android:id="@+id/id_drawee_view"
 * android:layout_width="300dp"
 * android:layout_height="300dp"
 * fresco:failureImage="@mipmap/failed"
 * fresco:actualImageScaleType="focusCrop"
 * fresco:placeholderImage="@mipmap/loading"
 * fresco:retryImage="@mipmap/retry"
 * />
 * <p/>
 * <!--
 * 1> 必须设置layout_width和layout_height
 * 如果没有在XML中声明这两个属性，将无法正确加载图像。
 * Drawees 不支持 wrap_content 属性。
 * <p/>
 * 2> 固定宽高比
 * 只有希望显示的固定宽高比时，可以使用wrap_content。
 * 如果希望显示的图片保持一定宽高比例，如果 4:3，则在XML中:
 * <com.zthink.demo.widget.AcDraweeView
 * android:id="@+id/my_image_view"
 * android:layout_width="20dp"
 * android:layout_height="wrap_content"
 * 然后在代码中指定显示比例：
 * mDraweeView.setAspectRatio(1.33f);
 * <p/>
 * 3>常用属性
 * fresco:failureImage="@mipmap/failed" 下载错误显示图片
 * fresco:placeholderImage="@mipmap/loading" 下载中占位图
 * fresco:retryImage="@mipmap/retry" 当加载失败时点击重新加载图片
 * fresco:roundAsCircle="true" 圆形
 * fresco:roundedCornerRadius="10dp" 圆角
 * -->
 * =============================================================================
 */
public class DraweeView extends SimpleDraweeView {

    private String mUrl;

    public DraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public DraweeView(Context context) {
        super(context);
    }

    public DraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public DraweeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (null != attrs) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DraweeView);
            if (a.hasValue(R.styleable.DraweeView_url)) {
                mUrl = a.getString(R.styleable.DraweeView_url);
            }
            a.recycle();
        }

    }

    /**
     * 支持 直接资源引用
     *
     * @param res
     */
    public void setUrl(Integer res) {
        Uri uri = Uri.parse("res:///" + res);
        mUrl = uri.getPath();
        this.displayImage(uri);
    }

    public void setUrl(String url) {
        mUrl = url;
        this.displayImage(mUrl);
    }

    public String getUrl() {
        return mUrl;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!TextUtils.isEmpty(mUrl))
            this.displayImage(mUrl);
    }

    /**
     * 类型 	          Scheme 	             示例
     * 远程图片 	          http://, https:// 	 HttpURLConnection 或者参考 使用其他网络加载方案
     * 本地文件               file:// 	             FileInputStream
     * Content provider 	  content:// 	         ContentResolver
     * asset目录下的资源 	  asset:// 	             AssetManager
     * res目录下的资源 	      res:// 	             Resources.openRawResource
     *
     * @param url
     */
    public void displayImage(String url) {
        if (null != url) {
            this.setController(getController(Uri.parse(url)));
        } else
            displayFail();
    }

    private void displayFail() {
        this.setController(getController(Uri.parse("")));
    }

    public void displayImage(Uri uri) {
        if (null != uri) {
            this.setController(getController(uri));
        } else
            displayFail();
    }

    protected DraweeController getController(Uri uri) {
        //可以设置 .setTapToRetryEnabled(true) 来让他加载失败时再点击一次重新加载图片，但是这个会引发拦截住 touch事件，比如 listview中item的点击事件
        return Fresco.newDraweeControllerBuilder().setAutoPlayAnimations(true)
                .setImageRequest(ImageRequestBuilder.newBuilderWithSource(uri)
                        .build()).build();
    }
}
