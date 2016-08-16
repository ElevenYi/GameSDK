package com.game.sdk.bean;

/**
 * Created by echowang on 16/4/23.
 */
public class User {
    private String sid;
    private String uid;
    private String nickName;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public interface UserListener{
        void onSuccess(User userBean);
        void onFail(String msg);
    }
}
