package com.game.sdk.activity.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.game.sdk.R;

import java.lang.reflect.Field;
import java.util.Timer;

import butterknife.ButterKnife;

/**
 * Created by Eleven on 2016/8/18.
 */
public class FloatWindowSmallView extends FloatView {

    public FloatWindowSmallView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_float_window_small, this);
        ButterKnife.bind(this);
        View view = findViewById(R.id.ll_float_small_whole);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnim();
    }

    private void startAnim() {
        ObjectAnimator.ofFloat(FloatWindowSmallView.this,"rotation",0F,360F).setDuration(1000).start();
    }
}
