package com.game.sdk.model;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.game.network.HttpListener;
import com.game.network.HttpProxy;
import com.game.sdk.PlatFormNew;
import com.game.sdk.Platform;
//import com.game.sdk.R;
import com.game.sdk.bean.User;
import com.game.sdk.callback.StateCallback;
import com.game.sdk.config.Config;
import com.game.sdk.presenter.UserPresenter;
import com.game.sdk.widget.FloatContainer;
import com.game.sdk.widget.GameFloatIcon;
import com.game.sdk.widget.GameFloatPanel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;
import android.widget.PopupWindow;

/**
 * 
 * @author Wilfredo Liu
 *
 */
public class GameFloatModel implements IFloatModel, StateCallback {

    private int mState;

    private int mLastState;

    public final static String TAG = "GameFloatModel";

    private final static int INVALID_VALUE = Integer.MIN_VALUE;

    // FloatModel won't hold the activity instance,
    // private Activity mContext;

    private WindowManager mWm = null;

    private Point mDisplaySize;

    private FloatContainer mContainer;
    private int mFloatX = INVALID_VALUE;
    private int mFloatY = INVALID_VALUE;

    private PopupWindow mPopupWindow;
    private Rect mDisplayFrame;
    private boolean mOnTheLeft = false;

    private WeakReference<View> mParentHolder;

    private SparseArray<Bundle> mPanelInfos;
    
    public final static String KEY_ID = "id";
    public final static String KEY_TITLE = "title";
    public final static String KEY_URL = "url";
    public final static String KEY_ISHOT = "ishot";

    public GameFloatModel(Context context, View parent) {
        this(context, DEFAULT, parent);
    }

    public GameFloatModel(Context context, int state, View parent) {
        this(context, state, parent, null);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public GameFloatModel(Context context, int state, View parent, Point point) {
        super();
        if (point != null) {
            mFloatX = point.x;
            mFloatY = point.y;
        }
        mParentHolder = new WeakReference<View>(parent);
        mState = state;
        mLastState = mState;
        mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplaySize = new Point();
        if (Build.VERSION.SDK_INT >= 13) {
            mWm.getDefaultDisplay().getSize(mDisplaySize);
        } else {
            mDisplaySize.x = mWm.getDefaultDisplay().getWidth();
            mDisplaySize.y = mWm.getDefaultDisplay().getHeight();
        }
        mDisplayFrame = new Rect();
        mContainer = new FloatContainer(context, this);
        mPopupWindow = new PopupWindow(context, null, 0);
        mPopupWindow.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setContentView(mContainer);
        if (mFloatX == INVALID_VALUE || mFloatY == INVALID_VALUE) {
            updateState(true);
        } else {
            updateState(false);
        }
        mPanelInfos = new SparseArray<Bundle>();
    }

    private void updateState(boolean isInit) {
        switch (mState) {
        case TRIAL:
            switchFloatView(FLAG_TRIAL, isInit);
            break;
        case GAMING:
        case DEFAULT:
            switchFloatView(FLAG_NORMAL, isInit);
            break;
        default:
            switchFloatView(FLAG_DISMISS, isInit);
            break;
        }
    }

    @Override
    public void onResume(Activity activity) {
        if (activity.equals(mContainer.getContext())) {
            if (mState != PAYMENT && mState != IDEL && !mPopupWindow.isShowing()) {
                if (mContainer.getFloatView() == null) {
                    updateState(true);
                } else if (mLastState != mState) {
                    mLastState = mState;
                    updateState(false);
                } else {
                    showFloatView(mFloatX - (mContainer.getFloatView().width() >> 1),
                            mFloatY - (mContainer.getFloatView().height() >> 1));
                }
            }
        } else {
            Log.i(TAG, "resumed activity is not the same, we try to reload popup!!");
            mState = PlatFormNew.getInstance().startFloatView(activity, false);
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onDestroy");
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    @Override
    public void onActivityDestroy() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        mContainer.removeAllViews();
    }

    @Override
    public void onRelease() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
        mContainer.removeAllViews();
        mContainer = null;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged");
        updateDisplayFrame();
        if (Build.VERSION.SDK_INT >= 13) {
            mWm.getDefaultDisplay().getSize(mDisplaySize);
        } else {
            mDisplaySize.x = mWm.getDefaultDisplay().getWidth();
            mDisplaySize.y = mWm.getDefaultDisplay().getHeight();
        }
    }

    @Override
    public void onUserStateChanged(Context context, int state) {
        Log.d(TAG, "onUserStateChanged state:" + state);
        if (mState == state) {
            return;
        }
        mState = state;
        if (!(context instanceof Activity)) {
            return;
        }
        if (!((Activity) context).getIntent().getBooleanExtra(STATE_MASK, false)) {
            return;
        }
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        if (mPopupWindow.getContentView().getContext().equals(context)) {
            updateState(true);
        } else {
            Log.i(TAG, "calling activity is not the same, we try to reload popup!!");
            PlatFormNew.getInstance().startFloatView((Activity) context, false);
        }
    }

    private void showFloatView() {
        showFloatView(mFloatX = 0, mFloatY = mDisplaySize.y / 2);
    }

    private void showFloatView(final int x, final int y) {
        Log.d(TAG, "showFloatView x:" + x + " y:" + y);
        mOnTheLeft = false;
        updateDisplayFrame();
        final View parent = mParentHolder == null ? null : mParentHolder.get();
        if (parent != null) {
            parent.post(new Runnable() {
                @SuppressLint("RtlHardcoded")
                @Override
                public void run() {
                    mPopupWindow.setContentView(mContainer);
                    mPopupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, x, y);
                }
            });
        } else {
            Log.d(TAG, "parent has gone, no need to inflat popup.");
            onRelease();
        }
    }

