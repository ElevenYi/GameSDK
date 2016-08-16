package com.game.sdk.activity.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.sdk.config.Config;
import com.game.sdk.presenter.UserPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by echowang on 16/5/8.
 */
public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private List<String> userList = new ArrayList<String>();
    private DeleteHistoryListner deleteHistoryListner;

    public HistoryAdapter(Context context){
        this.context = context;
    }

    public void setDataSource(List<String> userList){
        if (userList != null){
            this.userList.clear();
            this.userList.addAll(userList);
            notifyDataSetChanged();
        }
    }

    public void setDeleteHistoryListner(DeleteHistoryListner deleteHistoryListner){
        this.deleteHistoryListner = deleteHistoryListner;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public String getItem(int position) {
        if (position < 0 ){
            return null;
        }

        if (position >= userList.size()){
            return null;
        }

        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView = null;
        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(Config.getLayoutByName(context,"history_item"),null);

            holderView = new HolderView();
            holderView.phoneText = (TextView) convertView.findViewById(Config.getIdByName(context,"history_phone"));
            holderView.deleteImage = (ImageView) convertView.findViewById(Config.getIdByName(context,"history_delete"));

            convertView.setTag(holderView);
        }else{
            holderView = (HolderView) convertView.getTag();
        }

        final String item = getItem(position);
        if (!TextUtils.isEmpty(item)){
            holderView.phoneText.setText(item);
            holderView.deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("HistoryAdapter","delete");
                    if (deleteHistoryListner != null){
                        deleteHistoryListner.delete(item);
                    }
//                    UserPresenter.deleteHistoryUser(context,item);
//                    userList.remove(item);
//                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }

    public interface DeleteHistoryListner{
        void delete(String phone);
    }

    class HolderView{
        TextView phoneText;
        ImageView deleteImage;
    }
}
