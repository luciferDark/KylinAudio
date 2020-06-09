package com.ll.audio.application;

import android.app.Application;

import com.ll.lib_audio.mediaplayer.app.AudioHelper;

/**
 * @Auther Kylin
 * @Data 2020/6/9 - 17:45
 * @Package com.ll.audio.application
 * @Description
 */
public class KylinApplication extends Application {

    private static KylinApplication mApplicatioin = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicatioin = this;
        init();
    }

    private void init() {
        //初始化音频SDK
        AudioHelper.init(this);
    }

    public static KylinApplication getApplicatioin() {
        return mApplicatioin;
    }
}
