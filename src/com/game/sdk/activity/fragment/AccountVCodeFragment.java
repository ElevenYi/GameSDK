package com.game.sdk.activity.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.sdk.activity.GameToast;
import com.game.sdk.presenter.UserPresenter;

/**
 * 短信验证码
 * Created by echowang on 16/4/24.
 */
public class AccountVCodeFragment extends UserBaseFragment implements View.OnClickListener {
    private ImageButton backBtn;
    private Button tryBtn;
    private Button registerBtn;
    private EditText vcodeText;
    private TextView phonetText;
    private ImageView sendSuccess;

    private String phone;
    private String pwd;
    private String type;

    private int delayTimes = 60;

    private final static int DELAY = 100;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            removeMessages(DELAY);
            delayTimes--;
            if (delayTimes > 0){
                sendEmptyMessageDelayed(DELAY,1000);
                tryBtn.setText(getString(getStringByName("vcode_try"),delayTimes));
            }else if (delayTimes == 0){
                sendSuccess.setVisibility(View.INVISIBLE);
                tryBtn.setText(getString(getStringByName("send_vcode")));
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutByName("fragment_account_vcode"),null,true);
        backBtn = (ImageButton) view.findViewById(getIdByName("vcode_back"));
        tryBtn = (Button) view.findViewById(getIdByName("vcode_try"));
        registerBtn = (Button) view.findViewById(getIdByName("vcode_register_account"));
        vcodeText = (EditText) view.findViewById(getIdByName("register_vcode"));
        phonetText = (TextView) view.findViewById(getIdByName("vcode_phone"));
        sendSuccess = (ImageView) view.findViewById(getIdByName("send_success"));

        backBtn.setOnClickListener(this);
        tryBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        phone = getArguments().getString("phone");
        pwd = getArguments().getString("pwd");
        type = getArguments().getString("type");

        tryBtn.setText(getString(getStringByName("vcode_try"),delayTimes));
        phonetText.setText(getString(getStringByName("vcode_message"),phone));

        handler.sendEmptyMessageDelayed(DELAY,1000);

        sendSuccess.setVisibility(View.VISIBLE);
        vcodeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (backBtn.getId() == v.getId()){
            back();
            return;
        }

        if (tryBtn.getId() == v.getId()){
            trySendCode();
            return;
        }

        if (registerBtn.getId() == v.getId()){
            registerAccount();
            return;
        }
    }

    private void back(){
//        finishActivity();
        delayTimes = 0;
        handler.removeMessages(DELAY);
        onBackPressed();
    }

    private void trySendCode(){
//        GameToast.showLoginSuccess(baseActivity,"发送成功");
        if (delayTimes == 0){
            UserPresenter.sendVode(this);
        }
    }

    private void registerAccount(){
//        GameToast.showLoginSuccess(baseActivity,"注册成功");
//        finishActivity();
        if ("register".equalsIgnoreCase(type)){
            delayTimes = 0;
            handler.removeMessages(DELAY);
            UserPresenter.register(this);
            return;
        }

        if ("bind".equalsIgnoreCase(type)){
            delayTimes = 0;
            handler.removeMessages(DELAY);
            UserPresenter.bindPhone(this);
            return;
        }
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public String getVcode() {
        return vcodeText.getEditableText().toString();
    }

    @Override
    public void sendCodeResult(boolean result, String msg) {
        super.sendCodeResult(result, msg);

        if (result){
            delayTimes = 60;
            handler.sendEmptyMessageDelayed(DELAY,1000);
            sendSuccess.setVisibility(View.VISIBLE);
        }else{
            showToast(msg);
        }
    }
}
