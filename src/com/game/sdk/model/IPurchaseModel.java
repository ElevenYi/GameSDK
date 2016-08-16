package com.game.sdk.model;

import android.app.Activity;
import android.content.Context;

import com.game.sdk.bean.Purchase;

/**
 * Created by echowang on 16/4/23.
 */
public interface IPurchaseModel {
    void initNowPay(Activity activity);
    void createOrderId(Context context, String channelId, Purchase purchase);
    void startAlipay(Context context, Purchase purchase, PurchaseModel.PurchaseStatusListener listener);
    void startBankPay(Context context, Purchase purchase, PurchaseModel.PurchaseStatusListener listener);
    void startWeChatPay(Context context, Purchase purchase, PurchaseModel.PurchaseStatusListener listener);
    void getCoins(Context context,PurchaseModel.PurchaseCoinsListener listener);
    void coinsPay(Context context,Purchase purchase, PurchaseModel.PurchaseStatusListener listener);
}
