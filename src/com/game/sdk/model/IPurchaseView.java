package com.game.sdk.model;

import android.content.Context;

import com.game.sdk.bean.Purchase;

/**
 * Created by echowang on 16/5/6.
 */
public interface IPurchaseView {
    Context getContext();
    void showLoading(String msg);
    void dismissLoading();
    Purchase getPurchase();
    void closeActivity();
    void showToast(String msg);
    int getCoins();
    void setCoins(int coins);
}
