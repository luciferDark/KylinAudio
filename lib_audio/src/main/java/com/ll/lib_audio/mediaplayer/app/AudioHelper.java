package com.ll.lib_audio.mediaplayer.app;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * @Auther Kylin
 * @Data 2020/5/20 - 17:20
 * @Package com.ll.lib_audio.mediaplayer.app
 * @Description 音频播放对外接口类
 */
public class AudioHelper {
    private WeakReference<Context> mWeakContext = null;
    //------------------单例----------------------//
    private static AudioHelper mInstance = null;

    private AudioHelper() {
    }

    public static void init(Context context) {
        getInstance().setContext(context);
    }

    public static AudioHelper getInstance() {
        if (null == mInstance) {
            synchronized (AudioHelper.class) {
                if (null == mInstance) {
                    mInstance = new AudioHelper();
                }
            }
        }
        return mInstance;
    }
    //------------------单例----------------------//

    public Context getContext() {
        if (null == mWeakContext) {
            return null;
        }
        return mWeakContext.get();
    }

    private void setContext(Context context) {
        if (null != context) {
            mWeakContext = new WeakReference<>(context);
        }
    }
}
