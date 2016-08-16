package com.game.sdk.activity;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.game.sdk.Platform;
import com.game.sdk.config.Config;

/**
 * Created by echowang on 16/4/23.
 */
public class BaseActivity extends FragmentActivity{
    private FragmentManager fragmentManager;
//    private ProgressDialog dialog;
    private View loadingView;
    private TextView loadingMsgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutByName("activity_fragment"));
        fragmentManager = getSupportFragmentManager();

        loadingView = findViewById(getIdByName("loaing"));
        loadingMsgView = (TextView) findViewById(getIdByName("loading_msg"));
    }

    protected void initFragment(Fragment fragment){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(getIdByName("content"),fragment);
//        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 0){
            finish();
        }else{
            fragmentManager.popBackStack();
        }
    }

    public void showLoading(final String msg) {
//        if(dialog == null){
//            dialog = new ProgressDialog(this);
//            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        }

//        dialog.setTitle(msg);
//        if (!dialog.isShowing()){
//            dialog.show();
//        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingView.setVisibility(View.VISIBLE);
                loadingMsgView.setText(msg);
            }
        });

    }

    public void dismissLoading() {
//        if (dialog != null && dialog.isShowing()){
//            dialog.dismiss();
//        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingView.setVisibility(View.GONE);
            }
        });

    }

    public int getLayoutByName(String layoutName){
        return Config.getLayoutByName(this,layoutName);
//        return getResources().getIdentifier(layoutName,"layout",getPackageName());
    }

    public int getDrawableByName(String drawableName){
        return Config.getDrawableByName(this,drawableName);
//        return getResources().getIdentifier(drawableName,"drawable",getPackageName());
    }

    public int getIdByName(String idName){
        return Config.getIdByName(this,idName);
//        return getResources().getIdentifier(idName,"id",getPackageName());
    }

    public int getStringByName(String stringName){
        return Config.getStringByName(this,stringName);
//        return getResources().getIdentifier(stringName,"string",getPackageName());
    }

    public int getStyleByName(String styleName){
        return Config.getStyleByName(this,styleName);
//        return getResources().getIdentifier(styleName,"style",getPackageName());
    }
}
