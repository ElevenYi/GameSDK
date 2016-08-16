package com.game.sdk.bean;

import java.io.Serializable;

/**
 * Created by echowang on 16/4/23.
 */
public class Purchase implements Serializable {
    private double coins;
    private String serverid;
    private String roleid;
    private String developerInfo;

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public String getServerid() {
        return serverid;
    }

    public void setServerid(String serverid) {
        this.serverid = serverid;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getDeveloperInfo() {
        return developerInfo;
    }

    public void setDeveloperInfo(String developerInfo) {
        this.developerInfo = developerInfo;
    }
}
