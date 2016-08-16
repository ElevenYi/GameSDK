package com.game.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.game.sdk.R;

/**
 * Created by eleven on 16/8/15.
 */
public class TextNewsView extends SimpleLinearLayout {

    public TextNewsView(Context context) {
        super(context);
    }

    public TextNewsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initViews() {
        contentView = inflate(mContext, R.layout.view_text_ad,this);
    }

    public void setData(){

    }
}
