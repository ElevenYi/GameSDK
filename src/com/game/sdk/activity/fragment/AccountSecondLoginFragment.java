package com.game.sdk.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.sdk.R;
import com.game.sdk.presenter.UserPresenter;
import com.game.sdk.widget.TextNewsView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eleven on 16/8/16.
 */
public class AccountSecondLoginFragment extends UserBaseFragment implements View.OnClickListener {

    RelativeLayout close_RL;
    LinearLayout home_LL;
    LinearLayout package_LL;
    LinearLayout message_LL;
    LinearLayout user_LL;
    ImageView change_IV;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutByName("fragment_account_login_second"), null, true);
        close_RL = (RelativeLayout) view.findViewById(getIdByName("rl_fragment_account_login_second_close"));
        home_LL = (LinearLayout) view.findViewById(getIdByName("ll_fragment_account_login_second_function_home"));
        package_LL = (LinearLayout) view.findViewById(getIdByName("ll_fragment_account_login_second_function_package"));
        message_LL = (LinearLayout) view.findViewById(getIdByName("ll_fragment_account_login_second_function_message"));
        user_LL = (LinearLayout) view.findViewById(getIdByName("ll_fragment_account_login_second_function_user_center"));
        change_IV = (ImageView) view.findViewById(getIdByName("iv_fragment_account_bind_change_user"));
        ButterKnife.bind(this, view);

        initView();
        initEvents();

        return view;
    }

    //根据获取接口的数据来显示view
    private void initView() {

    }

    private void initEvents() {
        close_RL.setOnClickListener(this);
        home_LL.setOnClickListener(this);
        package_LL.setOnClickListener(this);
        message_LL.setOnClickListener(this);
        user_LL.setOnClickListener(this);
        change_IV.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        User user = UserPresenter.getUser();
//        userInfoText.setText(getString(getStringByName("login_success"), UserPresenter.getNickName(this)));
    }

    @Override
    public void onClick(View v) {
        if (close_RL.getId() == v.getId()) {
            enterGame();
            return;
        }

        if (change_IV.getId() == v.getId()) {
            changeAccount();
            return;
        }
    }

    private void enterGame() {
//        GameToast.showLoginSuccess(baseActivity,"登录成功");
//        UserPresenter.enterGame(this);
        UserPresenter.autoLogin(this);
    }

    private void changeAccount() {
        LoginFragment loginFragment = LoginFragment.getFragmentByName(baseActivity, LoginFragment.class);
        redirectFragment(loginFragment);
    }

    @Override
    public void autoLoginFail() {
        LoginFragment loginFragment = LoginFragment.getFragmentByName(baseActivity, LoginFragment.class);
        redirectFragment(loginFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
