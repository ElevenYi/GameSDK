package com.game.sdk.callback;

import com.game.sdk.bean.Purchase;

/**
 * Created by echowang on 16/4/23.
 */
public interface PurchaseCallback {
    void onCancel();
    void onSuccess(String ordeId, Purchase purchase);
    void onFail(String msg);
}
