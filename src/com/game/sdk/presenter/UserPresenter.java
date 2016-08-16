package com.game.sdk.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.game.sdk.Platform;
import com.game.sdk.activity.GameToast;
import com.game.sdk.activity.UserActivity;
import com.game.sdk.bean.User;
import com.game.sdk.callback.CallbackManager;
import com.game.sdk.model.GameFloatModel;
import com.game.sdk.model.IUserModel;
import com.game.sdk.model.IUserView;
import com.game.sdk.model.UserModel;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by echowang on 16/4/23.
 */
public class UserPresenter {
    /**
     * 启动登录界面
     * @param activity
     */
    public static void showLoginActiviy(Activity activity){
        IUserModel userModel = UserModel.getInstance();
//        User user = userModel.getUserInfo(activity);
        String token = userModel.getToken(activity);
        if (TextUtils.isEmpty(token)){
            Intent intent = new Intent(activity, UserActivity.class);
            intent.putExtra("type",UserActivity.LOGIN);
            activity.startActivity(intent);
        }else{
            Intent intent = new Intent(activity, UserActivity.class);
            intent.putExtra("type",UserActivity.AUTOLOGIN);
            activity.startActivity(intent);
        }
    }


//    public static void showBindActivity(Activity activity){
//        Intent intent = new Intent(activity, UserActivity.class);
//        intent.putExtra("type",UserActivity.BIND);
//        activity.startActivity(intent);
//    }

    /**
     * 启动绑定界面
     * @param context
     */
    public static void showBindActivity(Context context){
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra("type",UserActivity.BIND);
        context.startActivity(intent);

        Platform.getInstance().setUserState(context,GameFloatModel.IDEL);
    }

    public static User getUser(){
        return UserModel.getInstance().getUser();
    }

    public static boolean isLogin(){
        return UserModel.getInstance().isLogin();
    }

    /**
     * 用户登录
     * @param userView
     */
    public static void login(final IUserView userView){
        final IUserModel userModel = UserModel.getInstance();
        final Context context = userView.getContext();
        String phone = userView.getPhone();
        String pwd = userView.getPassword();
        userView.showLoading("正在登录");
        userModel.login(context, phone, pwd, new User.UserListener() {
            @Override
            public void onSuccess(User userBean) {
                userView.dismissLoading();
                if (userBean != null){
//                    GameToast.showLoginSuccess(userView.getContext(),"登录成功");
//                    loginSuccess(userView,false);
//                    userView.closeActivity();
//                    Platform.getInstance().setUserState(context, GameFloatModel.DEFAULT);
//                    userModel.setGuestFlag(context,false);
                    userModel.saveUserPhone(userView.getContext(),userView.getPhone());
                    userView.loginSucess(false);
                }else{
                    userView.showToast("登录失败");
                }
            }

            @Override
            public void onFail(String msg) {
                userView.dismissLoading();
                userView.showToast(msg);
            }
        });
    }

    /**
     * 自动登录
     * @param userView
     */
    public static void autoLogin(final IUserView userView){
        IUserModel userModel = UserModel.getInstance();
        userView.showLoading("正在登录");
        userModel.autoLogin(userView.getContext(), new User.UserListener() {
            @Override
            public void onSuccess(User userBean) {
                userView.dismissLoading();

                if (userBean != null){
                    userView.loginSucess(false);
                }else{
                    userView.showToast("登录失败");
                }
            }

            @Override
            public void onFail(String msg) {
                userView.dismissLoading();
                userView.showToast(msg);
                userView.autoLoginFail();
            }
        });
    }

    /**
     * 帐号退出
     */
    public static void logout(){
        UserModel.getInstance().logout();
    }

