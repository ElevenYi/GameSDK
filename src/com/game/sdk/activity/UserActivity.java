package com.game.sdk.activity;

import android.content.Intent;
import android.os.Bundle;

import com.game.sdk.activity.fragment.AccountBindFragment;
import com.game.sdk.activity.fragment.AccountChangeFragment;
import com.game.sdk.activity.fragment.AccountSecondLoginFragment;
import com.game.sdk.activity.fragment.LoginFragment;

public class UserActivity extends BaseActivity {
    public final static int LOGIN = 1;
    public final static int BIND = 2;
    public final static int AUTOLOGIN = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int type = intent.getIntExtra("type",LOGIN);
        if (type == LOGIN){
            LoginFragment loginFragment = LoginFragment.getFragmentByName(this,LoginFragment.class);
            initFragment(loginFragment);
        }else if (type == BIND){
            AccountBindFragment accountBindFragment = AccountBindFragment.getFragmentByName(this, AccountBindFragment.class);
            initFragment(accountBindFragment);
        }else {
            AccountSecondLoginFragment accountSecondLoginFragment = AccountSecondLoginFragment.getFragmentByName(this, AccountSecondLoginFragment.class);
            initFragment(accountSecondLoginFragment);
//            AccountChangeFragment accountChangeFragment = AccountChangeFragment.getFragmentByName(this,AccountChangeFragment.class);
//            initFragment(accountChangeFragment);
        }

    }

}
