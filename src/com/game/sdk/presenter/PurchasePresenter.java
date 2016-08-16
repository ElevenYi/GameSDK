package com.game.sdk.presenter;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.game.sdk.activity.PurchaseActivity;
import com.game.sdk.bean.Purchase;
import com.game.sdk.callback.CallbackManager;
import com.game.sdk.model.IPurchaseView;
import com.game.sdk.model.PurchaseModel;

/**
 * Created by echowang on 16/4/23.
 */
public class PurchasePresenter {
    /**
     * 展示支付渠道
     * @param activity
     */
    public static void showPurchaseChannel(Activity activity,Purchase purchase){
        if (UserPresenter.isLogin()){
            Intent intent = new Intent(activity, PurchaseActivity.class);
            intent.putExtra("purchase",purchase);
            activity.startActivity(intent);
        }else{
            Toast.makeText(activity,"请先登录",Toast.LENGTH_SHORT).show();
            CallbackManager.getPurchaseCallback().onFail("未登录");
        }

    }

    /**
     * 初始化现代支付
     * @param activity
     */
    public static void initNowPay(Activity activity){
        PurchaseModel.getInstance().initNowPay(activity);
    }

    /**
     * 启动支付宝支付
     * @param purchaseView
     */
    public static void alipay(final IPurchaseView purchaseView){
        purchaseView.showLoading("正在启动支付宝支付");
        PurchaseModel.getInstance().startAlipay(purchaseView.getContext(), purchaseView.getPurchase(), new PurchaseModel.PurchaseStatusListener() {
            @Override
            public void onSuccess(String orderId,Purchase purchase) {
                purchaseView.dismissLoading();
                purchaseView.closeActivity();
                CallbackManager.getPurchaseCallback().onSuccess(orderId,purchase);
            }

            @Override
            public void onFail(String msg) {
                purchaseView.dismissLoading();
                purchaseView.closeActivity();
                CallbackManager.getPurchaseCallback().onFail(msg);
            }
        });
    }

    /**
     * 启动微信支付
     * @param purchaseView
     */
    public static void webchatPay(final IPurchaseView purchaseView){
        purchaseView.showLoading("正在启动微信支付");
        PurchaseModel.getInstance().startWeChatPay(purchaseView.getContext(), purchaseView.getPurchase(), new PurchaseModel.PurchaseStatusListener() {
            @Override
            public void onSuccess(String orderId,Purchase purchase) {
                purchaseView.dismissLoading();
                purchaseView.closeActivity();
                CallbackManager.getPurchaseCallback().onSuccess(orderId,purchase);
            }

            @Override
            public void onFail(String msg) {
                purchaseView.dismissLoading();
                purchaseView.closeActivity();
                CallbackManager.getPurchaseCallback().onFail(msg);
            }
        });
    }

    /**
     * 启动银联支付
     * @param purchaseView
     */
    public static void bankPay(final IPurchaseView purchaseView){
        purchaseView.showLoading("正在启动银联支付");
        PurchaseModel.getInstance().startBankPay(purchaseView.getContext(), purchaseView.getPurchase(), new PurchaseModel.PurchaseStatusListener() {
            @Override
            public void onSuccess(String orderId,Purchase purchase) {
                purchaseView.dismissLoading();
                purchaseView.closeActivity();
                CallbackManager.getPurchaseCallback().onSuccess(orderId,purchase);
            }

            @Override
            public void onFail(String msg) {
                purchaseView.dismissLoading();
                purchaseView.closeActivity();
                CallbackManager.getPurchaseCallback().onFail(msg);
            }
        });
    }

    public static void getCoins(final IPurchaseView purchaseView){
        purchaseView.showLoading("正在加载");
        PurchaseModel.getInstance().getCoins(purchaseView.getContext(), new PurchaseModel.PurchaseCoinsListener() {
            @Override
            public void onSuccess(int coins) {
                purchaseView.dismissLoading();
                purchaseView.setCoins(coins);
            }

            @Override
            public void onFail(String msg) {
                purchaseView.dismissLoading();
                purchaseView.setCoins(0);
            }
        });
    }

    public static void coinsPay(final IPurchaseView purchaseView){

        Purchase purchase = purchaseView.getPurchase();
        if (purchase == null){
            purchaseView.showToast("参数异常");
            return;
        }
        int coins = purchaseView.getCoins();
        if (coins < purchase.getCoins()){
            purchaseView.showToast("猫币不足,请使用其它方式！");
            return;
        }

        purchaseView.showLoading("正在支付");
        PurchaseModel.getInstance().coinsPay(purchaseView.getContext(), purchaseView.getPurchase(), new PurchaseModel.PurchaseStatusListener() {
            @Override
            public void onSuccess(String orderId, Purchase purchase) {
                purchaseView.dismissLoading();
                purchaseView.closeActivity();
                CallbackManager.getPurchaseCallback().onSuccess(orderId,purchase);
            }

            @Override
            public void onFail(String msg) {
                purchaseView.dismissLoading();
                purchaseView.closeActivity();
                CallbackManager.getPurchaseCallback().onFail(msg);
            }
        });
    }

    /**
     * 取消支付
     */
    public static void cancelPay(IPurchaseView purchaseView){
        purchaseView.closeActivity();
        CallbackManager.getPurchaseCallback().onCancel();
    }
}
