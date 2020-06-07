package com.ll.lib_audio.mediaplayer.core;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import com.ll.lib_audio.mediaplayer.app.AudioHelper;
import com.ll.lib_audio.mediaplayer.bean.AudioBean;
import com.ll.lib_audio.mediaplayer.event.AudioEvent;

import org.greenrobot.eventbus.EventBus;


/**
 * @Auther Kylin
 * @Data 2020/5/20 - 16:41
 * @Package com.ll.lib_audio.mediaplayer.audio
 * @Description 音频播放控制器：对外发送各种播放事件类型
 */
public class AudioPlayer implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener,
        AudioFocusManager.AudioFocusListener {

    private static final int TIME_MSG = 0x01;
    private static final int TIME_INVAL = 100;
    private static final String TAG = "AudioPlayer";

    private CommonMediaPlayer mCommonMediaPlayer;
    private AudioFocusManager mAudioFocusManager;//音频焦点监听器
    private WifiManager.WifiLock mWifiLock;//处理后台保活

    //标志位用于判断当前状态是否是因为焦点竞争失败丢失
    private boolean isPauseByFouceLossTransient = false;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_MSG:
                    break;
            }
        }
    };

    public AudioPlayer() {
        init();
    }

    private void init() {
        Context mContext = AudioHelper.getInstance().getContext();
        if (null != mContext) {
            mCommonMediaPlayer = new CommonMediaPlayer();
            mCommonMediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);//低电量版本播放模式
            mCommonMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 注册监听器
            mCommonMediaPlayer.setOnPreparedListener(this);
            mCommonMediaPlayer.setOnBufferingUpdateListener(this);
            mCommonMediaPlayer.setOnCompletionListener(this);
            mCommonMediaPlayer.setOnErrorListener(this);

            mWifiLock = ((WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                    .createWifiLock(WifiManager.WIFI_MODE_FULL, TAG);
            mAudioFocusManager = new AudioFocusManager(mContext, this);
        }
    }

    /**
     * 加载音频，加载完成后会自动回调 @startAudio
     *
     * @param audioBean
     */
    public void loadAudio(AudioBean audioBean) {
        try {
            mCommonMediaPlayer.reset();
            mCommonMediaPlayer.setDataSource(audioBean.getUrl());
            mCommonMediaPlayer.prepareAsync();
            // UIEvent -- 加载完成
            EventBus.getDefault().post(new AudioEvent(AudioEvent.Status.EVENT_LOAD, "audio load", audioBean));
        } catch (Exception e) {
            e.printStackTrace();
            // UIEvent -- 加载异常
            EventBus.getDefault().post(new AudioEvent(AudioEvent.Status.EVENT_LOAD_ERROR, "audio error :" + e.getMessage(), audioBean));
        }
    }

    private void startAudio() {
        //先获取焦点
        if (!mAudioFocusManager.requestAudioFocus()) {
            //未获取到焦点
            Log.e(TAG, "startAudio: request audio focus failed");
            return;
        }
        mCommonMediaPlayer.start();

        mWifiLock.acquire();//启用wifi锁
        mHandler.sendEmptyMessage(TIME_MSG);//更新进度
        // UIEvent -- 播放开始
        EventBus.getDefault().post(new AudioEvent(AudioEvent.Status.EVENT_START, "audio start"));
    }

    /**
     * 音频暂停播放
     */
    public void pause() {
        if (CommonMediaPlayer.Status.STARTED == getStatus()) {
            mCommonMediaPlayer.pause();
            // 释放wifi lock锁
            if (mWifiLock.isHeld()) {
                mWifiLock.release();
            }
            // 释放音频焦点
            if (mAudioFocusManager != null) {
                mAudioFocusManager.abandonAudioFocus();
            }
            // UIEvent -- 播放暂停
            EventBus.getDefault().post(new AudioEvent(AudioEvent.Status.EVENT_PASUE, "audio pause"));
        }
    }

    /**
     * 音频恢复播放
     */
    public void resume() {
        if (CommonMediaPlayer.Status.PAUSED == getStatus()) {
            mCommonMediaPlayer.start();
            // UIEvent -- 播放恢复
            EventBus.getDefault().post(new AudioEvent(AudioEvent.Status.EVENT_RESUME, "audio resume"));
        }
    }

    /**
     * 清空播放器资源
     */
    public void release() {
        if (null != mCommonMediaPlayer) {
            mCommonMediaPlayer.release();
            mCommonMediaPlayer = null;
        }

        if (null != mWifiLock) {
            mWifiLock.release();
            mWifiLock = null;
        }

        if (null != mAudioFocusManager) {
            mAudioFocusManager.abandonAudioFocus();
            mAudioFocusManager = null;
        }
        // UIEvent -- 资源销毁
        EventBus.getDefault().post(new AudioEvent(AudioEvent.Status.EVENT_RELEASE, "audio release"));
    }

    /**
     * 获取音频当前播放状态
     *
     * @return
     */
    public CommonMediaPlayer.Status getStatus() {
        if (null != mCommonMediaPlayer) {
            return mCommonMediaPlayer.getStaus();
        }

        return CommonMediaPlayer.Status.STOPED;
    }

    /**
     * 设置音量
     *
     * @param left  左声道
     * @param right 右声道
     */
    public void setVolume(float left, float right) {
        if (null != mCommonMediaPlayer) {
            mCommonMediaPlayer.setVolume(left, right);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        startAudio();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        EventBus.getDefault().post(new AudioEvent(AudioEvent.Status.EVENT_COMPLETION, "audio onCompletion"));
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        EventBus.getDefault().post(new AudioEvent(AudioEvent.Status.EVENT_ERROR, "audio error :"));
        return true;
    }

    @Override
    public void audioFocusGrant() {
        //焦点获取
        setVolume(1.0f, 1.0f);//恢复音量
        if (isPauseByFouceLossTransient) {
            //由于焦点竞争失败，需要重新播放
            resume();
            isPauseByFouceLossTransient = false;
        }
    }

    @Override
    public void audioFocusLoss() {
        //焦点永久失去
        pause();
    }

    @Override
    public void audioFocusLossTransient() {
        //焦点短暂失去，例如电话
        setVolume(0.0f, 0.0f);
        pause();
        isPauseByFouceLossTransient = true;
    }

    @Override
    public void audioFocusLossCanDuck() {
        //焦点瞬时失去，例如短信音，通知音
        setVolume(0.3f, 0.3f);
    }
}
