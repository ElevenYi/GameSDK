package com.game.sdk.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.game.sdk.bean.Purchase;
import com.game.sdk.presenter.PurchasePresenter;

/**
 * Created by echowang on 16/4/24.
 */
public class PurchaseChannelFragment extends PurchaseBaseFragment implements View.OnClickListener {
    private ImageButton backBtn;
    private View alipayBtn;
    private View wechatBtn;
    private View bankBtn;

    private TextView balanceText;
    private int balance = 0;
    private View otherBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutByName("fragment_purchase_channel"),null,true);
        backBtn = (ImageButton) view.findViewById(getIdByName("purchase_back"));
        alipayBtn = view.findViewById(getIdByName("purchase_alipay"));
        wechatBtn = view.findViewById(getIdByName("purchase_wechat"));
        bankBtn = view.findViewById(getIdByName("purchase_bank"));
        otherBtn = view.findViewById(getIdByName("purchase_other"));
        balanceText = (TextView) view.findViewById(getIdByName("cat_balance"));

        backBtn.setOnClickListener(this);
        alipayBtn.setOnClickListener(this);
        wechatBtn.setOnClickListener(this);
        bankBtn.setOnClickListener(this);
        otherBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        balanceText.setText(String.valueOf(balance));

        PurchasePresenter.getCoins(this);
    }

    @Override
    public void onClick(View v) {
        if (backBtn.getId() == v.getId()){
            back();
            return;
        }

        if (alipayBtn.getId() == v.getId()){
            alipay();
            return;
        }

        if (wechatBtn.getId() == v.getId()){
            wechatPay();
            return;
        }

        if (bankBtn.getId() == v.getId()){
            bankPay();
            return;
        }

        if (otherBtn.getId() == v.getId()){
            otherPay();
            return;
        }
    }

    private void back(){
        PurchasePresenter.cancelPay(this);
    }

    private void alipay(){
//        GameToast.showLoginSuccess(baseActivity,"启动支付宝");
        PurchasePresenter.alipay(this);
    }

    private void wechatPay(){
//        GameToast.showLoginSuccess(baseActivity,"启动微信");
        PurchasePresenter.webchatPay(this);
    }

    private void bankPay(){
//        GameToast.showLoginSuccess(baseActivity,"启动网银");
        PurchasePresenter.bankPay(this);
    }

    private void  otherPay(){
//        GameToast.showLoginSuccess(baseActivity,"启动其它支付");
        PurchasePresenter.coinsPay(this);
    }

    @Override
    public Purchase getPurchase() {
        Purchase purchase = (Purchase) getArguments().getSerializable("purchase");
        return purchase;
    }

    @Override
    public int getCoins() {
        return balance;
    }

    @Override
    public void setCoins(int coins) {
        super.setCoins(coins);
        balance = coins;
        balanceText.setText(String.valueOf(balance));
    }
}
