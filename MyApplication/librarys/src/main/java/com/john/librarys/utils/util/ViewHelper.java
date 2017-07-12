package com.john.librarys.utils.util;

/**
 * Created by 红哥哥 on 2016/7/21.
 */
import android.view.View;

import static com.john.librarys.utils.util.AnimatorProxy.NEEDS_PROXY;
import static com.john.librarys.utils.util.AnimatorProxy.wrap;

public final class ViewHelper {
    private ViewHelper() {
    }

    public static float getAlpha(View view) {
        return NEEDS_PROXY? wrap(view).getAlpha():view.getAlpha();
    }

    public static void setAlpha(View view, float alpha) {
        if(NEEDS_PROXY) {
            wrap(view).setAlpha(alpha);
        } else {
            view.setAlpha(alpha);
        }

    }

    public static float getPivotX(View view) {
        return NEEDS_PROXY? wrap(view).getPivotX():view.getPivotX();
    }

    public static void setPivotX(View view, float pivotX) {
        if(NEEDS_PROXY) {
            wrap(view).setPivotX(pivotX);
        } else {
            view.setPivotX(pivotX);
        }

    }

    public static float getPivotY(View view) {
        return NEEDS_PROXY? wrap(view).getPivotY():view.getPivotY();
    }

    public static void setPivotY(View view, float pivotY) {
        if(NEEDS_PROXY) {
            wrap(view).setPivotY(pivotY);
        } else {
            view.setPivotY(pivotY);
        }

    }

    public static float getRotation(View view) {
        return NEEDS_PROXY? wrap(view).getRotation():view.getRotation();
    }

    public static void setRotation(View view, float rotation) {
        if(NEEDS_PROXY) {
            wrap(view).setRotation(rotation);
        } else {
            view.setRotation(rotation);
        }

    }

    public static float getRotationX(View view) {
        return NEEDS_PROXY? wrap(view).getRotationX():view.getRotationX();
    }

    public static void setRotationX(View view, float rotationX) {
        if(NEEDS_PROXY) {
            wrap(view).setRotationX(rotationX);
        } else {
            view.setRotationX(rotationX);
        }

    }

    public static float getRotationY(View view) {
        return NEEDS_PROXY? wrap(view).getRotationY():view.getRotationY();
    }

    public static void setRotationY(View view, float rotationY) {
        if(NEEDS_PROXY) {
            wrap(view).setRotationY(rotationY);
        } else {
            view.setRotationY(rotationY);
        }

    }

    public static float getScaleX(View view) {
        return NEEDS_PROXY? wrap(view).getScaleX():view.getScaleX();
    }

    public static void setScaleX(View view, float scaleX) {
        if(NEEDS_PROXY) {
            wrap(view).setScaleX(scaleX);
        } else {
            view.setScaleX(scaleX);
        }

    }

    public static float getScaleY(View view) {
        return NEEDS_PROXY? wrap(view).getScaleY():view.getScaleY();
    }

    public static void setScaleY(View view, float scaleY) {
        if(NEEDS_PROXY) {
            wrap(view).setScaleY(scaleY);
        } else {
            view.setScaleY(scaleY);
        }

    }

    public static float getScrollX(View view) {
        return NEEDS_PROXY?(float) wrap(view).getScrollX():ViewHelper.Honeycomb.getScrollX(view);
    }

    public static void setScrollX(View view, int scrollX) {
        if(NEEDS_PROXY) {
            wrap(view).setScrollX(scrollX);
        } else {
            view.setScrollX(scrollX);
        }

    }

    public static float getScrollY(View view) {
        return NEEDS_PROXY?(float) wrap(view).getScrollY():ViewHelper.Honeycomb.getScrollY(view);
    }

    public static void setScrollY(View view, int scrollY) {
        if(NEEDS_PROXY) {
            wrap(view).setScrollY(scrollY);
        } else {
            view.setScrollY(scrollY);
        }

    }

    public static float getTranslationX(View view) {
        return NEEDS_PROXY? wrap(view).getTranslationX():view.getTranslationX();
    }

    public static void setTranslationX(View view, float translationX) {
        if(NEEDS_PROXY) {
            wrap(view).setTranslationX(translationX);
        } else {
            view.setTranslationX(translationX);
        }

    }

    public static float getTranslationY(View view) {
        return NEEDS_PROXY? wrap(view).getTranslationY():view.getTranslationY();
    }

    public static void setTranslationY(View view, float translationY) {
        if(NEEDS_PROXY) {
            wrap(view).setTranslationY(translationY);
        } else {
            view.setTranslationY(translationY);
        }

    }

    public static float getX(View view) {
        return NEEDS_PROXY? wrap(view).getX():view.getX();
    }

    public static void setX(View view, float x) {
        if(NEEDS_PROXY) {
            wrap(view).setX(x);
        } else {
            view.setX(x);
        }

    }

    public static float getY(View view) {
        return NEEDS_PROXY? wrap(view).getY():view.getY();
    }

    public static void setY(View view, float y) {
        if(NEEDS_PROXY) {
            wrap(view).setY(y);
        } else {
            view.setY(y);
        }

    }

    private static final class Honeycomb {
        private Honeycomb() {
        }

        static float getAlpha(View view) {
            return view.getAlpha();
        }

        static void setAlpha(View view, float alpha) {
            view.setAlpha(alpha);
        }

        static float getPivotX(View view) {
            return view.getPivotX();
        }

        static void setPivotX(View view, float pivotX) {
            view.setPivotX(pivotX);
        }

        static float getPivotY(View view) {
            return view.getPivotY();
        }

        static void setPivotY(View view, float pivotY) {
            view.setPivotY(pivotY);
        }

        static float getRotation(View view) {
            return view.getRotation();
        }

        static void setRotation(View view, float rotation) {
            view.setRotation(rotation);
        }

        static float getRotationX(View view) {
            return view.getRotationX();
        }

        static void setRotationX(View view, float rotationX) {
            view.setRotationX(rotationX);
        }

        static float getRotationY(View view) {
            return view.getRotationY();
        }

        static void setRotationY(View view, float rotationY) {
            view.setRotationY(rotationY);
        }

        static float getScaleX(View view) {
            return view.getScaleX();
        }

        static void setScaleX(View view, float scaleX) {
            view.setScaleX(scaleX);
        }

        static float getScaleY(View view) {
            return view.getScaleY();
        }

        static void setScaleY(View view, float scaleY) {
            view.setScaleY(scaleY);
        }

        static float getScrollX(View view) {
            return (float)view.getScrollX();
        }

        static void setScrollX(View view, int scrollX) {
            view.setScrollX(scrollX);
        }

        static float getScrollY(View view) {
            return (float)view.getScrollY();
        }

        static void setScrollY(View view, int scrollY) {
            view.setScrollY(scrollY);
        }

        static float getTranslationX(View view) {
            return view.getTranslationX();
        }

        static void setTranslationX(View view, float translationX) {
            view.setTranslationX(translationX);
        }

        static float getTranslationY(View view) {
            return view.getTranslationY();
        }

        static void setTranslationY(View view, float translationY) {
            view.setTranslationY(translationY);
        }

        static float getX(View view) {
            return view.getX();
        }

        static void setX(View view, float x) {
            view.setX(x);
        }

        static float getY(View view) {
            return view.getY();
        }

        static void setY(View view, float y) {
            view.setY(y);
        }
    }
}
