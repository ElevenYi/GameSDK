package com.game.sdk;

import android.app.Application;

/**
 * Created by eleven on 16/8/15.
 */
public class MainApplication extends Application {

    private static MainApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

    }


    public static MainApplication getInstance() {
        return sInstance;
    }
}
