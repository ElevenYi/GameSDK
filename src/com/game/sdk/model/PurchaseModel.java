package com.game.sdk.model;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.game.network.HttpListener;
import com.game.network.HttpProxy;
import com.game.sdk.bean.Purchase;
import com.game.sdk.bean.User;
import com.game.sdk.config.Config;
import com.game.sdk.presenter.UserPresenter;
import com.ipaynow.plugin.api.IpaynowPlugin;
import com.ipaynow.plugin.manager.route.dto.ResponseParams;
import com.ipaynow.plugin.manager.route.impl.ReceivePayResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by echowang on 16/4/23.
 */
public class PurchaseModel implements IPurchaseModel,ReceivePayResult{
    private static PurchaseModel instance;
    private PurchaseStatusListener statusListener;
    private String orderId;
    private Purchase purchase;

    public static PurchaseModel getInstance(){
        if (instance == null){
            instance = new PurchaseModel();
        }

        return instance;
    }

    private PurchaseModel(){

    }

    public interface PurchaseStatusListener{
        void onSuccess(String orderId,Purchase purchase);
        void onFail(String msg);
    }

    public interface PurchaseCoinsListener{
        void onSuccess(int coins);
        void onFail(String msg);
    }

    @Override
    public void initNowPay(Activity activity) {
        IpaynowPlugin. getInstance().init(activity);
    }

    @Override
    public void createOrderId(Context context,String channelId,Purchase purchase) {
        this.purchase = purchase;
        User user = UserPresenter.getUser();
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","order.create");
        params.put("sid",user.getSid());
        params.put("coins",String.valueOf(purchase.getCoins()));
        params.put("game_id", Config.getGameId());
        params.put("serverid",purchase.getServerid());
        params.put("roleid",purchase.getRoleid());
        params.put("developerInfo",purchase.getDeveloperInfo());
        params.put("payChannelType",channelId);
        params.put("device_no",Config.getDeviceNo(context));

        HttpProxy.get(context, Config.HOST, params, false, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response != null && response.getBoolean("status")){
                        orderId = response.getString("order_id");
                        JSONObject json = response.getJSONObject("channel_order_info").getJSONObject("params");

                        StringBuilder requestMessage = new StringBuilder();
                        requestMessage.append("appId=").append(json.getString("appId")).append("&");
                        requestMessage.append("mhtOrderNo=").append(json.getString("mhtOrderNo")).append("&");
                        requestMessage.append("mhtOrderName=").append(json.getString("mhtOrderName")).append("&");
                        requestMessage.append("mhtOrderType=").append(json.getString("mhtOrderType")).append("&");
                        requestMessage.append("mhtCurrencyType=").append(json.getString("mhtCurrencyType")).append("&");
                        requestMessage.append("mhtOrderAmt=").append(json.getString("mhtOrderAmt")).append("&");
                        requestMessage.append("mhtOrderDetail=").append(json.getString("mhtOrderDetail")).append("&");
                        requestMessage.append("funcode=").append(json.getString("funcode")).append("&");
//			            requestMessage.append("mhtOrderTimeOut=").append(json.getString("mhtOrderTimeOut")).append("&");
                        requestMessage.append("mhtOrderStartTime=").append(json.getString("mhtOrderStartTime")).append("&");
                        requestMessage.append("notifyUrl=").append(json.getString("notifyUrl")).append("&");
                        requestMessage.append("mhtCharset=").append(json.getString("mhtCharset")).append("&");
                        requestMessage.append("payChannelType=").append(json.getString("payChannelType")).append("&");
//			            requestMessage.append("mhtReserved=").append(json.getString("mhtReserved")).append("&");
                        requestMessage.append("mhtSignature=").append(json.getString("mhtSignature")).append("&");
                        requestMessage.append("mhtSignType=").append(json.getString("mhtSignType"));

                        System.out.println("requestMessage :" + requestMessage);
                        IpaynowPlugin.getInstance().setCallResultReceiver(PurchaseModel.this);
//			            String requestMessage = "appId=1461555791607412&mhtCharset=UTF-8&mhtCurrencyType=156&mhtOrderAmt=1000&mhtOrderDetail=充值:1612667795&mhtOrderName=充值:1612667795&mhtOrderNo=1612667795&mhtOrderStartTime=20160506114800&mhtOrderType=01&notifyUrl=http://www.gm88.com/api/paynotify-notify-1066.html&payChannelType=12&mhtSignature=908068e8ff91ead16c207319929daeaa&mhtSignType=MD5";
                        IpaynowPlugin.getInstance().pay(requestMessage.toString());
                    }else{
                        String errorMsg = response.getString("errortext");
                        if (statusListener != null){
                            statusListener.onFail(TextUtils.isEmpty(errorMsg) ? "订单创建失败" : errorMsg);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    if (statusListener != null){
                        statusListener.onFail("订单创建失败");
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                if (statusListener != null){
                    statusListener.onFail(TextUtils.isEmpty(msg) ? "网络异常" : msg);
                }
            }
        });
//        String jsonStr = "{\"order_id\":\"1070\",\"channel_order_info\":{\"method\":\"GET\",\"gateway\":\"https:\\/\\/api.ipaynow.cn\",\"params\":{\"appId\":\"1461307249361329\",\"mhtOrderNo\":\"1612652103\",\"mhtOrderName\":\"\\u5145\\u503c:1612652103\",\"mhtOrderType\":\"01\",\"mhtCurrencyType\":\"156\",\"mhtOrderAmt\":1000,\"mhtOrderDetail\":\"\\u5145\\u503c:1612652103\",\"funcode\":\"WP001\",\"mhtOrderStartTime\":\"20160506142844\",\"notifyUrl\":\"http:\\/\\/www.gm88.com\\/api\\/paynotify-notify-1070.html\",\"mhtCharset\":\"UTF-8\",\"payChannelType\":\"13\",\"mhtSignature\":\"973d64b7c2e961ad9890a4344ca9b98f\",\"mhtSignType\":\"MD5\"}},\"status\":true}";

    }

//    银联支付:11 支付宝支付:12; 微信支付:13; 微信插件支付:1310 百度支付:50
//    QQ 支付:25

