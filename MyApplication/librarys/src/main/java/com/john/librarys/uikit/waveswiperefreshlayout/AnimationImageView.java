package com.john.librarys.uikit.waveswiperefreshlayout;

import android.content.Context;
import android.view.animation.Animation;

/**
 * @author John Gu
 * @date 2017/11/22.
 */

public class AnimationImageView extends android.support.v7.widget.AppCompatImageView {

    /**
     * AnimationのStartとEnd時にListenerにアレする
     */
    private Animation.AnimationListener mListener;

    /**
     * コンストラクタ
     * {@inheritDoc}
     */
    public AnimationImageView(Context context) {
        super(context);
    }

    /**
     * {@link AnimationImageView#mListener} のセット
     *
     * @param listener {@link android.view.animation.Animation.AnimationListener}
     */
    public void setAnimationListener(Animation.AnimationListener listener) {
        mListener = listener;
    }

    /**
     * ViewのAnimationのStart時にセットされたListenerの {@link android.view.animation.Animation.AnimationListener#onAnimationStart(Animation)}
     * を呼ぶ
     */
    @Override public void onAnimationStart() {
        super.onAnimationStart();
        if (mListener != null) {
            mListener.onAnimationStart(getAnimation());
        }
    }

    /**
     * ViewのAnimationのEnd時にセットされたListenerの {@link android.view.animation.Animation.AnimationListener#onAnimationEnd(Animation)}
     * (Animation)} を呼ぶ
     */
    @Override public void onAnimationEnd() {
        super.onAnimationEnd();
        if (mListener != null) {
            mListener.onAnimationEnd(getAnimation());
        }
    }
}
