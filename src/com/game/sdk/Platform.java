package com.game.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.SparseArray;
import com.game.sdk.activity.widget.BuoyMenu;
import com.game.sdk.bean.Purchase;
import com.game.sdk.callback.CallbackManager;
import com.game.sdk.callback.LoginCallback;
import com.game.sdk.callback.LogoutCallback;
import com.game.sdk.callback.PurchaseCallback;
import com.game.sdk.callback.StateCallback;
import com.game.sdk.config.Config;
import com.game.sdk.model.GameFloatModel;
import com.game.sdk.model.IFloatModel;
import com.game.sdk.presenter.PurchasePresenter;
import com.game.sdk.presenter.UserPresenter;

/**
 * Created by echowang on 16/4/23.
 */
public class Platform {
    private static Platform ourInstance;

    private SparseArray<StateCallback> mStateCallbacks = new SparseArray<StateCallback>();

    private int mState = StateCallback.IDEL;
    
    private final Object obj = new Object();

    public static Platform getInstance() {
        if (ourInstance == null) {
            ourInstance = new Platform();
        }
        return ourInstance;
    }

    private Platform() {

    }

    /**
     * 初始化SDK
     * @param gameId
     */
    public Platform initPlatform(Activity activity,String gameId){
        Config.setGameId(gameId);
        startFloatView(activity,true);
        PurchasePresenter.initNowPay(activity);
        return this;
    }

    /**
     * 判断用户是否登录
     * @return
     */
    public boolean isLogin(){
        return UserPresenter.isLogin();
    }

    /**
     * 用户登录
     * @param activity
     * @param callback
     */
    public void login(Activity activity,LoginCallback callback){
        CallbackManager.setLoginCallback(callback);
        UserPresenter.showLoginActiviy(activity);
    }

    /**
     * 用户支付
     * @param activity
     * @param purchase
     * @param callback
     */
    public void purchase(Activity activity, Purchase purchase, PurchaseCallback callback){
        CallbackManager.setPurchaseCallback(callback);
        PurchasePresenter.showPurchaseChannel(activity,purchase);
    }

    /**
     * 用户注销
     * @param callback
     */
    public void logout(LogoutCallback callback){
        UserPresenter.logout();
        CallbackManager.setLogoutCallback(callback);
    }

    /**
     * 显示浮标
     * @param activity
     */
    public void showMeunIcon(Activity activity){
        BuoyMenu.showBuoyMenu(activity);
    }
    
    /**
     * 显示浮标，
     * 在需要显示浮标的activity中调用
     * 请与floatResume(Activity activity)、floatPause()、floatDestroy()
     * floatConfigurationChanged(Configuration newConfig)同时使用
     * @param context
     * @param defaultPosition false: 尝试使用上次的位置
     */
    public int startFloatView(Context context, boolean defaultPosition) {
        if (context instanceof Activity) {
            ((Activity) context).getIntent().putExtra(IFloatModel.STATE_MASK, true);
//            int id = context.getResources().getInteger(Config.g);
            int id = 0;
            Point point = null;
            if (mStateCallbacks.get(id) != null) {
                if (!defaultPosition) {
                    point = ((IFloatModel) mStateCallbacks.get(id)).getLastPosition();
                }
                mStateCallbacks.get(id).onRelease();
                mStateCallbacks.remove(id);
            }
            StateCallback cb;
            if (point == null) {
                cb = new GameFloatModel((Activity) context, mState, ((Activity) context).getWindow().getDecorView().getRootView());
            } else {
                cb = new GameFloatModel((Activity) context, mState, ((Activity) context).getWindow().getDecorView().getRootView(), point);
            }
            mStateCallbacks.append(id, cb);
        }
        return mState;
    }

    /**
     * Platform Activity resume
     */
    public void floatResume(Activity activity) {
        for (int i = 0; i < mStateCallbacks.size(); i++) {
            mStateCallbacks.valueAt(i).onResume(activity);
        }
    }

    /**
     * Platform Activity pause
     */
    public void floatPause() {
        for (int i = 0; i < mStateCallbacks.size(); i++) {
            mStateCallbacks.valueAt(i).onPause();
        }
    }
    
    /**
     * Platform Activity destroy
     */
    public void floatDestroy() {
        for (int i = 0; i < mStateCallbacks.size(); i++) {
            mStateCallbacks.valueAt(i).onActivityDestroy();
        }
    }

    /**
     * Platform ConfigurationChanged
     * @param newConfig
     */
    public void floatConfigurationChanged(Configuration newConfig) {
        for (int i = 0; i < mStateCallbacks.size(); i++) {
            mStateCallbacks.valueAt(i).onConfigurationChanged(newConfig);
        }
    }
    
    /**
     * 用户行为变化(游戏中、试玩中、支付中...)
     * @see StateCallback
     * @param context;
     * @param state IDEL DEFAULT, PAYMENT, GAMING, TRIAL
     */
    public void setUserState(Context context, int state) {
        synchronized (obj) {
            if (state != mState) {
                mState = state;
                for (int i = 0; i < mStateCallbacks.size(); i++) {
                    mStateCallbacks.valueAt(i).onUserStateChanged(context, mState);
                }
            }
        }
    }

    /**
     * 退出游戏
     */
    public void exit(){
        for (int i = 0; i < mStateCallbacks.size(); i++) {
            mStateCallbacks.valueAt(i).onRelease();
        }
        mStateCallbacks.clear();
        logout(new LogoutCallback() {
		});
        ourInstance = null;
        
    }
}