    @Override
    public void startAlipay(Context context, Purchase purchase, PurchaseModel.PurchaseStatusListener listener) {
        this.statusListener = listener;
        createOrderId(context,"12",purchase);
    }

    @Override
    public void startBankPay(Context context,Purchase purchase,PurchaseModel.PurchaseStatusListener listener) {
        this.statusListener = listener;
        createOrderId(context,"11",purchase);
    }

    @Override
    public void startWeChatPay(Context context,Purchase purchase,PurchaseModel.PurchaseStatusListener listener) {
        this.statusListener = listener;
        createOrderId(context,"13",purchase);
    }

    @Override
    public void getCoins(Context context, final PurchaseCoinsListener listener) {

        User user = UserPresenter.getUser();
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","user.get_coins");
        params.put("sid",user.getSid());
        params.put("device_no",Config.getDeviceNo(context));

        HttpProxy.get(context, Config.HOST, params, false, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.i("PurchaseModel","getCoins : " + response);
                try {
                    if (response != null && response.getBoolean("status")){
                        int coins = response.getInt("coins");
                        if (listener != null){
                            listener.onSuccess(coins);
                        }

                    }else{
                        if (listener != null){
                            listener.onFail("读取猫币失败");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (listener != null){
                        listener.onFail("读取猫币失败");
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                if (listener != null){
                    listener.onFail("读取猫币失败 : " + msg);
                }
            }
        });
    }

    @Override
    public void coinsPay(Context context, final Purchase purchase, final PurchaseStatusListener listener) {
        User user = UserPresenter.getUser();
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","order.pay");
        params.put("sid",user.getSid());
        params.put("coins",String.valueOf(purchase.getCoins()));
        params.put("game_id", Config.getGameId());
        params.put("serverid",purchase.getServerid());
        params.put("roleid",purchase.getRoleid());
        params.put("developerInfo",purchase.getDeveloperInfo());
        params.put("device_no",Config.getDeviceNo(context));

        HttpProxy.get(context, Config.HOST, params, false, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.i("PurchaseModel","coinsPay : " + response);
                try {
                    if (response != null && response.getBoolean("status")) {
                        if (listener != null){
                            if (response.has("order_id")){
                                listener.onSuccess(response.getString("order_id"),purchase);
                            }else{
                                listener.onSuccess("",purchase);
                            }
                        }
                    }else{
                        if (listener != null){
                            listener.onFail("支付失败");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (listener != null){
                        listener.onFail("支付失败");
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                if (listener != null){
                    listener.onFail("支付失败 ： " + msg);
                }
            }
        });
    }

    @Override
    public void onIpaynowTransResult(ResponseParams responseParams) {
        String respCode = responseParams.respCode;
        String errorCode = responseParams.errorCode;
        String errorMsg = responseParams.respMsg;

        Log.i("PurchaseModel","respCode : " + respCode);
        Log.i("PurchaseModel","errorCode : " + errorCode);
        Log.i("PurchaseModel","errorMsg : " + errorMsg);

        if ("00".equalsIgnoreCase(respCode)){
            if (statusListener != null){
                statusListener.onSuccess(orderId,purchase);
            }
        }else{
            if (statusListener != null){
                statusListener.onFail("errorCode:" + errorCode + ",errorMsg:" + errorMsg);
            }
        }
    }
}
