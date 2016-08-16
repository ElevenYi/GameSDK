package com.game.sdk.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.game.sdk.presenter.UserPresenter;

/**
 * 登录选择界面
 * Created by echowang on 16/4/23.
 */
public class LoginFragment extends UserBaseFragment implements OnClickListener {
    private Button guestBtn;
    private View qqLoginBtn;
    private View weiboLoginBtn;
    private Button accountLoginBtn;
    private Button accountRegisterBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutByName("fragment_login"),null,true);
        guestBtn = (Button) view.findViewById(getIdByName("login_guest"));
        qqLoginBtn = view.findViewById(getIdByName("login_qq"));
        weiboLoginBtn = view.findViewById(getIdByName("login_weibo"));
        accountLoginBtn = (Button) view.findViewById(getIdByName("login_account"));
        accountRegisterBtn = (Button) view.findViewById(getIdByName("login_register"));

        guestBtn.setOnClickListener(this);
        qqLoginBtn.setOnClickListener(this);
        weiboLoginBtn.setOnClickListener(this);
        accountLoginBtn.setOnClickListener(this);
        accountRegisterBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == guestBtn.getId()){
            guestLogin();
            return;
        }

        if(v.getId() == qqLoginBtn.getId()){
            qqLogin();
            return;
        }

        if(v.getId() == weiboLoginBtn.getId()){
            weiboLogin();
            return;
        }

        if(v.getId() == accountLoginBtn.getId()){
            accountLogin();
            return;
        }

        if (v.getId() == accountRegisterBtn.getId()){
            accountRegister();
            return;
        }
    }

    /**
     * 游客登录
     */
    private void guestLogin(){
//        AccountBindFragment bindFragment = (AccountBindFragment) AccountBindFragment.instantiate(baseActivity,AccountBindFragment.class.getName());
//        redirectFragment(bindFragment);
        UserPresenter.fastLogin(this);
    }

    /**
     * 跳转到QQ登录界面
     */
    private void qqLogin(){
//        GameToast.showLoginSuccess(baseActivity,"登录成功");
        ThirdLoginFragment thirdLoginFragment = ThirdLoginFragment.getFragmentByName(baseActivity,ThirdLoginFragment.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type",ThirdLoginFragment.QQ);
        bundle.putInt("action",ThirdLoginFragment.LOGIN);
        thirdLoginFragment.setArguments(bundle);
        redirectFragment(thirdLoginFragment);
//        finishActivity();
    }

    /**
     * 跳转到微博登录界面
     */
    private void weiboLogin(){
//        GameToast.showLoginSuccess(baseActivity,"登录成功");
//        finishActivity();
        ThirdLoginFragment thirdLoginFragment = ThirdLoginFragment.getFragmentByName(baseActivity,ThirdLoginFragment.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type",ThirdLoginFragment.SINA);
        bundle.putInt("action",ThirdLoginFragment.LOGIN);
        thirdLoginFragment.setArguments(bundle);
        redirectFragment(thirdLoginFragment);
    }

    /**
     * 跳转到帐号登录界面
     */
    private void accountLogin(){
        AccountLoginFragment accountLoginFragment = AccountLoginFragment.getFragmentByName(baseActivity,AccountLoginFragment.class);
        redirectFragment(accountLoginFragment);
    }

    /**
     * 跳转到注册界面
     */
    private void accountRegister(){
        AccountRegisterFragment accountRegisterFragment = AccountRegisterFragment.getFragmentByName(baseActivity,AccountRegisterFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("type","register");
        accountRegisterFragment.setArguments(bundle);
        redirectFragment(accountRegisterFragment);
    }


}
