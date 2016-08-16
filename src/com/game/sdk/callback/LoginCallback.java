package com.game.sdk.callback;

import com.game.sdk.bean.User;

/**
 * Created by echowang on 16/4/23.
 */
public interface LoginCallback {
    void loginSuccess(User user);
    void loginFail(String msg);
}
