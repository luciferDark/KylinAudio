package com.ll.lib_audio.mediaplayer.core;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.ll.lib_audio.mediaplayer.app.AudioHelper;

/**
 * @Auther Kylin
 * @Data 2020/6/17 - 11:13
 * @Package com.ll.lib_audio.mediaplayer.core
 * @Description
 */
public class AudioService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public class AudioBroadcastReceiver extends BroadcastReceiver {
        public static final String TAG = "AudioBroadcastReceiver";

        public static final String ACTION_AUDIO_BROADCAST =
//                AudioHelper.getInstance().getContext().getPackageName() +
                        "com.ll.lib_audio.mediaplayer.core.AUDIO_BROADCAST_RECEIVER";
        public static final String EXTRA = "extra";

        public static final String EXTRA_PLAY_PAUSE = "extra_play_pause";
        public static final String EXTRA_FAVOR = "extra_favor";
        public static final String EXTRA_PREV = "extra_prev";
        public static final String EXTRA_NEXT = "extra_next";


        public static final int REQUEST_CODE_PLAY_PAUSE = 0x1001;
        public static final int REQUEST_CODE_PREV = 0x1002;
        public static final int REQUEST_CODE_NEXT = 0x1003;
        public static final int REQUEST_CODE_FAVOR = 0x1004;


        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent || TextUtils.isEmpty(intent.getAction())) {
                return;
            }
            final String extra = intent.getStringExtra(EXTRA);
            Log.d(TAG, "onReceive: extra=" + extra);
            switch (extra) {
                case EXTRA_PLAY_PAUSE:
                    AudioController.getInstance().switchPlayOrPause();
                    break;
                case EXTRA_FAVOR:
                    break;
                case EXTRA_PREV:
                    AudioController.getInstance().preview();
                    break;
                case EXTRA_NEXT:
                    AudioController.getInstance().next();
                    break;
            }
        }
    }
}
