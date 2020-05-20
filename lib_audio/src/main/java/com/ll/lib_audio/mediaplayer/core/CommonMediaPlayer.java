package com.ll.lib_audio.mediaplayer.core;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * @Auther Kylin
 * @Data 2020/5/13 - 14:49
 * @Package com.ll.lib_audio.mediaplayer.core
 * @Description
 */
public class CommonMediaPlayer extends MediaPlayer implements MediaPlayer.OnCompletionListener{
    /**
     * 音频播放状态
     */
    public enum Status {
        IDLE,
        INITIALIZED,
        STARTED,
        PAUSED,
        STOPED,
        COMPLETED,
    }
    // 当前播放状态标识
    private Status mStatus = Status.IDLE;

    private OnCompletionListener mListener = null;

    public CommonMediaPlayer() {
        super();

        mStatus = Status.IDLE;
        super.setOnCompletionListener(this);
    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, IllegalStateException, SecurityException {
        super.setDataSource(path);
        mStatus = Status.INITIALIZED;
    }

    @Override
    public void start() throws IllegalStateException {
        super.start();
        mStatus = Status.STARTED;
    }

    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        mStatus = Status.STOPED;
    }

    @Override
    public void pause() throws IllegalStateException {
        super.pause();
        mStatus = Status.PAUSED;
    }

    @Override
    public void reset() {
        super.reset();
        mStatus = Status.IDLE;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mStatus = Status.COMPLETED;
        //        if (mListener != null){
        //            mListener.onCompletion(mp);
        //        }
    }

    /**
     * 获取当前播放音频状态
     * @return
     */
    public Status getStaus() {
        return mStatus;
    }

    /**
     * 是否播放完成
     * @return
     */
    public boolean isCompleted(){
        return  mStatus == Status.COMPLETED;
    }

    public void SetOnCompletedListener(OnCompletionListener mListener){
        this.mListener = mListener;
    }
}