package com.game.sdk.activity.widget;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.game.sdk.activity.model.MenuItemEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eleven on 2016/8/18.
 */
public class MyWindowManager {

    private static Context mContext;
    private static Context mainActivityContext;

    /**
     * 隐藏悬浮窗View的实例
     */
    private static FloatWindowHideView hideWindow;
    /**
     * 隐藏悬浮窗的参数
     */
    private static WindowManager.LayoutParams hideWindowParams;

    /**
     * 大悬浮窗View的实例
     */
    private static FloatWindowBigView bigWindow;

    /**
     * 大悬浮窗的参数
     */
    private static WindowManager.LayoutParams bigWindowParams;

    /**
     * 大悬浮窗View的实例
     */
    private static FloatBannerView bannerWindow;

    /**
     * 大悬浮窗的参数
     */
    private static WindowManager.LayoutParams bannerWindowParams;

    /**
     * 用于控制在屏幕上的添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;

    private static int xInScreen = 0;
    private static int yInScreen = 0;
    public static boolean isLeftInScreen = true;

    public static long last_show_big_float_time = 0;
    private static int statusBarHeight;
    public static int screenWidth = 0;
    public static int screenHeight = 0;

    /**
     * 用户获取手机内存
     */
    private static ActivityManager mActivityManager;

    public static MoveListener moveLister = new MoveListener() {
        @Override
        public void moveListener(int x, int y) {
            xInScreen = x;
            yInScreen = y;
        }
    };

    public static void setMainActivityContext(Context context) {
        mainActivityContext = context;
    }

    public static void setContext(Context c) {
        mContext = c;
    }

    public static void createHideWindow(boolean isLeft) {
//        mContext = context;
        WindowManager windowManager = getWindowManager();
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (null == hideWindow) {
            hideWindow = new FloatWindowHideView(mainActivityContext);
            isLeftInScreen = isLeft;
            hideWindow.setLeft(isLeft);
            hideWindow.setMoveListener(moveLister);
            if (null == hideWindowParams) {
                hideWindowParams = new WindowManager.LayoutParams();
                hideWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                hideWindowParams.format = PixelFormat.RGBA_8888;
                hideWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                hideWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                hideWindowParams.width = FloatWindowHideView.viewWidth;
                hideWindowParams.height = FloatWindowHideView.viewHeight;
                if (isLeft) {
                    hideWindowParams.x = xInScreen = isLeft ? 0 : screenWidth;
                    hideWindowParams.y = yInScreen = yInScreen == 0 ? screenHeight / 2 - getStatusBarHeight() : yInScreen;
                } else {
                    hideWindowParams.x = screenWidth;
                    hideWindowParams.y = yInScreen;
                }

                Log.i("隐藏窗口的x坐标", hideWindowParams.x + "");
                Log.i("隐藏窗口的y坐标", hideWindowParams.y + "");
            }
            hideWindow.setParams(hideWindowParams);
            windowManager.addView(hideWindow, hideWindowParams);
            last_show_big_float_time = 0;
        }
    }

    public static void createLeftHideWindow(Context context) {
        createHideWindow(true);
    }

    public static void createRightHideWinodw(Context context) {
        createHideWindow(false);
    }


    /**
     * 移除隐藏的悬浮窗
     */
    public static void removeHideWindow() {
        if (null != hideWindow) {
            WindowManager windowManager = getWindowManager();
            windowManager.removeView(hideWindow);
            hideWindow = null;
            hideWindowParams = null;
        }
    }


    /**
     * 创建一个大悬浮窗，位置为屏幕正中间
     */
    public static void createBigWindow() {
        WindowManager windowManager = getWindowManager();
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        screenHeight = windowManager.getDefaultDisplay().getHeight();
        Log.i("这是获取到的屏幕x", screenWidth + "");
        Log.i("这是获取到的屏幕y", screenHeight + "");
        if (null == bigWindow) {
            bigWindow = new FloatWindowBigView(mainActivityContext);
            bigWindow.setMoveListener(moveLister);
            bigWindow.screenWidth = screenWidth;
            bigWindow.activityContext = mainActivityContext;
            setListMenuData();
            if (null == bigWindowParams) {
                bigWindowParams = new WindowManager.LayoutParams();
                bigWindowParams.x = xInScreen;
                bigWindowParams.y = yInScreen;
                Log.i("这是大浮窗的x坐标", bigWindowParams.x + "");
                Log.i("这是大浮窗的y坐标", bigWindowParams.y + "");
                bigWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                bigWindowParams.format = PixelFormat.RGBA_8888;
                bigWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                bigWindowParams.width = FloatWindowBigView.viewWidth;
                bigWindowParams.height = FloatWindowBigView.viewHeight;
            }
            bigWindow.setParams(bigWindowParams);
            windowManager.addView(bigWindow, bigWindowParams);
            updateLastShowBigTime(System.currentTimeMillis());
        }
    }


