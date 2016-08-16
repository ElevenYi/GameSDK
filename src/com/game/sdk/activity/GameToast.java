package com.game.sdk.activity;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.sdk.config.Config;

/**
 * Created by echowang on 16/4/24.
 */
public class GameToast {
    /**
     * 显示Toast
     * @param context
     * @param iconId
     * @param msg
     */
    private static void showToast(Context context,int iconId,String msg){
        View toastView = LayoutInflater.from(context).inflate(Config.getLayoutByName(context,"toast"),null);
        Toast toast = new Toast(context.getApplicationContext());
        ImageView toastIcon = (ImageView) toastView.findViewById(Config.getIdByName(context,"toast_icon"));
        toastIcon.setImageResource(iconId);
        TextView toastMsg = (TextView) toastView.findViewById(Config.getIdByName(context,"toast_msg"));
        toastMsg.setText(msg);
        toast.setView(toastView);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 显示登录Toast
     * @param context
     * @param msg
     */
    public static void showLoginSuccess(Context context,String msg){
        GameToast.showToast(context,Config.getDrawableByName(context,"account_login"),msg);
    }

    /**
     * 显示注册Toast
     * @param context
     * @param msg
     */
    public static void showRegisterToast(Context context,String msg){
        GameToast.showToast(context,Config.getDrawableByName(context,"correct"),msg);
    }

    /**
     * 显示绑定Toast
     * @param context
     * @param msg
     */
    public static void showBindToast(Context context,String msg){
        GameToast.showToast(context,Config.getDrawableByName(context,"account_bind"),msg);
    }
}
