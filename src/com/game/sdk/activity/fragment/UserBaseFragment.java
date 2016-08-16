package com.game.sdk.activity.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.game.sdk.activity.GameToast;
import com.game.sdk.model.IUserView;
import com.game.sdk.presenter.UserPresenter;

import java.util.Set;

/**
 * Created by echowang on 16/5/7.
 */
public class UserBaseFragment extends BaseFragment implements IUserView {

    @Override
    public Context getContext() {
        return baseActivity;
    }

    @Override
    public void showLoading(String msg) {
        baseActivity.showLoading(msg);
    }

    @Override
    public void dismissLoading() {
        baseActivity.dismissLoading();
    }

    @Override
    public String getPhone() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getVcode() {
        return null;
    }

    @Override
    public String getNickName() {
        return null;
    }

//    @Override
//    public String clearPhone() {
//        return null;
//    }
//
//    @Override
//    public String clearPassword() {
//        return null;
//    }
//
//    @Override
//    public String clearVcode() {
//        return null;
//    }

    @Override
    public void closeActivity() {
        baseActivity.finish();
    }

    @Override
    public void showToast(String msg) {
        if (TextUtils.isEmpty(msg)){
            return;
        }
        Toast.makeText(baseActivity,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setHistoryUser(Set<String> userList) {

    }

    @Override
    public void loginSucess(boolean isRegister) {
        if (isRegister){
            GameToast.showRegisterToast(getContext(),"注册成功");
        }else{
            GameToast.showLoginSuccess(getContext(),"登录成功");
        }

        UserPresenter.enterGame(this);
//        closeActivity();
//        AccountChangeFragment accountChangeFragment = AccountChangeFragment.getFragmentByName(baseActivity,AccountChangeFragment.class);
//        redirectFragment(accountChangeFragment);
    }

    @Override
    public void autoLoginFail() {

    }

    @Override
    public void sendCodeResult(boolean result,String msg) {

    }
}
