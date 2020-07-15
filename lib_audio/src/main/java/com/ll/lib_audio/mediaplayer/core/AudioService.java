package com.ll.lib_audio.mediaplayer.core;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.ll.lib_audio.mediaplayer.app.AudioHelper;
import com.ll.lib_audio.mediaplayer.bean.AudioBean;
import com.ll.lib_audio.mediaplayer.event.AudioEvent;
import com.ll.lib_audio.mediaplayer.exception.AudioContextNullException;
import com.ll.lib_audio.mediaplayer.view.notification.NotificationHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * @Auther Kylin
 * @Data 2020/6/17 - 11:13
 * @Package com.ll.lib_audio.mediaplayer.core
 * @Description 音频播放后台服务类：处理音频播放服务和Notification的交互。
 */
public class AudioService extends Service implements NotificationHelper.NotificationHelperListener {
    private static final String TAG = "AudioService";
    private static String DATA_AUDIOS = "data_audios";
    //actions
    private static String ACTION_START = "action_start";

    private AudioBroadcastReceiver mAudioBroadcastReceiver = null;
    private ArrayList<AudioBean> mAudioBeans = null;

    public static void startService(ArrayList<AudioBean> mAudioBean){
        Context context = AudioHelper.getInstance().getContext();
        if (null == context){
            throw new AudioContextNullException();
        }
        Intent intent = new Intent(context, AudioService.class);
        intent.setAction(ACTION_START);
        intent.putExtra(DATA_AUDIOS, mAudioBean);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        registerBroadcastReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mAudioBeans = (ArrayList<AudioBean>) intent.getSerializableExtra(DATA_AUDIOS);
        if (ACTION_START.equals(intent.getAction())){
            //开始播放
            playAudios();
            NotificationHelper.getInstance().init(this);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void playAudios() {
        AudioController.getInstance().setQueue(mAudioBeans);
        AudioController.getInstance().play();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unRegisterBroadcastReceiver();
        //清除前台服务
        stopForeground(true);
    }

    @Override
    public void onNotificationInit() {
        //将服务注册为前台服务，提交服务的保活能力，和Notification绑定
        startForeground(NotificationHelper.NOTIFICATION_ID, NotificationHelper.getInstance().getNotification());
    }

    /**
     *  处理音频播放Eventbus事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioEvent(AudioEvent event){
        Log.w(TAG, "onAudioEvent: " +  event.toString());
        switch (event.eventCode) {
            case EVENT_LOAD:
                onAudioEvent_ShowLoadView(event);
                break;
            case EVENT_START:
                onAudioEvent_ShowStartView(event);
                break;
            case EVENT_RESUME:
                onAudioEvent_ShowStartView(event);
                break;
            case EVENT_PASUE:
                onAudioEvent_ShowPauseView(event);
                break;
            case EVENT_FAVOURITE:
                onAudioEvent_ShowFavouriteView(event);
                break;
        }
    }

    /**
     * 处理音频播放Eventbus加载事件
     * @param event
     */
    private void onAudioEvent_ShowLoadView(AudioEvent event) {
        NotificationHelper.getInstance().showLoadStatus(event.audioBean);
    }

    /**
     * 处理音频播放Eventbus开始事件
     * @param event
     */
    private void onAudioEvent_ShowStartView(AudioEvent event) {
        NotificationHelper.getInstance().showPlayStatus();
    }

    /**
     * 处理音频播放Eventbus暂停事件
     * @param event
     */
    private void onAudioEvent_ShowPauseView(AudioEvent event) {
        NotificationHelper.getInstance().showPauseStatus();
    }

    /**
     * 处理音频播放Eventbus收藏事件
     * @param event
     */
    private void onAudioEvent_ShowFavouriteView(AudioEvent event) {
        NotificationHelper.getInstance().changeFavorStatus(event.audioBean);
    }

    /**
     * 注册广播接收器
     */
    private void registerBroadcastReceiver(){
        if (null == mAudioBroadcastReceiver){
            mAudioBroadcastReceiver = new AudioBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AudioBroadcastReceiver.ACTION_AUDIO_BROADCAST);
            registerReceiver(mAudioBroadcastReceiver, intentFilter);
        }
    }

    /**
     * 反注册广播接收器
     */
    private void unRegisterBroadcastReceiver(){
        if (null != mAudioBroadcastReceiver){
            unregisterReceiver(mAudioBroadcastReceiver);
        }
    }

    /**
     *  广播接收器
     *  1：用于接收Notification上相关按钮发来的音频处理广播：
     *  上一首、下一首、播放、暂停、收藏
     */
    public class AudioBroadcastReceiver extends BroadcastReceiver {
        public static final String TAG = "AudioBroadcastReceiver";

        public static final String ACTION_AUDIO_BROADCAST =
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
                    AudioController.getInstance().changeFavouriteStatus();
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
