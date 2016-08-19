package com.game.sdk;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by eleven on 16/8/15.
 */
public class MainApplication extends Application {

    private static MainApplication sInstance;

    private Context mainActivityContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        //初始化TalkingData数据
        //初始化ImageLoader策略
        initImageLoaderParams();
    }

    private void initImageLoaderParams() {
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
    }


    public static MainApplication getInstance() {
        return sInstance;
    }

    public void setMainActivityContext(Context mainActivityContext) {
        this.mainActivityContext = mainActivityContext;
    }

    public Context getMainActivityContext() {
        return mainActivityContext;
    }
}
