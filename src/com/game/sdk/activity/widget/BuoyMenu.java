package com.game.sdk.activity.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

//import com.game.sdk.R;

/**
 * Created by echowang on 16/4/25.
 */
public class BuoyMenu extends ImageView implements View.OnTouchListener{
    private static BuoyMenu buoyMenu;
//    private static WindowManager mWindowManager;
    private static PopupWindow popupWindow;
//    private static boolean isShowing = false;

    private static GameActivityLifecycleCallbacks activityLifecycleCallbacks;

    private final static int width = 200;
    private final static int heigth = 200;
    private float startX = 0;
    private float startY = 0;

    private static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//            layoutParams.format = PixelFormat.RGBA_8888;
//            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
//            layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
//            layoutParams.width = 200;
//            layoutParams.height = 200;

            Activity activity = (Activity) msg.obj;
            popupWindow = new PopupWindow(buoyMenu,width,heigth);
            popupWindow.setTouchable(true);
            popupWindow.setFocusable(false);
            popupWindow.update();
            popupWindow.showAtLocation(activity.getWindow().getDecorView(),Gravity.LEFT | Gravity.TOP,0,50);
//            mWindowManager.addView(buoyMenu,layoutParams);

        }
    };

    public static void showBuoyMenu(Activity activity){
        if (popupWindow != null && popupWindow.isShowing()){
            return;
        }
        if (buoyMenu == null){
            buoyMenu = new BuoyMenu(activity);
//            mWindowManager = activity.getWindowManager();
        }

        if (Build.VERSION.SDK_INT >= 14){
            activityLifecycleCallbacks = new GameActivityLifecycleCallbacks(activity.getClass().getName());
            activity.getApplication().registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        }

        Message msg = new Message();
        msg.obj = activity;
        handler.sendMessageDelayed(msg,1 * 1000);
    }

    public static void dismissBuoyMenu(Activity activity){
        if (popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();
//            mWindowManager.removeView(buoyMenu);
            if (Build.VERSION.SDK_INT >= 14 && activityLifecycleCallbacks != null){
                activity.getApplication().unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
            }

        }

        if (activityLifecycleCallbacks != null){

        }
    }

    public BuoyMenu(Context context) {
        super(context);
        initMenu();
    }

    public BuoyMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMenu();
    }

    public BuoyMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMenu();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BuoyMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initMenu();
    }

    private void initMenu(){
//        this.setImageResource(R.drawable.logo);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("BUOY","" + event.getAction());
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                updateMenuPosition(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_UP:
//                updateMenuPosition(event.getX(),event.getY());
                break;
        }
            return true;
    }

    private void updateMenuPosition(float endX,float endY){
        Log.i("BUOY","startX : " + startX);
        Log.i("BUOY","startY : " + startY);
        Log.i("BUOY","endX : " + endX);
        Log.i("BUOY","endY : " + endY);

        int moveX = (int) (endX - startX);
        int moveY = (int) (endY - startY);

        moveX = Math.abs(moveX);
        moveY = Math.abs(moveY);
        if (Math.sqrt(moveX * moveX + moveY * moveY) > 100){
//            moveX = (int) (endX - startX);
//            moveY = (int) (endY - startY);
            popupWindow.update((int)endX,(int)endY,-1,-1);
            startX = endX;
            startY = endY;
        }

//        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.getLayoutParams();
//        if (startX >= this.layoutParams.x && startX <= (layoutParams.x + 200) && startY >= layoutParams.y && startY <= (layoutParams.y + 200)){
//            int dx = (int) (endX - startX);
//            int dy = (int) (endY - startY);
//
//
//            layoutParams.x = (int)startX + dx;
//            layoutParams.y = (int)startY + dy;
//
//            startX = endX;
//            startY = endY;
//            popupWindow.update();
////            mWindowManager.updateViewLayout(this,layoutParams);
//        }

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    static class GameActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks{
        private String activityName;

        public GameActivityLifecycleCallbacks(String activityName){
            this.activityName = activityName;
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (activityName.equalsIgnoreCase(activity.getClass().getName())){
                dismissBuoyMenu(activity);
            }

        }
    }
}
