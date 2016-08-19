package com.game.sdk.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.game.sdk.PlatFormNew;
import com.game.sdk.Platform;
import com.game.sdk.model.GameFloatModel;

/**
 * Created by echowang on 16/4/24.
 */
public class AccountBindFragment extends UserBaseFragment implements View.OnClickListener {
//    private ImageButton backBtn;
    private Button bindBtn;
    private RelativeLayout closeBtn;
    private View qqBtn;
    private View weiboBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutByName("fragment_account_bind"),null,true);
//        backBtn = (ImageButton) view.findViewById(getIdByName("bind_back"));
        closeBtn = (RelativeLayout) view.findViewById(getIdByName("rl_fragment_account_bind_close"));
        bindBtn = (Button) view.findViewById(getIdByName("bind_account"));
        qqBtn = view.findViewById(getIdByName("bind_qq"));
        weiboBtn = view.findViewById(getIdByName("bind_weibo"));

//        backBtn.setOnClickListener(this);
        bindBtn.setOnClickListener(this);
        qqBtn.setOnClickListener(this);
        weiboBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
//        if (backBtn.getId() == v.getId()){
//            back();
//            return;
//        }

        if (bindBtn.getId() == v.getId()){
            bindAccount();
            return;
        }

        if (qqBtn.getId() == v.getId()){
            qqLogin();
            return;
        }

        if (weiboBtn.getId() == v.getId()){
        	weiboLogin();
            return;
        }

        if (closeBtn.getId() == v.getId()){
            back();
            return;
        }
    }

    private void back(){
//        GameToast.showLoginSuccess(baseActivity,"登录成功");
        PlatFormNew.getInstance().setUserState(getContext(), GameFloatModel.TRIAL);
        finishActivity();
    }

    private void bindAccount(){
//        GameToast.showLoginSuccess(baseActivity,"登录成功");
//        finishActivity();
        AccountRegisterFragment accountRegisterFragment = AccountRegisterFragment.getFragmentByName(baseActivity,AccountRegisterFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("type","bind");
        accountRegisterFragment.setArguments(bundle);
        redirectFragment(accountRegisterFragment);
    }

    private void qqLogin(){
//        GameToast.showLoginSuccess(baseActivity,"登录成功");
//        finishActivity();
        ThirdLoginFragment thirdLoginFragment = ThirdLoginFragment.getFragmentByName(baseActivity,ThirdLoginFragment.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type",ThirdLoginFragment.QQ);
        bundle.putInt("action",ThirdLoginFragment.BIND);
        thirdLoginFragment.setArguments(bundle);
        redirectFragment(thirdLoginFragment);
    }

    private void weiboLogin(){
//        GameToast.showLoginSuccess(baseActivity,"登录成功");
//        finishActivity();
        ThirdLoginFragment thirdLoginFragment = ThirdLoginFragment.getFragmentByName(baseActivity,ThirdLoginFragment.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type",ThirdLoginFragment.SINA);
        bundle.putInt("action",ThirdLoginFragment.BIND);
        thirdLoginFragment.setArguments(bundle);
        redirectFragment(thirdLoginFragment);
    }
}
