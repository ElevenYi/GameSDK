package com.game.sdk.activity.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.game.sdk.activity.adapter.HistoryAdapter;
import com.game.sdk.config.Config;
import com.game.sdk.presenter.UserPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 帐号登录
 * Created by echowang on 16/4/24.
 */
public class AccountLoginFragment extends UserBaseFragment implements View.OnClickListener {
    private Button loginBtn;
    private ImageButton backBtn;
    private EditText phoneText;
    private EditText pwdText;
    private ImageView phoneError;
    private ImageView passportError;

    private ImageView historyUser;

//    private PopupMenu popupMenu;
    private PopupWindow popupWindow;
    private ListView mListView;
    private HistoryAdapter adapter;
    private List<String> phoneList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutByName("fragment_account_login"),null,true);
        loginBtn = (Button) view.findViewById(getIdByName("login_game"));
        backBtn = (ImageButton) view.findViewById(getIdByName("login_back"));
        phoneText = (EditText) view.findViewById(getIdByName("login_phone"));
        pwdText = (EditText) view.findViewById(getIdByName("login_pwd"));
        phoneError = (ImageView) view.findViewById(getIdByName("account_error"));
        passportError = (ImageView) view.findViewById(getIdByName("passport_error"));
        loginBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
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
                }else{
                    phoneError.setVisibility(View.VISIBLE);
                    phoneError.setImageResource(getDrawableByName("x"));
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
                }else {
                    passportError.setVisibility(View.VISIBLE);
                    passportError.setImageResource(getDrawableByName("x"));
                }
            }
        });
        phoneError.setVisibility(View.INVISIBLE);
        passportError.setVisibility(View.INVISIBLE);

        historyUser = (ImageView) view.findViewById(getIdByName("history_user"));
        historyUser.setOnClickListener(this);
//        popupMenu = new PopupMenu(getContext(),historyUser);
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                phoneText.setText(item.getTitle());
//                return false;
//            }
//        });


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView = new ListView(getContext());
        phoneList = new ArrayList<String>();
        adapter = new HistoryAdapter(getContext());
        mListView.setAdapter(adapter);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mListView.setLayoutParams(layoutParams);
        popupWindow = new PopupWindow(mListView, Config.dip2px(getContext(),210), ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null,""));
        popupWindow.update();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = adapter.getItem(position);
                if (item != null){
                    phoneText.setText(item);
                }

                popupWindow.dismiss();
            }
        });

        adapter.setDeleteHistoryListner(new HistoryAdapter.DeleteHistoryListner() {
            @Override
            public void delete(String phone) {
                UserPresenter.deleteHistoryUser(getContext(),phone);
                phoneList.remove(phone);
                adapter.setDataSource(phoneList);
                popupWindow.dismiss();
            }
        });

        UserPresenter.readHistoryUser(this);
    }

    @Override
    public void onClick(View v) {
        if (loginBtn.getId() == v.getId()){
            loginGame();
            return;
        }

        if (backBtn.getId() == v.getId()){
            back();
            return;
        }

        if (historyUser.getId() == v.getId()){
//            popupMenu.show();
            Log.i("history","show");
            popupWindow.showAsDropDown(phoneText,0,0);
        }
    }

    private void loginGame(){
//        GameToast.showLoginSuccess(baseActivity,"登录成功");
//        finishActivity();
        if (UserPresenter.checkPhone(getPhone()) && UserPresenter.checkPassport(getPassword())){
            UserPresenter.login(this);
        }else{
            showToast("手机号或密码不对");
        }

    }

    private void back(){
//        finishActivity();
        onBackPressed();
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
    public void setHistoryUser(Set<String> userList) {
        super.setHistoryUser(userList);

        if (userList != null){
//            Menu menu = popupMenu.getMenu();
            phoneList.clear();
            for (String phone : userList){
//                menu.add(phone);
                Log.i("history",phone);
                phoneList.add(phone);
            }
            adapter.setDataSource(phoneList);
//            adapter.notifyDataSetChanged();
        }
    }
}
