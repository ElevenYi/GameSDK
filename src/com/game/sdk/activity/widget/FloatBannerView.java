package com.game.sdk.activity.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.game.sdk.R;
import com.game.sdk.widget.SimpleLinearLayout;

/**
 * Created by Eleven on 2016/8/19.
 */
public class FloatBannerView extends SimpleLinearLayout {

    ImageView banner_IV;


    /**
     * 记录广告浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录广告浮窗的高度
     */
    public static int viewHeight;

    public FloatBannerView(Context context) {
        super(context);
    }

    public FloatBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initViews() {
        contentView = inflate(mContext, getLayoutByName("layout_float_banner"), this);
        banner_IV = (ImageView) contentView.findViewById(getIdByName("iv_float_banner_image"));
    }

    public void setData(final String url) {
        banner_IV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }
}