    public void setOnTheLeft(boolean left) {
        mOnTheLeft = left;
    }

    public boolean isOnTheLeft() {
        return mOnTheLeft;
    }

    @Override
    public void updateFloatViewXY(float rawX, float rawY) {
        mFloatX = (int) (rawX) + (mContainer.getFloatView().width() >> 1);
        mFloatY = (int) (rawY) + (mContainer.getFloatView().height() >> 1);
        mPopupWindow.update((int) (rawX), (int) (rawY), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
    }

    @Override
    public void updateDisplayFrame() {
        mContainer.getWindowVisibleDisplayFrame(mDisplayFrame);
    }

    @Override
    public Rect getDisplayFrame() {
        return mDisplayFrame;
    }

    @Override
    public void switchFloatView(int flag, final boolean isInit) {
        switch (flag) {
        case FLAG_NORMAL:
            final GameFloatIcon iconNormal = new GameFloatIcon(mContainer.getContext(), flag, this);
            iconNormal.setImageResource(Config.getDrawableByName(mContainer.getContext(),"float_icon_default"));
            mContainer.addView(iconNormal);
            mContainer.setOnFloatViewClickListener(iconNormal);
            showFloatView(isInit);
            mContainer.post(new Runnable() {
                @Override
                public void run() {
                    iconNormal.startTranslationAnimation(mFloatX);
                }
            });
            mLastState = mState;
            break;
        case FLAG_TRIAL:
            final GameFloatIcon iconTrial = new GameFloatIcon(mContainer.getContext(), flag, this);
            iconTrial.setImageResource(Config.getDrawableByName(mContainer.getContext(),"float_icon_trial"));
            mContainer.addView(iconTrial);
            mContainer.setOnFloatViewClickListener(iconTrial);
            showFloatView(isInit);
            mContainer.post(new Runnable() {
                @Override
                public void run() {
                    iconTrial.startTranslationAnimation(mFloatX);
                    iconTrial.startScaleAnimation(false, false);
                }
            });
            mLastState = mState;
            break;
        case FLAG_PANEL:
            loadPanelInfo(isInit, mContainer.getContext(), Config.HOST, "link.view");
            break;
        default:
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            break;
        }

    }

    private void showFloatView(boolean isInit) {
        if (isInit) {
            showFloatView();
        } else {
            int x = mFloatX - (mContainer.getFloatView().width() >> 1);
            int y = mFloatY - (mContainer.getFloatView().height() >> 1);
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            showFloatView(x, y);
        }
    }

    @Override
    public Point getLastPosition() {
        return new Point(mFloatX, mFloatY);
    }

    @Override
    public void loadPanelInfo(final boolean isInit, final Context context, String url, String action) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("action", action);
        HttpProxy.post(context, url, params, false, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    boolean status = response.getBoolean("status");
                    if(status){
                        mPanelInfos.clear();
                        Iterator<String> iterator = response.keys();
                        while (iterator.hasNext()) {
                            String name = iterator.next();
                            try {
                                JSONObject obj = response.getJSONObject(name);
                                if (obj != null) {
                                    Bundle bundle = new Bundle();
                                    int key = obj.getInt(KEY_ID);
                                    bundle.putInt(KEY_ID, key);
                                    bundle.putString(KEY_TITLE, obj.getString(KEY_TITLE));
                                    bundle.putString(KEY_URL, getUrlWithQueryString(obj.getString(KEY_URL)));
                                    bundle.putBoolean(KEY_ISHOT, 1 == obj.getInt(KEY_ISHOT));
                                    mPanelInfos.put(key, bundle);
                                } else {
                                    //
                                }
                            } catch (JSONException e) {
                                //
                            }
                        }
                        GameFloatPanel panel = new GameFloatPanel(context, GameFloatModel.this);
                        //panel.setModel(GameFloatModel.this);
                        mContainer.addView(panel);
                        mContainer.setOnFloatViewClickListener(panel);
                        showFloatView(isInit);
                        mLastState = mState;
                    } else {
                        //
                    }
                } catch (JSONException e) {
                    //
                }
            }

            @Override
            public void onFail(String msg) {
                // TODO Auto-generated method stub
                
            }
        });
    }

    @Override
    public SparseArray<Bundle> getPanelInfo() {
        return mPanelInfos;
    }

    private String getUrlWithQueryString(String url){
        if (TextUtils.isEmpty(url)){
            return Config.HOST1;
        }
        User user = UserPresenter.getUser();
        if (user == null){
            return url;
        }

        url = url.replace("{sid}",user.getSid());

        return url;
    }
}