    /**
     * 创建一个大悬浮窗，位置为屏幕正中间
     */
    public static void createBannerWindow() {
        WindowManager windowManager = getWindowManager();
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (null == bannerWindow) {
            bannerWindow = new FloatBannerView(mainActivityContext);
            if (null == bannerWindowParams) {
                bannerWindowParams = new WindowManager.LayoutParams();
                bannerWindowParams.x = xInScreen;
                bannerWindowParams.y = yInScreen;
                bannerWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                bannerWindowParams.format = PixelFormat.RGBA_8888;
                bannerWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                bannerWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                bannerWindowParams.width = FloatBannerView.viewWidth;
                bannerWindowParams.height = FloatBannerView.viewHeight;
            }
            bigWindow.setParams(bigWindowParams);
            windowManager.addView(bigWindow, bigWindowParams);
            updateLastShowBigTime(0);
        }
    }


    public static void updateLastShowBigTime(long t) {
        last_show_big_float_time = t;
    }

    public static void showHideView() {
        if (bigWindow != null) {
            removeBigWindow();
            createHideWindow(!(xInScreen >= screenWidth / 2));
        }
    }

    /**
     * 移除大悬浮框
     */
    public static void removeBigWindow() {
        if (null != bigWindow) {
            WindowManager windowManager = getWindowManager();
            windowManager.removeView(bigWindow);
            bigWindow = null;
            bigWindowParams = null;
        }
    }


    /**
     * 是否有悬浮框（包括小悬浮窗和大悬浮窗）显示在屏幕上
     *
     * @return
     */
    public static boolean isWindowShowing() {
        return hideWindow != null || bigWindow != null;
    }


    /**
     * 删除所有的悬浮窗口
     */
    public static void clearAllFloatView() {
        if (null != hideWindow)
            removeHideWindow();
        if (null != bigWindow)
            removeBigWindow();
    }

    /**
     * 上次显示的时间是否超过2000
     *
     * @return
     */
    public static boolean shouldShowHidView() {
        return (System.currentTimeMillis() - last_show_big_float_time) >= 2000 && last_show_big_float_time > 0;
    }


    /**
     * 如果ActivityManager还未创建，则创建一个新的ActivityManager返回。否则返回当前已创建的ActivityManager。
     *
     * @param context
     * @return
     */
    private static ActivityManager getActivityManager(Context context) {
        if (null == mActivityManager) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }

    private static WindowManager getWindowManager() {
        if (null == mWindowManager) {
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    public static void setListMenuData() {
        List<MenuItemEntity> entities = new ArrayList<>();
        MenuItemEntity entity = new MenuItemEntity();
        entity.id = 0;
        entity.name = "刷新";
        entity.url = "http://www.baidu.com";
        entities.add(entity);
        MenuItemEntity entity1 = new MenuItemEntity();
        entity1.id = 1;
        entity1.name = "礼包";
        entity1.url = "http://www.163.com";
        entities.add(entity1);
        MenuItemEntity entity2 = new MenuItemEntity();
        entity2.id = 2;
        entity2.name = "联系客服";
        entity2.url = "http://www.163.com";
        entities.add(entity2);
        MenuItemEntity entity3 = new MenuItemEntity();
        entity3.id = 3;
        entity3.name = "账户管理";
        entity3.url = "http://www.163.com";
        entities.add(entity3);
        setListMenuData(entities);
    }

    public static void setListMenuData(List<MenuItemEntity> entityList) {
        if (null != bigWindow) {
            bigWindow.setData(entityList);
        }
    }

    public interface MoveListener {
        void moveListener(int x, int y);
    }


    public static int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = mContext.getResources().getDimensionPixelSize(x);
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

}
