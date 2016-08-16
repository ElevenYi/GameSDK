package com.game.sdk.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.game.network.HttpListener;
import com.game.network.HttpProxy;
import com.game.sdk.bean.User;
import com.game.sdk.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by echowang on 16/4/23.
 */
public class UserModel implements IUserModel{
    private static UserModel instance;
    private User userBean;
    private boolean needBind = false;

    public static UserModel getInstance(){
        if (instance == null){
            instance = new UserModel();
        }
        return instance;
    }

    private UserModel(){

    }

    public boolean userNeedBind(){
        return needBind;
    }

    @Override
    public User getUser() {
        return userBean;
    }

    public boolean isLogin(){
        if (userBean == null){
            return false;
        }

        if (TextUtils.isEmpty(userBean.getSid())){
            return false;
        }

        if (TextUtils.isEmpty(userBean.getUid())){
            return false;
        }

        return true;
    }

    public void logout(){
        userBean = null;
    }

    /**
     * 帐号登录
     * @param context
     * @param phone
     * @param pwd
     * @param userListener
     */
    @Override
    public void login(final Context context, String phone, String pwd, final User.UserListener userListener) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","user.login");
        params.put("phone_mob",phone);
        params.put("password",pwd);
        params.put("promote",Config.getPromote(context));
        params.put("game_id",Config.getGameId());
        params.put("device_no",Config.getDeviceNo(context));

//        Log.i("UserModel","login url : " + HttpProxy.getUrlWithQueryString(Config.HOST,params, Request.DEFAULT_PARAMS_ENCODING));
        HttpProxy.post(context, Config.HOST, params, false, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
//                Log.i("UserModel","login : " + response);
                try {
                    boolean status = response.getBoolean("status");
                    if(status){
                        userBean = new User();
                        userBean.setSid(response.getString("sid"));
                        userBean.setUid(response.getString("uid"));
                        userBean.setNickName(response.getString("nickname"));
                        saveNickName(context,response.getString("nickname"));
                        if (response.has("token")) {
                            saveToken(context,response.getString("token"));
                        }

                        if (response.has("need_bind")){
                            needBind = response.getBoolean("need_bind");
                        }else{
                        	needBind = false;
                        }
//                        setGuestFlag(context,false);

                        if (userListener != null){
                            userListener.onSuccess(userBean);
                        }
                    }else{
                        if (userListener != null){
                            userListener.onFail(response.getString("errortext"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (userListener != null){
                        userListener.onFail("登录失败");
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                if (userListener != null){
                    userListener.onFail("登录失败 : " + msg);
                }
            }
        });
    }

    /**
     * 帐号注册
     * @param context
     * @param phone
     * @param pwd
     * @param vcode
     * @param userListener
     */
    @Override
    public void register(final Context context, String phone, String pwd, String vcode,final User.UserListener userListener) {
//        Log.i("UserModel","register phone_mob : " + phone);
//        Log.i("UserModel","register password : " + pwd);
//        Log.i("UserModel","register vcode : " + vcode);
        Map<String,String> params = new HashMap<String,String>();
        params.put("action","user.reg");
        params.put("game_id",Config.getGameId());
        params.put("phone_mob",phone);
        params.put("password",pwd);
        params.put("verifycode",vcode);
        params.put("promote", Config.getPromote(context));
        params.put("device_no",Config.getDeviceNo(context));

        HttpProxy.post(context, Config.HOST, params, false, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
//                Log.i("UserModel","register : " + response);
                try {
                    boolean status = response.getBoolean("status");
                    if(status){
                        userBean = new User();
                        userBean.setSid(response.getString("sid"));
                        userBean.setUid(response.getString("uid"));
                        userBean.setNickName(response.getString("nickname"));
                        saveNickName(context,response.getString("nickname"));
                        if (response.has("token")) {
                            saveToken(context,response.getString("token"));
                        }

                        if (response.has("need_bind")){
                            needBind = response.getBoolean("need_bind");
                        }else{
                        	needBind = false;
                        }
//                        setGuestFlag(context,false);

                        if (userListener != null){
                            userListener.onSuccess(userBean);
                        }
                    }else{
                        if (userListener != null){
                            userListener.onFail(response.getString("errortext"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (userListener != null){
                        userListener.onFail("注册失败");
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                if (userListener != null){
                    userListener.onFail("注册失败");
                }
            }
        });
    }

    /**
     * 获取短信验证码
     * @param context
     * @param phone
     * @param userListener
     */
    @Override
    public void sendVcode(Context context,String phone,final User.UserListener userListener) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","user.send_captcha");
        params.put("phone_mob",phone);
        params.put("device_no",Config.getDeviceNo(context));

        HttpProxy.post(context, Config.HOST, params, false, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
//                Log.i("UserModel","vcode : " + response);
                try {
                    boolean status = response.getBoolean("status");
                    if (status){
                        if (userListener != null){
                            userListener.onSuccess(userBean);
                        }
                    }else {
                        if (userListener != null){
                            userListener.onFail(response.getString("errortext"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (userListener != null){
                        userListener.onFail("发送失败");
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                if (userListener != null){
                    userListener.onFail("发送失败 : " + msg);
                }
            }
        });
    }

    /**
     * 修改帐号昵称
     * @param context
     * @param nickName
     * @param userListener
     */
    @Override
    public void updateNickName(final Context context, String nickName, final User.UserListener userListener) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","user.change_name");
        params.put("sid",userBean.getSid());
        params.put("nickname",nickName);
        params.put("device_no",Config.getDeviceNo(context));

        HttpProxy.post(context, Config.HOST, params, false, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
//                Log.i("UserModel","updateNickName : " + response);
                try {
                    boolean status = response.getBoolean("status");
                    if(status){
                        userBean.setNickName(response.getString("nickname"));
                        saveNickName(context,response.getString("nickname"));
                        if (userListener != null){
                            userListener.onSuccess(userBean);
                        }
                    }else{
                        if (userListener != null){
                            userListener.onFail(response.getString("errortext"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (userListener != null){
                        userListener.onFail("更新失败");
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                if (userListener != null){
                    userListener.onFail("更新失败 : " + msg);
                }
            }
        });
    }

    @Override
    public void logout(Context context,User.UserListener userListener) {

    }

    /**
     * 找回密码
     * @param context
     * @param phone
     * @param pwd
     * @param vcode
     * @param userListener
     */
    @Override
    public void forgetPass(Context context, String phone, String pwd, String vcode, final User.UserListener userListener) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","user.forgetpass");
        params.put("phone_mob",phone);
        params.put("newpass",pwd);
        params.put("verifycode",vcode);
        params.put("step","2");
        params.put("device_no",Config.getDeviceNo(context));

        HttpProxy.post(context, Config.HOST, params, false, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
//                Log.i("UserModel","forgetPass : " + response);
                try {
                    boolean status = response.getBoolean("status");
                    if(status){
                        //密码修改成功
                        if (userListener != null){
                            userListener.onSuccess(userBean);
                        }
                    }else{
                        if (userListener != null){
                            userListener.onFail(response.getString("errortext"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (userListener != null){
                        userListener.onFail("密码设置失败");
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                if (userListener != null){
                    userListener.onFail("密码设置失败");
                }
            }
        });
    }

    /**
     * 一键登录
     * @param context
     * @param userListener
     */
    @Override
    public void fastLogin(final Context context, final User.UserListener userListener) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","user.fastlogin");
        params.put("device_no",Config.getDeviceNo(context));
        params.put("promote",Config.getPromote(context));
        params.put("game_id",Config.getGameId());
//        Log.i("UserModel","login url : " + HttpProxy.getUrlWithQueryString(Config.HOST,params, Request.DEFAULT_PARAMS_ENCODING));
        HttpProxy.post(context, Config.HOST, params, false, new HttpListener<JSONObject>() {

            @Override
            public void onSuccess(JSONObject response) {
//                Log.i("UserModel","fastLogin : " + response);
                try {
                    boolean status = response.getBoolean("status");
                    if(status){
                        userBean = new User();
                        userBean.setSid(response.getString("sid"));
                        userBean.setUid(response.getString("uid"));
                        userBean.setNickName(response.getString("nickname"));
                        saveNickName(context,response.getString("nickname"));
                        if (response.has("token")) {
                            saveToken(context,response.getString("token"));
                        }

                        if (response.has("need_bind")){
                            needBind = response.getBoolean("need_bind");
                        }
//                        setGuestFlag(context,true);

                        if (userListener != null){
                            userListener.onSuccess(userBean);
                        }
                    }else{
                        if (userListener != null){
                            userListener.onFail(response.getString("errortext"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    if (userListener != null){
                        userListener.onFail("登录失败 : JSONException");
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                if (userListener != null){
                    userListener.onFail("登录失败 + " + msg);
                }
            }
        });
    }

    /**
     * 绑定帐号
     * @param context
     * @param phone
     * @param pwd
     * @param vcode
     * @param userListener
     */
    @Override
    public void bindPhone(final Context context, String phone, String pwd, String vcode, final User.UserListener userListener) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","user.bind");
        params.put("sid",userBean.getSid());
        params.put("device_no",Config.getDeviceNo(context));
        params.put("phone_mob",phone);
        params.put("password",pwd);
        params.put("verifycode",vcode);

        HttpProxy.post(context, Config.HOST, params, false, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
//                Log.i("UserModel","bindPhone : " + response);
                try {
                    boolean status = response.getBoolean("status");
                    if(status){
                        //帐号绑定成功
//                        setGuestFlag(context,false);
                        needBind = false;
                        if (userListener != null){
                            userListener.onSuccess(userBean);
                        }
                    }else{
                        if (userListener != null){
                            userListener.onFail(response.getString("errortext"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (userListener != null){
                        userListener.onFail("绑定失败");
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                if (userListener != null){
                    userListener.onFail("绑定失败");
                }
            }
        });
    }

    @Override
    public void getThirdUserInfo(final Context context, String url,final User.UserListener userListener) {
//        Log.i("UserModel","getThirdUserInfo : " + url);
        HttpProxy.get(context, url, null, false, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
//                Log.i("UserModel","getThirdUserInfo : " + response);
                try {
                    boolean status = response.getBoolean("status");
                    if(status){
                        userBean = new User();
                        userBean.setSid(response.getString("sid"));
                        userBean.setUid(response.getString("uid"));
                        userBean.setNickName(response.getString("nickname"));
                        saveNickName(context,response.getString("nickname"));

                        if (response.has("token")) {
                            saveToken(context,response.getString("token"));
                        }
                        
                        if (response.has("need_bind")){
                            needBind = response.getBoolean("need_bind");
                        }else{
                        	needBind = false;
                        }

                        if (userListener != null){
                            userListener.onSuccess(userBean);
                        }
                    }else{
                        if (userListener != null){
                            userListener.onFail(response.getString("errortext"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("UserModel","getThirdUserInfo : jsonexception");
                    if (userListener != null){
                        userListener.onFail("登录失败");
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                Log.i("UserModel","getThirdUserInfo : " + msg);
                if (userListener != null){
                    userListener.onFail("登录失败");
                }
            }
        });
    }

    @Override
    public void parseThirdUserInfo(final Context context,JSONObject response,User.UserListener userListener){
        if (response != null){
            try {
//                Log.i("UserModel","getThirdUserInfo : " + response);
                boolean status = response.getBoolean("status");
                if(status){
                    userBean = new User();
                    userBean.setSid(response.getString("sid"));
                    userBean.setUid(response.getString("uid"));
                    userBean.setNickName(response.getString("nickname"));
                    saveNickName(context,response.getString("nickname"));

                    if (response.has("token")) {
                        saveToken(context,response.getString("token"));
                    }

                    if (userListener != null){
                        userListener.onSuccess(userBean);
                    }
                }else{
                    if (userListener != null){
                        userListener.onFail(response.getString("errortext"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("UserModel","parseThirdUserInfo : jsonexception");
                if (userListener != null){
                    userListener.onFail("登录失败");
                }
            }
        }else{
            userListener.onFail("登录失败");
        }
    }

    @Override
    public void autoLogin(final Context context,final User.UserListener userListener){
        String token = getToken(context);
        if (TextUtils.isEmpty(token)){
            userListener.onFail("token 无效");
            return;
        }

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","user.login_by_token");
        params.put("token",token);
        params.put("device_no",Config.getDeviceNo(context));

        HttpProxy.get(context, Config.HOST, params, false, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
//                Log.i("UserModel","autoLogin : " + response);
                try {
                    boolean status = response.getBoolean("status");
                    if(status){
                        userBean = new User();
                        userBean.setSid(response.getString("sid"));
                        userBean.setUid(response.getString("uid"));
                        userBean.setNickName(response.getString("nickname"));
                        saveNickName(context,response.getString("nickname"));
                        if (response.has("token")) {
                            saveToken(context,response.getString("token"));
                        }

                        if (response.has("need_bind")){
                            needBind = response.getBoolean("need_bind");
                        }
//                        setGuestFlag(context,false);

                        if (userListener != null){
                            userListener.onSuccess(userBean);
                        }
                    }else{
                        if (userListener != null){
                            userListener.onFail(response.getString("errortext"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (userListener != null){
                        userListener.onFail("登录失败");
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                if (userListener != null){
                    userListener.onFail("登录失败 : " + msg);
                }
            }
        });
    }

    /**
     * 保存用户登录的手机号
     * @param context
     * @param phone
     */
    @Override
    public void saveUserPhone(Context context,String phone){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        Set<String> phones = sharedPreferences.getStringSet("phone",new LinkedHashSet<String>());
        if (phones.contains(phone)){
            return;
        }

        phones.add(phone);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("phone",phones);
        editor.commit();
    }

    /**
     * 读取保存的用户手机号
     * @param context
     * @return
     */
    @Override
    public Set<String> getAllPhone(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet("phone",new LinkedHashSet<String>());
    }

    @Override
    public void deleteUserPhone(Context context, String phone) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        Set<String> phones = sharedPreferences.getStringSet("phone",new LinkedHashSet<String>());
        if (phones.contains(phone)){
            phones.remove(phone);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet("phone",phones);
            editor.commit();
        }
    }

//    @Override
//    public void saveUserInfo(Context context){
//        String userInfo = userBean.getSid() + "," + userBean.getUid() + "," + userBean.getNickName();
//        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("user",userInfo);
//        editor.commit();
//    }
//
//    @Override
//    public User getUserInfo(Context context){
//        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
//        String user = sharedPreferences.getString("user","");
//        if (!TextUtils.isEmpty(user)){
//            String[] temp = user.split(",");
//            userBean = new User();
//            userBean.setSid(temp[0]);
//            userBean.setUid(temp[1]);
//            userBean.setNickName(temp[2]);
//        }
//        return userBean;
//    }

    @Override
    public void saveToken(Context context,String token){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token",token);
        editor.commit();
    }

    @Override
    public String getToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        return sharedPreferences.getString("token","");
    }

//    @Override
//    public void setGuestFlag(Context context,boolean isGuest){
//        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("guest",isGuest);
//        editor.commit();
//    }
//
//    @Override
//    public boolean getGuestFLag(Context context){
//        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
//        return sharedPreferences.getBoolean("guest",false);
//    }

    @Override
    public void saveNickName(Context context, String nickName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nickName",nickName);
        editor.commit();
    }

    @Override
    public String getNickName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        return sharedPreferences.getString("nickName","");
    }
}
