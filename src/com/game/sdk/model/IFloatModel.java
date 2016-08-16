package com.game.sdk.model;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.SparseArray;

/**
 * 
 * @author Wilfredo Liu
 *
 */
public interface IFloatModel {
    
    public final static String STATE_MASK = "com.game.sdk.state_mask";
    
    public final static int FLAG_DISMISS = -1;
    
    public final static int FLAG_NORMAL = 0;
    
    public final static int FLAG_TRIAL = 1;
    
    public final static int FLAG_PANEL = 2;
    
    public void updateDisplayFrame();
    
    public Rect getDisplayFrame();
    
    public void updateFloatViewXY(float rawX, float rawY);
    
    public Point getLastPosition();
    
    /**
     * 切换浮标
     * @param flag FLAG_DISMISS FLAG_NORMAL FLAG_TRIAL FLAG_PANEL
     * @param isInit
     */
    public void switchFloatView(int flag , boolean isInit);
    
    public void loadPanelInfo(final boolean isInit, final Context context, String url, String action);
    
    public SparseArray<Bundle> getPanelInfo();
}
