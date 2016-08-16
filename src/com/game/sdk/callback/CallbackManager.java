package com.game.sdk.callback;

/**
 * Created by echowang on 16/4/23.
 */
public class CallbackManager {
    private static LoginCallback loginCallback;
    private static PurchaseCallback purchaseCallback;
    private static LogoutCallback logoutCallback;

    public static LoginCallback getLoginCallback() {
        return loginCallback;
    }

    public static void setLoginCallback(LoginCallback loginCallback) {
        CallbackManager.loginCallback = loginCallback;
    }

    public static PurchaseCallback getPurchaseCallback() {
        return purchaseCallback;
    }

    public static void setPurchaseCallback(PurchaseCallback purchaseCallback) {
        CallbackManager.purchaseCallback = purchaseCallback;
    }

    public static LogoutCallback getLogoutCallback() {
        return logoutCallback;
    }

    public static void setLogoutCallback(LogoutCallback logoutCallback) {
        CallbackManager.logoutCallback = logoutCallback;
    }
}
