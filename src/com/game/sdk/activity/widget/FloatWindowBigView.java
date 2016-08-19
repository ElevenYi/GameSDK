package com.game.sdk.activity.widget;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.game.sdk.R;
import com.game.sdk.activity.model.MenuItemEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Eleven on 2016/8/18.
 */
public class FloatWindowBigView extends FloatView implements View.OnClickListener {


    FloatListMenuView menuList_FLMV;
    ImageView bigIcon_IV;
    RelativeLayout menu_RL;
    RelativeLayout whole_RL;
    RelativeLayout clickGm_RL;


    int[] p = new int[2];
    boolean isExpending = false;
    public float screenWidth = 0;
    PopupWindow popupWindow;
    public String bannerUrl = "";
    public Context activityContext;


    public FloatWindowBigView(final Context context) {
        super(context);
        isSmallView = false;
    }

    @Override
    protected void initViews() {
        contentView = inflate(mContext, getLayoutByName("layout_float_window_big"), this);
        viewWidth = contentView.getLayoutParams().width;
        viewHeight = contentView.getLayoutParams().height;
        whole_RL = (RelativeLayout) contentView.findViewById(getIdByName("rl_float_big_whole"));
        menuList_FLMV = (FloatListMenuView) contentView.findViewById(getIdByName("flmv_float_menu"));
        bigIcon_IV = (ImageView) contentView.findViewById(getIdByName("iv_float_menu_big_icon"));
        menu_RL = (RelativeLayout) contentView.findViewById(getIdByName("rl_float_down_menu"));
        clickGm_RL = (RelativeLayout) contentView.findViewById(getIdByName("clickGm_RL"));
        clickGm_RL.setOnClickListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        showIcon();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        showCloseAnim();
    }

    private void showCloseAnim() {
        ObjectAnimator.ofFloat(bigIcon_IV, "alpha", 1F, 0.2F).setDuration(600).start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isExpending)
            MyWindowManager.updateLastShowBigTime(System.currentTimeMillis());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("FloatWindowSmallView", "小悬浮窗被按下");
                //手指按下时记录必要数据，纵坐标的值都需要减去状态栏的高度
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("FloatWindowSmallView", "小悬浮窗拖动");
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                //手指移动的时候更新小悬浮窗的位置
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                Log.i("FloatWindowSmallView", "小悬浮窗点击之后被弹起");
                //如果手指离开屏幕时，xDownInScreen 和 xInScreen相等，且yDownInScreen和yInScreen相等，则视为出发了单击事件
                if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
                    if (event.getX() > 0 || event.getY() > 0) {
                        if (!isExpending) {
                            showMenu();
                            isExpending = true;
                        } else {
                            showIcon();
                            isExpending = false;
                        }
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void showMenu() {
        isExpending = true;
        menu_RL.setVisibility(VISIBLE);
        //1.icon旋转360度
        ObjectAnimator.ofFloat(bigIcon_IV, "rotation", 0F, 360F).setDuration(1000).start();
        //2.menu向下渐现
        ObjectAnimator.ofFloat(menu_RL, "alpha", 0F, 1F).setDuration(1000).start();
        MyWindowManager.updateLastShowBigTime(0);
    }

    private void showIcon() {
        MyWindowManager.updateLastShowBigTime(System.currentTimeMillis());
        //1.menu向上渐隐消失
        ObjectAnimator.ofFloat(menu_RL, "alpha", 1F, 0F).setDuration(1000).start();
        //2.icon旋转360度
        ObjectAnimator.ofFloat(bigIcon_IV, "rotation", 0F, 360F).setDuration(1000).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                menu_RL.setVisibility(GONE);
            }
        }, 1000);
    }


    public void setData(List<MenuItemEntity> entities) {
        if (null != entities && entities.size() > 0)
            menuList_FLMV.setData(entities);
    }


    private void showPopupWindow() {
        FloatBannerDialogFragment fragment = new FloatBannerDialogFragment();
        fragment.show(((Activity) activityContext).getFragmentManager(), "banner");
//        FloatBannerView floatBannerView = new FloatBannerView(activityContext);
//        floatBannerView.setData(bannerUrl);
//        popupWindow = new PopupWindow(mContext);
//        popupWindow = new PopupWindow(floatBannerView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.showAtLocation(((MainActivity) activityContext).getWindow().getDecorView(), Gravity.CENTER, (int) screenWidth / 2, (int) screenWidth / 2);
    }

    @Override
    public void onClick(View v) {
        showPopupWindow();
    }
}
