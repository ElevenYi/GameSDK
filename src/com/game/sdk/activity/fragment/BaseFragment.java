package com.game.sdk.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.game.sdk.activity.BaseActivity;

/**
 * Created by echowang on 16/4/23.
 */
public class BaseFragment extends Fragment {
    protected BaseActivity baseActivity;

    public static <T> T getFragmentByName(Context context,Class<T> clazz){
        return (T) Fragment.instantiate(context,clazz.getName());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        baseActivity = (BaseActivity) activity;
    }

    /**
     * 跳转到其它Fragment
     * @param fragment
     */
    protected void redirectFragment(Fragment fragment){
        FragmentManager fragmentManager = baseActivity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(!fragment.isAdded()){
            transaction.hide(this);
            transaction.add(getIdByName("content"),fragment);
            transaction.addToBackStack(null);
        }else{
            transaction.hide(this);
            transaction.show(fragment);
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 返回
     */
    protected void onBackPressed(){
        baseActivity.onBackPressed();
    }

    protected void finishActivity(){
        baseActivity.finish();
    }

    public int getLayoutByName(String layoutName){
        return baseActivity.getLayoutByName(layoutName);
    }

    public int getDrawableByName(String drawableName){
        return baseActivity.getDrawableByName(drawableName);
    }

    public int getIdByName(String idName){
        return baseActivity.getIdByName(idName);
    }

    public int getStringByName(String stringName){
        return baseActivity.getStringByName(stringName);
    }

    public int getStyleByName(String styleName){
        return baseActivity.getStyleByName(styleName);
    }
}
