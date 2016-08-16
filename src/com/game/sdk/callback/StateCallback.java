package com.game.sdk.callback;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

public interface StateCallback {
    
    /**
     * 空置状态(例如欢迎界面/加载时)
     */
    public final static int IDEL = -1;
    
    /**
     * 默认状态
     */
    public final static int DEFAULT = 0;
    
    /**
     * 支付状态
     */
    public final static int PAYMENT = 1;
    
    /**
     * 游戏状态
     */
    public final static int GAMING = 2;
    
    /**
     * 试玩状态
     */
    public final static int TRIAL = 3;
    
    public void onResume(Activity activity);
    public void onPause();
    public void onActivityDestroy();
    public void onRelease();
    public void onConfigurationChanged(Configuration newConfig);
    
    /**
     * 游戏状态变化
     * @param context
     * @param state ：IDEL DEFAULT, PAYMENT, GAMING, TRIAL
     */
    public void onUserStateChanged(Context context, int state);
}
