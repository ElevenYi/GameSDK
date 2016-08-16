package com.game.sdk.activity.fragment;

import android.os.Bundle;
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

import com.game.sdk.presenter.UserPresenter;

/**
 * 手机号注册
 * Created by echowang on 16/4/24.
 */
public class AccountRegisterFragment extends UserBaseFragment implements View.OnClickListener {
    private ImageButton backBtn;
    private Button registerBtn;
    private EditText phoneText;
    private EditText pwdText;
    private ImageView phoneError;
    private ImageView passportError;
    private TextView errorText;

    private String type;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutByName("fragment_account_register"),null,true);
        backBtn = (ImageButton) view.findViewById(getIdByName("register_back"));
        registerBtn = (Button) view.findViewById(getIdByName("register_account"));
        phoneText = (EditText) view.findViewById(getIdByName("register_phone"));
        pwdText = (EditText) view.findViewById(getIdByName("register_pwd"));
        phoneError = (ImageView) view.findViewById(getIdByName("account_error"));
        passportError = (ImageView) view.findViewById(getIdByName("passport_error"));
        errorText = (TextView) view.findViewById(getIdByName("error_msg"));

        phoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (UserPresenter.checkPhone(getPhone())){
                    phoneError.setVisibility(View.VISIBLE);
                    phoneError.setImageResource(getDrawableByName("correct"));
                    errorText.setVisibility(View.GONE);
                }else{
                    phoneError.setVisibility(View.VISIBLE);
                    phoneError.setImageResource(getDrawableByName("x"));
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(getStringByName("account_error"));
                }
            }
        });

        pwdText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (UserPresenter.checkPassport(getPassword())){
                    passportError.setVisibility(View.VISIBLE);
                    passportError.setImageResource(getDrawableByName("correct"));
                    errorText.setVisibility(View.GONE);
                }else {
                    passportError.setVisibility(View.VISIBLE);
                    passportError.setImageResource(getDrawableByName("x"));
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(getStringByName("passport_error"));
                }
            }
        });
        phoneError.setVisibility(View.INVISIBLE);
        passportError.setVisibility(View.INVISIBLE);
        errorText.setVisibility(View.GONE);

        backBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getString("type");
        if ("bind".equalsIgnoreCase(type)){
            registerBtn.setText("绑定");
        }else{
            registerBtn.setText("注册");
        }
    }

    @Override
    public void onClick(View v) {
        if (backBtn.getId() == v.getId()){
            back();
            return;
        }

        if (registerBtn.getId() == v.getId()){
            registerAccount();
            return;
        }
    }

    private void back(){
//        finishActivity();
        onBackPressed();
    }

    private void registerAccount(){
        if (UserPresenter.checkPhone(getPhone()) && UserPresenter.checkPassport(getPassword())){
            UserPresenter.sendVode(this);
        }else{
            showToast("手机号或密码不对");
        }

    }

    @Override
    public String getPhone() {
        return phoneText.getEditableText().toString();
    }

    @Override
    public String getPassword() {
        return pwdText.getEditableText().toString();
    }

    @Override
    public void sendCodeResult(boolean result, String msg) {
        super.sendCodeResult(result, msg);
        if (result){
            AccountVCodeFragment accountVCodeFragment = AccountVCodeFragment.getFragmentByName(baseActivity,AccountVCodeFragment.class);
            Bundle bundle = new Bundle();
            bundle.putString("phone",getPhone());
            bundle.putString("pwd",getPassword());
            bundle.putString("type",type);
            accountVCodeFragment.setArguments(bundle);
            redirectFragment(accountVCodeFragment);
        }else {
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(msg);
        }
    }
}
