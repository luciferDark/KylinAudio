package com.ll.lib_audio.mediaplayer.audio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;

import com.ll.lib_audio.mediaplayer.core.CommonMediaPlayer;


/**
 * @Auther Kylin
 * @Data 2020/5/20 - 16:41
 * @Package com.ll.lib_audio.mediaplayer.audio
 * @Description 音频播放控制器：对外发送各种播放事件类型
 */
public class AudioPlayer implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnErrorListener,
        AudioFocusManager.AudioFocusListener {

    private static final int TIME_MSG = 0x01;
    private static final int TIME_INVAL = 100;

    private CommonMediaPlayer mCommonMediaPlayer;
    private WifiManager.WifiLock mWifiLock;//处理后台保活
    private AudioFocusManager mAudioFocusManager;//音频焦点监听器

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TIME_MSG:
                    break;
            }
        }
    };

    public AudioPlayer() {
        init();
    }

    private void init() {
        mCommonMediaPlayer = new CommonMediaPlayer();
        mCommonMediaPlayer.setWakeMode(null, PowerManager.PARTIAL_WAKE_LOCK);//低电量版本播放模式
        mCommonMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mCommonMediaPlayer.setOnBufferingUpdateListener(this);
        mCommonMediaPlayer.setOnCompletionListener(this);
        mCommonMediaPlayer.setOnErrorListener(this);


    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void audioFocusGrant() {

    }

    @Override
    public void audioFocusLoss() {

    }

    @Override
    public void audioFocusLossTransient() {

    }

    @Override
    public void audioFocusLossCanDuck() {

    }
}
