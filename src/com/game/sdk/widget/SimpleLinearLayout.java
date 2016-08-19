package com.game.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.game.sdk.config.Config;

/**
 * Created by eleven on 16/8/15.
 */
public class SimpleLinearLayout extends LinearLayout {


    protected Context mContext;
    protected View contentView;

    public SimpleLinearLayout(Context context) {
        super(context);
        this.mContext = context;
        initViews();
    }

    public SimpleLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initViews();
    }

    protected void initViews() {

    }


    public int getLayoutByName(String layoutName){
        return Config.getLayoutByName(mContext,layoutName);
//        return getResources().getIdentifier(layoutName,"layout",getPackageName());
    }

    public int getDrawableByName(String drawableName){
        return Config.getDrawableByName(mContext,drawableName);
//        return getResources().getIdentifier(drawableName,"drawable",getPackageName());
    }

    public int getIdByName(String idName){
        return Config.getIdByName(mContext,idName);
//        return getResources().getIdentifier(idName,"id",getPackageName());
    }

    public int getStringByName(String stringName){
        return Config.getStringByName(mContext,stringName);
//        return getResources().getIdentifier(stringName,"string",getPackageName());
    }

    public int getStyleByName(String styleName){
        return Config.getStyleByName(mContext,styleName);
//        return getResources().getIdentifier(styleName,"style",getPackageName());
    }
}