    /**
     * 用户注册
     * @param userView
     */
    public static void register(final IUserView userView){
        final IUserModel userModel = UserModel.getInstance();
        final Context context = userView.getContext();
        String phone = userView.getPhone();
        String pwd = userView.getPassword();
        String vcode = userView.getVcode();
        userView.showLoading("正在注册");
        userModel.register(context, phone, pwd, vcode, new User.UserListener() {
            @Override
            public void onSuccess(User userBean) {
                userView.dismissLoading();
                if (userBean != null){
//                    GameToast.showRegisterToast(userView.getContext(),"注册成功");
//                    loginSuccess(userView,true);
//                    userView.closeActivity();
//                    userModel.setGuestFlag(context,false);
//                    Platform.getInstance().setUserState(context, GameFloatModel.DEFAULT);
                    userModel.saveUserPhone(userView.getContext(),userView.getPhone());
                    userView.loginSucess(true);
                }else{
                    userView.showToast("注册失败");
                }
            }

            @Override
            public void onFail(String msg) {
                userView.dismissLoading();
                userView.showToast(msg);
            }
        });
    }

    /**
     * 发送验证码
     * @param userView
     */
    public static void sendVode(final IUserView userView){
        IUserModel userModel = UserModel.getInstance();
        Context context = userView.getContext();
        String phone = userView.getPhone();
        userView.showLoading("正在发送验证码");
        userModel.sendVcode(context, phone, new User.UserListener() {
            @Override
            public void onSuccess(User userBean) {
                userView.dismissLoading();
                userView.sendCodeResult(true,"");
            }

            @Override
            public void onFail(String msg) {
//                userView.showToast(msg);
                userView.dismissLoading();
                userView.sendCodeResult(false,msg);
            }
        });
    }

    /**
     * 一键登录
     * @param userView
     */
    public static void fastLogin(final IUserView userView){
        final IUserModel userModel = UserModel.getInstance();
        final Context context = userView.getContext();
        userView.showLoading("正在登录");
        userModel.fastLogin(context, new User.UserListener() {
            @Override
            public void onSuccess(User userBean) {
                userView.dismissLoading();
                if (userBean != null){
//                    GameToast.showLoginSuccess(userView.getContext(),"登录成功");
//                    loginSuccess(userView,false);
//                    userView.closeActivity();
//                    userModel.setGuestFlag(context,true);
//                    Platform.getInstance().setUserState(context, GameFloatModel.TRIAL);
                    userView.loginSucess(false);
                }
            }

            @Override
            public void onFail(String msg) {
                userView.dismissLoading();
                userView.showToast(msg);
            }
        });
    }

    /**
     * 修改昵称
     * @param userView
     */
    public static void updataNickName(final IUserView userView){
        IUserModel userModel = UserModel.getInstance();
        Context context = userView.getContext();
        String nickName = userView.getNickName();
        userView.showLoading("正在修改昵称");
        userModel.updateNickName(context, nickName, new User.UserListener() {
            @Override
            public void onSuccess(User userBean) {
                userView.dismissLoading();
                if(userView != null){
                    userView.showToast("修改成功");
                }
            }

            @Override
            public void onFail(String msg) {
                userView.dismissLoading();
                userView.showToast(msg);
            }
        });
    }

    /**
     * 找回密码
     * @param userView
     */
    public static void forgetPass(final IUserView userView){
        IUserModel userModel = UserModel.getInstance();
        Context context = userView.getContext();
        String phone = userView.getPhone();
        String pwd = userView.getPassword();
        String vcode = userView.getVcode();
        userView.showLoading("找回密码");
        userModel.forgetPass(context, phone, pwd, vcode, new User.UserListener() {
            @Override
            public void onSuccess(User userBean) {
                userView.dismissLoading();
            }

            @Override
            public void onFail(String msg) {
                userView.dismissLoading();
                userView.showToast(msg);
            }
        });
    }

    /**
     * 绑定手机号
     * @param userView
     */
    public static void bindPhone(final IUserView userView){
        final IUserModel userModel = UserModel.getInstance();
        final Context context = userView.getContext();
        String phone = userView.getPhone();
        String pwd = userView.getPassword();
        String vcode = userView.getVcode();
        userView.showLoading("正在绑定");
        userModel.bindPhone(context, phone, pwd, vcode, new User.UserListener() {
            @Override
            public void onSuccess(User userBean) {
                userView.dismissLoading();
                if (userBean != null){
//                    userView.showToast("绑定成功");
                    GameToast.showBindToast(userView.getContext(),"绑定成功");
                    userView.closeActivity();
                    Platform.getInstance().setUserState(context,GameFloatModel.IDEL);
                }
            }

            @Override
            public void onFail(String msg) {
                userView.dismissLoading();
                userView.showToast(msg);
            }
        });
    }

