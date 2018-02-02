package com.john.myapplication.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.john.librarys.uikit.activity.BaseActivity;
import com.john.myapplication.R;
import com.zph.glpanorama.GLPanorama;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author John Gu
 * @date 2017/11/23.
 * open GL 全景图像
 */

public class OpenGlActivity extends BaseActivity{

    @Bind(R.id.mGLPanorama)
    GLPanorama mGLPanorama;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gl);
        ButterKnife.bind(this);
        //传入全景图片
        mGLPanorama.setGLPanorama(R.drawable.imgbug);
//        mGLPanorama.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Toast.makeText(mContext,"点了X"+ motionEvent.getX()+ "Y"+ motionEvent.getY(),Toast.LENGTH_LONG).show();
//                return false;
//            }
//        });
    }
}
