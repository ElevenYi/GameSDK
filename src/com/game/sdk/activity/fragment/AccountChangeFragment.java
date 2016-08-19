package com.game.sdk.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.game.sdk.bean.User;
import com.game.sdk.presenter.UserPresenter;

/**
 * Created by echowang on 16/4/24.
 */
public class AccountChangeFragment extends UserBaseFragment implements View.OnClickListener{
    
    private Button enterGameBtn;
    private View changeAccountBtn;
    private TextView userInfoText;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutByName("fragment_account_change"),null,true);
        enterGameBtn = (Button) view.findViewById(getIdByName("enter_game"));
        changeAccountBtn = view.findViewById(getIdByName("change_account"));
        userInfoText = (TextView) view.findViewById(getIdByName("user_info"));

        enterGameBtn.setOnClickListener(this);
        changeAccountBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        User user = UserPresenter.getUser();
        userInfoText.setText(getString(getStringByName("login_success"),UserPresenter.getNickName(this)));
    }

    @Override
    public void onClick(View v) {
        if (enterGameBtn.getId() == v.getId()){
            enterGame();
            return;
        }

        if (changeAccountBtn.getId() == v.getId()){
            changeAccount();
            return;
        }
    }

    private void enterGame(){
//        GameToast.showLoginSuccess(baseActivity,"登录成功");
//        UserPresenter.enterGame(this);
        UserPresenter.autoLogin(this);
    }

    private void changeAccount(){
        LoginFragment loginFragment = LoginFragment.getFragmentByName(baseActivity,LoginFragment.class);
        redirectFragment(loginFragment);
    }

    @Override
    public void autoLoginFail() {
        LoginFragment loginFragment = LoginFragment.getFragmentByName(baseActivity,LoginFragment.class);
        redirectFragment(loginFragment);
    }
}
