package com.john.librarys.uikit.widget;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class AlbumPicView extends DraweeView {

    private Matrix mMatrix = new Matrix();

    private PointF down = new PointF();
    private PointF mid = new PointF();

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;

    private float oldDist;

    private long mLastPressTime = 0;
    private boolean mIsZoomed = false;//已经缩放过？

    private static final int DOUBLE_POINT_DISTANCE = 10;
    private static final int DOUBLE_CLICK_INTERVAL = 300;
    private static final float MAX_SCALE = 3.0f;
    private static final float MIN_SCALE = 1.0f;

    private final static int DRAG_LEFT = 0;
    private final static int DRAG_RIGHT = 1;
    private final static int DRAG_UP = 2;
    private final static int DRAG_DOWN = 3;

    private Drawable mLoadingDrawable;

    private boolean mIsLoaded = false;

    public AlbumPicView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init(null);
    }

    public AlbumPicView(Context context) {
        super(context);
        init(null);
    }

    public AlbumPicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AlbumPicView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public AlbumPicView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    void init(AttributeSet attrs) {
        setClickable(true);
    }

    public Drawable getLoadingDrawable() {
        if (mLoadingDrawable == null) {
            int size = 100;
            mLoadingDrawable = new AlumPicProgressBarDrawable(size);
        }
        return mLoadingDrawable;
    }

    @Override
    public void setHierarchy(GenericDraweeHierarchy hierarchy) {
        hierarchy.setProgressBarImage(getLoadingDrawable(), ScalingUtils.ScaleType.CENTER);
        super.setHierarchy(hierarchy);
    }

    @Override
    protected DraweeController getController(Uri uri) {
        //可以设置 .setTapToRetryEnabled(true) 来让他加载失败时再点击一次重新加载图片，但是这个会引发拦截住 touch事件，比如 listview中item的点击事件
        return Fresco.newDraweeControllerBuilder().setAutoPlayAnimations(true).setControllerListener(mControllerListener)
                .setImageRequest(ImageRequestBuilder.newBuilderWithSource(uri)
                        .build()).build();

    }

    //监听加载完成
    private ControllerListener mControllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
            mIsLoaded = true;
            super.onFinalImageSet(id, imageInfo, animatable);
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        if (!mIsLoaded) {
            mMatrix = new Matrix();
        }
        canvas.concat(mMatrix);
        getDrawable().draw(canvas);
        canvas.restore();
    }


    boolean wattingDoubleClick = false;//等待第二次click事件的标致

    @Override
    public boolean performClick() {
        //双击事件 比单击事件优先，单击事件需要等待 DOUBLE_CLICK_INTERVAL 秒后再处理，
        //如果在这期间 有另外一次 click事件发生，就定义为双击时间，取消之前的单击事件
        //TODO 使用 handler 等消息通知的实现方式 应该会更稳妥
        long currentTime = System.currentTimeMillis();

        //单击事件，
        if (currentTime - mLastPressTime > DOUBLE_CLICK_INTERVAL) {
            wattingDoubleClick = true;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (wattingDoubleClick == true) {//发现 在DOUBLE_CLICK_INTERVAL期间没有发送第二次click事件，定义为单击事件
                        wattingDoubleClick = false;
                        AlbumPicView.super.performClick();
                    }
                }
            }, DOUBLE_CLICK_INTERVAL);
        }

        //双击事件
        if (currentTime - mLastPressTime <= DOUBLE_CLICK_INTERVAL && wattingDoubleClick) {
            wattingDoubleClick = false;//取消等待双击事件
            if (mIsZoomed) {
                animScale(1, down.x, down.y);
            } else {
                animScale(MAX_SCALE, down.x, down.y);
            }
            mIsZoomed = !mIsZoomed;
            invalidate();
        }
        mLastPressTime = currentTime;

        return true;
    }

    private boolean isMoved = false;//记录是否移动过

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;
                down.x = event.getX();
                down.y = event.getY();

                isMoved = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > DOUBLE_POINT_DISTANCE) {
                    mode = ZOOM;
                }

                isMoved = false;
                break;
            case MotionEvent.ACTION_MOVE:

                if (event.getX() != down.x || event.getY() != down.y) {
                    isMoved = true;
                }

                if (mode == ZOOM) {
                    //在缩放模式下，计算缩放大小
                    float newDist = spacing(event);
                    float scale = newDist / oldDist;

                    //判断双指滑动后的偏移量是否足够大
                    if (Math.abs(scale) > 0.05) {
                        //达到最大值，或者最小值
                        float orginScale = getMatrixScale(mMatrix);//由于 是等比例缩放所以只需要取X 的缩放即可

                        float willScale = scale;
                        if (orginScale * scale < MIN_SCALE) {
                            willScale = MIN_SCALE / orginScale;
                        } else if (orginScale * scale > MAX_SCALE) {
                            willScale = MAX_SCALE / orginScale;
                        }

                        midPoint(mid, event);
                        mMatrix.postScale(willScale, willScale, mid.x, mid.y);//手势中心缩放


                        if (willScale < 1) {
                            //修正缩小后的位移
                            float[] matrixValue = new float[9];
                            mMatrix.getValues(matrixValue);
                            float transX = matrixValue[Matrix.MTRANS_X];
                            float transY = matrixValue[Matrix.MTRANS_Y];
                            float currentScale = matrixValue[Matrix.MSCALE_X];

                            //横向修正
                            if (transX > 0) {
                                mMatrix.postTranslate(-transX, 0);//左侧 有空余
                            } else if (transX < 0) {//图片向左移动了
                                float matrixWidth = getDrawableWidth() * currentScale;//缩放后的 drwable 宽度

                                float rightPaddingOffset = getWidth() - (matrixWidth - Math.abs(transX));//计算出距离 左右侧的空余距离
                                if (rightPaddingOffset > 0) {//右侧有空余
                                    mMatrix.postTranslate(rightPaddingOffset, 0);//修正右侧空余
                                }
                            }

                            //纵向修正
                            if (transY > 0) {
                                mMatrix.postTranslate(0, -transY);//上侧 有空余
                            } else if (transY < 0) {//图片向上移动了
                                float matrixHeight = getDrawableHeight() * currentScale;

                                float bottomPaddingOffset = getHeight() - (matrixHeight - Math.abs(transY));//计算出距离 左右侧的空余距离
                                if (bottomPaddingOffset > 0) {//右侧有空余
                                    mMatrix.postTranslate(0, bottomPaddingOffset);//修正右侧空余
                                }
                            }
                        }

                        mIsZoomed = true;

                        oldDist = newDist;
                        //只能中心缩放
                        invalidate();
                    }

                } else if (mode == DRAG) {

                    if (1.0f < distance(event, down)) {
                        if (event.getX() > down.x ? canDrag(DRAG_RIGHT) : canDrag(DRAG_LEFT)) {
                            getParent().requestDisallowInterceptTouchEvent(true);//如果可以拖动就拦截事件
                        }

                        float[] trans = getMatrixTrans(mMatrix);
                        float tranX = trans[0];
                        float tranY = trans[1];

                        float xOffset = event.getX() - down.x;

                        Matrix horizontalmatrix = new Matrix(mMatrix);
                        horizontalmatrix.postTranslate(xOffset, 0);
                        if (xOffset > 0) {
                            if (canDrag(DRAG_RIGHT)) {
                                mMatrix = horizontalmatrix;
                            }
                        } else if (xOffset < 0) {
                            if (canDrag(DRAG_LEFT)) {
                                mMatrix = horizontalmatrix;
                            }
                        }

                        float yOffset = event.getY() - down.y;
                        Matrix verticalMatrix = new Matrix(mMatrix);
                        verticalMatrix.postTranslate(0, yOffset);
                        if (yOffset > 0) {
                            if (canDrag(DRAG_DOWN)) {
                                mMatrix = verticalMatrix;
                            }
                        } else if (yOffset < 0) {
                            if (canDrag(DRAG_UP)) {
                                mMatrix = verticalMatrix;
                            }
                        }

                        invalidate();
                        down.x = event.getX();
                        down.y = event.getY();

                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mode = NONE;
                //发生拖动的时候，就不通过 super.onTouchEvent去分发touch事件了，防止 会调用onclick的问题
                if (isMoved) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                //发生拖动的时候，就不通过 super.onTouchEvent去分发touch事件了，防止 会调用onclick的问题
                return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 动画缩放
     *
     * @param scale
     * @param x
     * @param y
     */
    private void animScale(final float scale, final float x, final float y) {
        ValueAnimator animator = new ValueAnimator();
        animator.setFloatValues(getMatrixScale(mMatrix), scale);
        animator.setDuration(500);
        animator.setEvaluator(new FloatEvaluator() {
            @Override
            public Float evaluate(float fraction, Number startValue, Number endValue) {
                float value = super.evaluate(fraction, startValue, endValue);
                mMatrix.setScale(value, value, x, y);
                invalidate();
                return value;
            }
        });
        animator.start();
    }

    private float[] getMatrixTrans(Matrix matrix) {
        float[] matrixValue = new float[9];
        matrix.getValues(matrixValue);
        float[] trans = new float[2];
        trans[0] = matrixValue[Matrix.MTRANS_X];
        trans[1] = matrixValue[Matrix.MTRANS_Y];
        return trans;
    }

    private float getMatrixScale(Matrix matrix) {
        float[] matrixValue = new float[9];
        matrix.getValues(matrixValue);
        return matrixValue[Matrix.MSCALE_X];//由于 是等比例缩放所以只需要取X 的缩放即可
    }

    // 触碰两点间距离
    private static float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        if (x < 0) {
            x = -x;
        }
        float y = event.getY(0) - event.getY(1);
        if (y < 0) {
            y = -y;
        }
        return (float) Math.sqrt(x * x + y * y);
    }

    // 取手势中心点
    private static void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    // 取两点之间的距离
    private static float distance(MotionEvent point2, PointF point1) {
        float x = point1.x - point2.getX();
        if (x < 0) {
            x = -x;
        }
        float y = point1.y - point2.getY();
        if (y < 0) {
            y = -y;
        }
        return (float) Math.sqrt(x * x + y * y);
    }

    private int getDrawableWidth() {
        return getDrawable().getBounds().width();
    }

    private int getDrawableHeight() {
        return getDrawable().getBounds().height();
    }

    private void getFourPoint(float[] x, float[] y) {
        float[] f = new float[9];
        mMatrix.getValues(f);
        // 图片4个顶点的坐标
        x[0] = f[Matrix.MSCALE_X] * 0 + f[Matrix.MSKEW_X] * 0
                + f[Matrix.MTRANS_X];
        y[0] = f[Matrix.MSKEW_Y] * 0 + f[Matrix.MSCALE_Y] * 0
                + f[Matrix.MTRANS_Y];
        x[1] = f[Matrix.MSCALE_X] * getDrawableWidth() + f[Matrix.MSKEW_X] * 0
                + f[Matrix.MTRANS_X];
        y[1] = f[Matrix.MSKEW_Y] * getDrawableWidth() + f[Matrix.MSCALE_Y] * 0
                + f[Matrix.MTRANS_Y];
        x[2] = f[Matrix.MSCALE_X] * 0 + f[Matrix.MSKEW_X]
                * getDrawableHeight() + f[Matrix.MTRANS_X];
        y[2] = f[Matrix.MSKEW_Y] * 0 + f[Matrix.MSCALE_Y]
                * getDrawableHeight() + f[Matrix.MTRANS_Y];
        x[3] = f[Matrix.MSCALE_X] * getDrawableWidth() + f[Matrix.MSKEW_X]
                * getDrawableHeight() + f[Matrix.MTRANS_X];
        y[3] = f[Matrix.MSKEW_Y] * getDrawableWidth() + f[Matrix.MSCALE_Y]
                * getDrawableHeight() + f[Matrix.MTRANS_Y];
    }

    private boolean canDrag(final int direction) {
        float[] x = new float[4];
        float[] y = new float[4];
        getFourPoint(x, y);

        // 出界判断
        if ((x[0] > 0 || x[2] > 0 || x[1] < getWidth() || x[3] < getWidth())
                && (y[0] > 0 || y[1] > 0 || y[2] < getHeight() || y[3] < getHeight())) {
            return false;
        }
        if (DRAG_LEFT == direction) {
            // 左移出界判断
            if (x[1] <= getWidth() || x[3] <= getWidth()) {
                return false;
            }
        } else if (DRAG_RIGHT == direction) {
            // 右移出界判断
            if (x[0] >= 0 || x[2] >= 0) {
                return false;
            }
        } else if (DRAG_UP == direction) {
            // 上移出界判断
            if (y[2] <= getHeight() || y[3] <= getHeight()) {
                return false;
            }
        } else if (DRAG_DOWN == direction) {
            // 下移出界判断
            if (y[0] >= 0 || y[1] >= 0) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 用于 是否拦截 viewpage的touch
     *
     * @return
     */
    public boolean viewPageCanScroll(MotionEvent event) {
        if (event.getX() > down.x) {//向左滑
            if (canDrag(DRAG_RIGHT)) {//相对此view 就是向右拖动
                return false;
            }
        } else if (event.getX() < down.x) {//相对此view 就是向右拖动
            if (canDrag(DRAG_LEFT)) {
                return false;
            }
        }

        return true;
    }
}
