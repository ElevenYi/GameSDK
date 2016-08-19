package com.game.sdk.activity.widget;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.game.sdk.MainApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Eleven on 2016/8/18.
 */
public class FloatWindowService extends Service {

    /**
     * 用于在线程中创建或移除悬浮窗
     */
    private Handler handler = new Handler();

    /**
     * 定时器，定时进行检测当前应该创建还是移除悬浮窗
     */
    private Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启定时器，每隔0.5s刷新一次
        if (timer == null) {
            MyWindowManager.setMainActivityContext(getMainActivityContext());
            MyWindowManager.setContext(getApplicationContext());
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyWindowManager.clearAllFloatView();
        //Service被终止的同时也停止定时器继续运行
        timer.cancel();
        timer = null;
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            //判断是否为登录成功的状态，只有在登录成功的状态下才会显示这个浮窗
            if (!MyWindowManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowManager.createHideWindow(true);
                    }
                });
            } else {
                if (isHome()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MyWindowManager.clearAllFloatView();
                        }
                    });
                }
                if (MyWindowManager.shouldShowHidView()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MyWindowManager.showHideView();
                        }
                    });
                }
            }
        }
    }

    /**
     * 判断当前界面是否是桌面
     *
     * @return
     */
    private boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }


    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfos) {
            names.add(resolveInfo.activityInfo.packageName);
        }
        return names;
    }

    private Context getMainActivityContext(){
        return ((MainApplication)getApplication()).getMainActivityContext();
    }
}
