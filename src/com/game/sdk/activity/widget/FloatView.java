package com.game.sdk.activity.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.game.sdk.widget.SimpleLinearLayout;

import java.lang.reflect.Field;

/**
 * Created by Eleven on 2016/8/18.
 */
public class FloatView extends SimpleLinearLayout {

    public boolean isSmallView = true;
    /**
     * 记录小浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录小浮窗的高度
     */
    public static int viewHeight;

    /**
     * 记录系统状态栏的高度
     */
    public static int statusBarHeight;

    /**
     * 用于更新小悬浮窗的位置
     */
    public WindowManager windowManager;

    /**
     * 悬浮窗的参数
     */
    public WindowManager.LayoutParams mParams;

    /**
     * 记录手指按下时在屏幕上的横坐标值
     */
    public float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标值
     */
    public float yDownInScreen;

    /**
     * 记录当前手指按在屏幕上的横坐标值
     */
    public float xInScreen;

    /**
     * 记录当前手指按在屏幕上的横坐标值
     */
    public float yInScreen;


    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标值
     */
    public float xInView;

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标值
     */
    public float yInView;

    public Context mContext;


    public MyWindowManager.MoveListener moveListener;


    public FloatView(Context context) {
        super(context);
        this.mContext = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }


    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置
     *
     * @param params 小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }


    /**
     * 更新小悬浮窗在屏幕中的位置
     */
    public void updateViewPosition() {
        if (null!=mParams){
            mParams.x = (int) (xInScreen - xInView);
            mParams.y = (int) (yInScreen - yInView);
            if (null != moveListener)
                moveListener.moveListener(mParams.x, mParams.y);
            Log.i("FloatWindowSmallView", "x坐标================>" + mParams.x);
            Log.i("FloatWindowSmallView", "y坐标================>" + mParams.y);
            windowManager.updateViewLayout(this, mParams);
        }
    }

    public int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

        }
        return statusBarHeight;
    }


    public void setMoveListener(MyWindowManager.MoveListener moveListener) {
        this.moveListener = moveListener;
    }

}
