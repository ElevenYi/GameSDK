package com.game.sdk.activity.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.sdk.R;
import com.game.sdk.activity.model.MenuItemEntity;
import com.game.sdk.widget.SimpleLinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eleven on 2016/8/18.
 */
public class ListMenuItemView extends SimpleLinearLayout {

    ImageView icon_IV;
    TextView name_TV;
    RelativeLayout whole_RL;

    public ListMenuItemView(Context context) {
        super(context);
    }

    public ListMenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initViews() {
        contentView = inflate(mContext, getLayoutByName("adapter_item_list_menu"), this);
        icon_IV = (ImageView) contentView.findViewById(getIdByName("iv_adapter_item_float_menu_icon"));
        name_TV = (TextView) contentView.findViewById(getIdByName("iv_adapter_item_float_menu_name"));
        whole_RL = (RelativeLayout) contentView.findViewById(getIdByName("rl_adapter_item_content"));
    }

    public void setData(final MenuItemEntity entity, boolean isLast) {
        if (null != entity) {
            if (entity.id % 2 == 0)
                whole_RL.setBackgroundDrawable(mContext.getResources().getDrawable(getDrawableByName("ic_adapter_item_float_menu_light")));
            else
                whole_RL.setBackgroundDrawable(mContext.getResources().getDrawable(getDrawableByName("ic_adapter_item_float_menu_dark")));
            whole_RL.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyWindowManager.updateLastShowBigTime(0);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(entity.url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
            if (isLast) {
                if (entity.id % 2 == 0) {
                    whole_RL.setBackgroundDrawable(mContext.getResources().getDrawable(getDrawableByName("ic_adapter_item_float_menu_with_corner_light")));
                } else
                    whole_RL.setBackgroundDrawable(mContext.getResources().getDrawable(getDrawableByName("ic_adapter_item_float_menu_with_corner_dark")));
            }
            name_TV.setText(entity.name);
            if (entity.name.equals("刷新")) {
                icon_IV.setImageDrawable(mContext.getResources().getDrawable(getDrawableByName("ic_float_menu_home")));
                icon_IV.setVisibility(VISIBLE);
                return;
            } else if (entity.name.equals("礼包")) {
                icon_IV.setVisibility(VISIBLE);
                icon_IV.setImageDrawable(mContext.getResources().getDrawable(getDrawableByName("ic_float_menu_package")));
                return;
            }
        }
    }
}
