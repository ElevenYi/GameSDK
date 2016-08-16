package com.game.sdk.activity.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.game.sdk.bean.Purchase;
import com.game.sdk.model.IPurchaseView;

/**
 * Created by echowang on 16/5/7.
 */
public class PurchaseBaseFragment extends BaseFragment implements IPurchaseView {
    @Override
    public Context getContext() {
        return baseActivity;
    }

    @Override
    public void showLoading(String msg) {
        baseActivity.showLoading(msg);
    }

    @Override
    public void dismissLoading() {
        baseActivity.dismissLoading();
    }

    @Override
    public Purchase getPurchase() {
        return null;
    }

    @Override
    public void closeActivity() {
        baseActivity.finish();
    }

    @Override
    public void showToast(String msg) {
        if (TextUtils.isEmpty(msg)){
            return;
        }
        Toast.makeText(baseActivity,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getCoins() {
        return 0;
    }

    @Override
    public void setCoins(int coins) {

    }
}
