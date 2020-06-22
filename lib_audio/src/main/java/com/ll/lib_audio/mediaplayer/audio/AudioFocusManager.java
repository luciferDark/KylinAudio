package com.ll.lib_audio.mediaplayer.audio;

import android.content.Context;
import android.media.AudioManager;

/**
 * @Auther Kylin
 * @Data 2020/5/20 - 16:44
 * @Package com.ll.lib_audio.mediaplayer.audio
 * @Description 音频焦点管理类
 */
public class AudioFocusManager implements AudioManager.OnAudioFocusChangeListener {
    private AudioFocusListener mAudioFocusListener = null;
    private AudioManager mAudioManager = null;

    public AudioFocusManager(Context context, AudioFocusListener mAudioFocusListener) {
        if (null != context) {
            this.mAudioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        }
        this.mAudioFocusListener = mAudioFocusListener;
    }

    public boolean requestAudioFocus() {
        if (null == this.mAudioManager) {
            return false;
        }

        return this.mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
                == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    public void abandonAudioFocus() {
        if (null == this.mAudioManager) {
            return;
        }
        this.mAudioManager.abandonAudioFocus(this);
    }

    @Override
    public void onAudioFocusChange(final int focusChange) {
        switch (focusChange){
            case AudioManager.AUDIOFOCUS_GAIN: //焦点获取
                if (null != this.mAudioFocusListener){
                    this.mAudioFocusListener.audioFocusGrant();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS: //焦点失去
                if (null != this.mAudioFocusListener){
                    this.mAudioFocusListener.audioFocusLoss();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: //焦点短暂失去，例如电话
                if (null != this.mAudioFocusListener){
                    this.mAudioFocusListener.audioFocusLossTransient();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK: //焦点瞬时失去，例如短信音，通知音
                if (null != this.mAudioFocusListener){
                    this.mAudioFocusListener.audioFocusLossCanDuck();
                }
                break;
        }
    }

    /**
     * 音频焦点状态监听接口
     */
    public interface AudioFocusListener {
        void audioFocusGrant();//焦点获取

        void audioFocusLoss();//焦点永久失去

        void audioFocusLossTransient();//焦点短暂失去，例如电话

        void audioFocusLossCanDuck();//焦点瞬时失去，例如短信音，通知音
    }
}
