package com.game.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

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
}
