package com.game.sdk.model;

import android.content.Context;

import java.util.Set;

/**
 * Created by echowang on 16/4/27.
 */
public interface IUserView {
    Context getContext();
    void showLoading(String msg);
    void dismissLoading();
    String getPhone();
    String getPassword();
    String getVcode();
    String getNickName();
//    String clearPhone();
//    String clearPassword();
//    String clearVcode();
    void closeActivity();
    void showToast(String msg);
    void loginSucess(boolean isRegister);
    void setHistoryUser(Set<String> userList);
    void sendCodeResult(boolean result,String msg);
    void autoLoginFail();
}
