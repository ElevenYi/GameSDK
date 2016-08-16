package com.game.sdk.activity.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.game.network.HttpProxy;
import com.game.network.Request;
import com.game.sdk.bean.User;
import com.game.sdk.config.Config;
import com.game.sdk.presenter.UserPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by echowang on 16/5/7.
 */
public class ThirdLoginFragment extends UserBaseFragment implements View.OnClickListener {
    public final static int QQ = 1;
    public final static int SINA = 2;

    public final static int LOGIN = 3;
    public final static int BIND = 4;

    private ImageButton backBtn;
    private TextView titelView;
    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutByName("fragment_third_login"),null,true);
        backBtn = (ImageButton) view.findViewById(getIdByName("third_login_back"));
        titelView = (TextView) view.findViewById(getIdByName("third_login_title"));
        mWebView = (WebView) view.findViewById(getIdByName("third_login_webview"));

        backBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int type = getArguments().getInt("type",QQ);
        int action = getArguments().getInt("action",LOGIN);
        switch (type){
            case QQ:
                titelView.setText(getStringByName("qqlogin"));
                break;
            case SINA:
                titelView.setText(getStringByName("weibologin"));
                break;
        }

        initWebView(action);
        loadLoginUrl(type,action);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == backBtn.getId()){
//            closeActivity();
            baseActivity.onBackPressed();
            return;
        }
    }

    private void initWebView(int action){
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(action), "local_obj");
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.i("ThirdLogin","url : " + url);
            	if(URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)){
            		Uri uri = Uri.parse(url);
                    String host = uri.getHost();
                    String act = uri.getQueryParameter("act");
                    String source = uri.getQueryParameter("source");
//                    Log.i("ThirdLogin","host : " + host);
//                    Log.i("ThirdLogin","act : " + act);
//                    Log.i("ThirdLogin","source : " + source);
                    if ("www.gm88.com".equalsIgnoreCase(host) && "callback".equalsIgnoreCase(act) && "app".equalsIgnoreCase(source)){
//                        UserPresenter.getThirdUserInfo(ThirdLoginFragment.this,url);
                        view.loadUrl(url);
                    }else{
                        view.loadUrl(url);
                    }
            	}
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading("正在加载");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissLoading();
                view.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('body')[0].innerHTML);");
            }
        });
    }

    private void loadLoginUrl(int type,int action){
        Map<String,String> params = new HashMap<String, String>();
        params.put("app","connect");
        params.put("source","app");
        params.put("game_id",Config.getGameId());
        params.put("promote",Config.getPromote(baseActivity));
        if (action == BIND){
            User user = UserPresenter.getUser();
            if (user != null && !TextUtils.isEmpty(user.getSid())){
                params.put("sid",user.getSid());
            }
        }

        switch (type){
            case QQ:
                params.put("open_code","qq");
                break;
            case SINA:
                params.put("open_code","weibo");

                break;
        }
        String url = HttpProxy.getUrlWithQueryString(Config.HOST1,params, Request.DEFAULT_PARAMS_ENCODING);
        Log.i("loadLoginUrl",url);
        mWebView.loadUrl(url);

    }

    class InJavaScriptLocalObj {
        int action;
        public InJavaScriptLocalObj(int action){
            this.action = action;
        }

        @JavascriptInterface
        public void showSource(String html) {

//            Log.d("HTML", html);
            try {
                JSONObject json = new JSONObject(html);
                if (action == BIND){
                    UserPresenter.parseThirdUserInfo(ThirdLoginFragment.this,json,true);
                }else{
                    UserPresenter.parseThirdUserInfo(ThirdLoginFragment.this,json,false);
                }

            }catch (JSONException e){

            }

        }
    }
}
