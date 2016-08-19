package com.game.sdk.activity.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.game.sdk.R;
import com.game.sdk.widget.SimpleLinearLayout;

import java.lang.reflect.Field;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eleven on 2016/8/18.
 */
public class FloatWindowHideView extends SimpleLinearLayout {


    ImageView hideIcon_IV;


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


    /**
     * 记录系统状态栏的高度
     */
    public static int statusBarHeight;

    public static int viewWidth, viewHeight;

    public boolean isLeft = true;

    private MyWindowManager.MoveListener moveListener;
    private WindowManager windowManager;

    private WindowManager.LayoutParams mParams;

    public FloatWindowHideView(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public FloatWindowHideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    protected void initViews() {
        contentView = inflate(mContext, getLayoutByName("layout_float_hide"), this);
        LinearLayout view = (LinearLayout) contentView.findViewById(getIdByName("ll_float_hide_whole"));
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        hideIcon_IV  = (ImageView) contentView.findViewById(getIdByName("iv_float_hide_view_icon"));

    }

    public void setParams(WindowManager.LayoutParams params) {
        this.mParams = params;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                xInView = event.getX();
                yInView = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = isLeft ? 0 : mParams.width;
                yInScreen = event.getRawY() - getStatusBarHeight();
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
                    MyWindowManager.createBigWindow();
                    MyWindowManager.removeHideWindow();
                }
                break;
            default:
                break;
        }
        return true;
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


    /**
     * 更新小悬浮窗在屏幕中的位置
     */
    public void updateViewPosition() {
        if (null != mParams) {
            mParams.x = (int) (xInScreen - xInView);
            mParams.y = (int) (yInScreen - yInView);
            if (null != moveListener)
                moveListener.moveListener(isLeft ? 0 : mParams.width, mParams.y);
            Log.i("FloatWindowSmallView", "x坐标================>" + mParams.x);
            Log.i("FloatWindowSmallView", "y坐标================>" + mParams.y);
            windowManager.updateViewLayout(this, mParams);
        }
    }


    public void setMoveListener(MyWindowManager.MoveListener moveListener) {
        this.moveListener = moveListener;
    }

    public void setLeft(boolean left) {
        isLeft = left;
        hideIcon_IV.setImageDrawable(mContext.getResources().getDrawable(isLeft ? R.drawable.ic_float_hide : R.drawable.ic_float_hide_right));
    }
}
