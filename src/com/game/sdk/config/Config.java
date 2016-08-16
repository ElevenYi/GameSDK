package com.game.sdk.config;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

/**
 * Created by echowang on 16/4/23.
 */
public class Config {
    private static boolean isInit;
    private static String gameId;
    public final static String HOST = "http://www.gm88.com/api/index.php";
    public final static String HOST1 = "http://www.gm88.com/index.php";    //第三方帐号接口
//    public final static String QQLOGIN = "http://gm88.com/index.php?app=connect&open_code=qq&source=app";
//    public final static String SINALOGIN = "http://gm88.com/index.php?app=connect&open_code=weibo&source=app";

    public static String getGameId() {
        return gameId;
    }

    public static void setGameId(String gameId) {
        Config.gameId = gameId;
        Config.isInit = true;
    }

    public static boolean isInitPlatform(){
        return isInit;
    }


    /**
     * 渠道id
     * @return
     */
    public static String getPromote(Context context){
        String channel = "0";
        try {
            channel = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData.getString("gm_channel","0");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (channel == null){
            channel = "0";
        }
        return channel;
    }

    /**
     * 读取设备id
     * @param context
     * @return
     */
    public static String getDeviceNo(Context context){
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    public static int getLayoutByName(Context context,String layoutName){
        return context.getResources().getIdentifier(layoutName,"layout",context.getPackageName());
    }

    public static int getDrawableByName(Context context,String drawableName){
        return context.getResources().getIdentifier(drawableName,"drawable",context.getPackageName());
    }

    public static int getIdByName(Context context,String idName){
        return context.getResources().getIdentifier(idName,"id",context.getPackageName());
    }

    public static int getStringByName(Context context,String stringName){
        return context.getResources().getIdentifier(stringName,"string",context.getPackageName());
    }

    public static int getStyleByName(Context context,String styleName){
        return context.getResources().getIdentifier(styleName,"style",context.getPackageName());
    }

    public static int getDimensByName(Context context,String dimenName){
        return context.getResources().getIdentifier(dimenName,"dimen",context.getPackageName());
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
