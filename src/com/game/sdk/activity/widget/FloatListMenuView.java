package com.game.sdk.activity.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.game.sdk.R;
import com.game.sdk.activity.model.MenuItemEntity;
import com.game.sdk.widget.SimpleLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Eleven on 2016/8/18.
 */
public class FloatListMenuView extends SimpleLinearLayout {

    ListView list_LV;
    ListAdapter adapter;

    public FloatListMenuView(Context context) {
        super(context);
    }

    public FloatListMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initViews() {
        contentView = inflate(mContext, getLayoutByName("layout_float_list_menu"), this);
        list_LV = (ListView) contentView.findViewById(getIdByName("lv_float_list_menu_lv"));
        initParams();
    }

    private void initParams() {
        adapter = new ListAdapter(mContext);
        list_LV.setAdapter(adapter);
    }

    public void setData(List<MenuItemEntity> entityList) {
        adapter.setmAdapterItems(entityList);
    }

    class ListAdapter extends BaseAdapter {
        Context mContext;
        List<MenuItemEntity> mDatas = new ArrayList<>();

        public ListAdapter(Context context) {
            mContext = context;
            this.mDatas.clear();
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListMenuItemView itemView;
            if (null == convertView) {
                itemView = new ListMenuItemView(mContext);
                convertView = itemView;
                itemView.setTag(convertView);
            } else
                itemView = (ListMenuItemView) convertView.getTag();
            itemView.setData(mDatas.get(position), position == mDatas.size() - 1);
            return convertView;
        }

        public void setmAdapterItems(List<MenuItemEntity> entities) {
            this.mDatas.clear();
            this.mDatas.addAll(entities);
            this.notifyDataSetChanged();
        }
    }
}