    public static void getThirdUserInfo(final IUserView userView, String url){
        IUserModel userModel = UserModel.getInstance();
        userView.showLoading("正在登录");
        userModel.getThirdUserInfo(userView.getContext(), url, new User.UserListener() {
            @Override
            public void onSuccess(User userBean) {
                userView.dismissLoading();
                if (userBean != null){
//                    GameToast.showLoginSuccess(userView.getContext(),"登录成功");
//                    loginSuccess(userView,false);
//                    userView.closeActivity();
                    userView.loginSucess(false);
                }
            }

            @Override
            public void onFail(String msg) {
                userView.dismissLoading();
                userView.showToast(msg);
            }
        });
    }

    public static void parseThirdUserInfo(final IUserView userView, JSONObject jsonObject, final boolean isBind){
        IUserModel userModel = UserModel.getInstance();
        userModel.parseThirdUserInfo(userView.getContext(),jsonObject, new User.UserListener() {
            @Override
            public void onSuccess(User userBean) {
                if (userBean != null){
                    if (isBind){
                        Platform.getInstance().setUserState(userView.getContext(),GameFloatModel.IDEL);
                        userView.closeActivity();
                    }else{
                        userView.loginSucess(false);
                    }

                }
            }

            @Override
            public void onFail(String msg) {
                userView.showToast(msg);
            }
        });
    }

    public static String getNickName(IUserView userView){
        IUserModel userModel = UserModel.getInstance();
        return userModel.getNickName(userView.getContext());
    }

//    public static void loginSuccess(IUserView userView,boolean isRegister){
//        if (isRegister){
//            GameToast.showRegisterToast(userView.getContext(),"注册成功");
//        }else{
//            GameToast.showLoginSuccess(userView.getContext(),"登录成功");
//        }
//
//        CallbackManager.getLoginCallback().loginSuccess(getUser());
//    }

    /**
     * 登录成功返回到游戏
     */
    public static void enterGame(IUserView userView){
        userView.closeActivity();
//        saveUserInfo(userView.getContext());

//        IUserModel userModel = UserModel.getInstance();
        if (UserModel.getInstance().userNeedBind()){
            Platform.getInstance().setUserState(userView.getContext(),GameFloatModel.TRIAL);
        }else{
            Platform.getInstance().setUserState(userView.getContext(),GameFloatModel.DEFAULT);
        }
//
        CallbackManager.getLoginCallback().loginSuccess(getUser());
    }

    /**
     * 检查手机号是否有效
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone){
        if (TextUtils.isEmpty(phone)){
            return false;
        }
        String pattern = "(13\\d|14[57]|15[^4,\\D]|17[678]|18\\d)\\d{8}|170[059]\\d{7}";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 检查密码是否有效
     * @param passport
     * @return
     */
    public static boolean checkPassport(String passport){
        if (TextUtils.isEmpty(passport) || passport.length() < 6 || passport.length() > 10){
            return false;
        }
        return true;
    }

    /**
     * 读取历史登录过的用户 （试玩用户与第三方帐号 不存储）
     * @param userView
     */
    public static void readHistoryUser(IUserView userView){
        IUserModel userModel = UserModel.getInstance();
        userView.setHistoryUser(userModel.getAllPhone(userView.getContext()));
    }

    /**
     * 删除历史帐号
     * @param context
     * @param phone
     */
    public static void deleteHistoryUser(Context context,String phone){
        IUserModel userModel = UserModel.getInstance();
        userModel.deleteUserPhone(context,phone);
    }

//    public static void saveUserInfo(Context context){
//        IUserModel userModel = UserModel.getInstance();
//        userModel.saveUserInfo(context);
//    }
}
