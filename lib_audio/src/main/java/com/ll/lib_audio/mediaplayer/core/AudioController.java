package com.ll.lib_audio.mediaplayer.core;

import android.util.Log;

import com.ll.lib_audio.mediaplayer.event.AudioEvent;
import com.ll.lib_audio.mediaplayer.exception.AudioQueueEmptyException;
import com.ll.lib_audio.mediaplayer.bean.AudioBean;
import com.ll.lib_audio.mediaplayer.greendao.GreenDaoHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Random;

/**
 * @Auther kylin
 * @Data 2020/6/4 - 22:23
 * @Package com.ll.lib_audio.mediaplayer.core
 * @Description
 */
public class AudioController {
    /**
     * 播放模式
     */
    public enum PlayMode {
        LOOP,//循环
        RANDOM,//随机
        REPEAT,//单曲
    }

    //------------------单例----------------------//
    private static AudioController mInstance = null;

    private AudioController() {
        EventBus.getDefault().register(this);
        mCurrrentIndex = 0;
        mAudioPlayer = new AudioPlayer();
        mPlayMode = PlayMode.LOOP;
        mQueue = new ArrayList<AudioBean>();
    }

    public static AudioController getInstance() {
        if (null == mInstance) {
            synchronized (AudioController.class) {
                if (null == mInstance) {
                    mInstance = new AudioController();
                }
            }
        }
        return mInstance;
    }
    //------------------单例----------------------//

    private int mCurrrentIndex = 0;
    private AudioPlayer mAudioPlayer;
    private PlayMode mPlayMode;
    private ArrayList<AudioBean> mQueue;
    private static final String TAG = "AudioController";

    //--------------处理播放队列----------------------

    /**
     * 获取播放队列
     *
     * @return
     */
    public ArrayList<AudioBean> getQueue() {
        if (null == mQueue) {
            mQueue = new ArrayList<AudioBean>();
        }
        return mQueue;
    }

    public void removeFromQueue(AudioBean bean){
        if(this.mQueue == null){
            return;
        }
        if (queryQueue(bean) <= -1) {
            return;
        }
        if (getCurrentAudioBean().getId() == bean.getId()){
            //当前播放歌曲
            pause();
            releaseAudioPlayer();
        }

        mQueue.remove(bean);
    }

    /**
     * 设置播放队列
     *
     * @param queue
     */
    public void setQueue(ArrayList<AudioBean> queue) {
        this.mQueue = queue;
         this.mCurrrentIndex = 0;
    }

    /**
     *  添加播放音频
     * @param bean
     */
    public void addAudio(AudioBean bean) {
        this.addAudio(bean, 0);
    }

    /**
     * 添加播放队列
     *
     * @param queue
     * @param index
     */
    public void addQueue(ArrayList<AudioBean> queue, int index) {
        this.mQueue.addAll(queue);
        this.mCurrrentIndex = 0;
    }

    /**
     * 添加音频到播放队列对于位置
     * @param index
     * @param bean
     */
    private void addQueue(int index, AudioBean bean) {
        if (null == mQueue) {
            throw new AudioQueueEmptyException("addQueue : the play queue is empty, or may be the index is error");
        }
        this.mQueue.add(index, bean);
    }

    /**
     * 查询播放队列中音频所在的位置
     * @param bean
     * @return
     */
    private int queryQueue(AudioBean bean) {
        if (null == mQueue || mQueue.isEmpty() ){
            return -1;
        }
        int index = mQueue.indexOf(bean);
        return index;
    }

    /**
     * 添加单一歌曲到index位置，播放音频
     * @param bean
     * @param index
     */
    public void addAudio(AudioBean bean, int index) {
        if (null == mQueue) {
            throw new AudioQueueEmptyException("addAudio : the play queue is empty, or may be the index is error");
        }
        int queryIndex = queryQueue(bean);
        if (queryIndex < 0){
            // 没有添加过
            addQueue(index, bean);
            setCurrrentIndex(index);
        } else {
            AudioBean nowPlaying = getCurrentAudioBean();
            if (nowPlaying.getId() == bean.getId()){
                //添加过且正在播放
            } else {
                setCurrrentIndex(queryIndex);
            }
        }
    }

    /**
     * 获取当前播放模式
     *
     * @return
     */
    public PlayMode getPlayMode() {
        return mPlayMode;
    }

    /**
     * 设置播放模式
     *
     * @param playMode
     */
    public void setPlayMode(PlayMode playMode) {
        this.mPlayMode = playMode;
        //发送播放模式修改事件
        EventBus.getDefault().post(
                new AudioEvent(AudioEvent.Status.EVENT_PLAY_MODE,
                        "set play mode" + playMode.toString(),
                        null,
                        playMode));
    }

    /**
     * 获取当前播放index
     *
     * @return
     */
    public int getCurrrentIndex() {
        return mCurrrentIndex;
    }

    /**
     * 设置播放index,并且播放
     *
     * @param index
     * @throws AudioQueueEmptyException
     */
    public void setCurrrentIndex(int index) throws AudioQueueEmptyException {
        if (null == mQueue) {
            throw new AudioQueueEmptyException("setCurrrentIndex : the play queue is empty, set it before current index");
        }
        this.mCurrrentIndex = index;
        play();
    }

    /**
     * 获取当前是否是播放状态
     *
     * @return
     */
    public boolean isStartState() {
        return CommonMediaPlayer.Status.STARTED == getStatus();
    }

    /**
     * 获取当前是否是暂停状态
     *
     * @return
     */
    public boolean isPauseState() {
        return CommonMediaPlayer.Status.PAUSED == getStatus();
    }

    /**
     * 获取当前是否是准备状态
     *
     * @return
     */
    public boolean isIdleState() {
        return CommonMediaPlayer.Status.IDLE == getStatus();
    }

    /**
     * 获取当前是否是停止状态
     *
     * @return
     */
    public boolean isStopState() {
        return CommonMediaPlayer.Status.STOPED == getStatus();
    }

    /**
     * 获取当前播放状态
     *
     * @return
     */
    public CommonMediaPlayer.Status getStatus() {
        if (null == mAudioPlayer) {
            return CommonMediaPlayer.Status.STOPED;
        }
        return mAudioPlayer.getStatus();
    }

    /**
     *  修改歌曲收藏状态
     */
    public void changeFavourite(){
        changeFavourite(getCurrentAudioBean());
    }


    /**
     *  修改歌曲收藏状态
     * @param bean
     */
    public void changeFavourite(AudioBean bean){
        if (null != GreenDaoHelper.getInstance().queryFavouriteAudioBean(bean)){
            //当前歌曲已经收藏
            GreenDaoHelper.getInstance().removeFavouriteAudioBean(bean);
            EventBus.getDefault().post(new AudioEvent(AudioEvent.Status.EVENT_REMOVE_FAVOURITE,
                    "remove audio bean from db" + bean.getId(), bean));
        } else {
            //当前歌曲未收藏
            GreenDaoHelper.getInstance().addFavouriteAudioBean(bean);
            EventBus.getDefault().post(new AudioEvent(AudioEvent.Status.EVENT_ADD_FAVOURITE,
                    "add audio bean into db" + bean.getId(), bean));
        }
    }

    /**
     * 播放
     */
    public void play() {
        AudioBean audioBean = getCurrentAudioBean();
        mAudioPlayer.loadAudio(audioBean);
    }

    /**
     * 暂停播放
     */
    public void pause() {
        mAudioPlayer.pause();
    }

    /**
     * 恢复播放
     */
    public void resume() {
        mAudioPlayer.resume();
    }

    /**
     * 释放资源
     */
    public void releaseAudioPlayer() {
        mAudioPlayer.release();
    }
    /**
     * 释放资源
     */
    public void release() {
        releaseAudioPlayer();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 播放上一首
     */
    public void preview() {
        AudioBean audioBean = getPreviewAudioBean();
        mAudioPlayer.loadAudio(audioBean);
    }

    /**
     * 播放下一首
     */
    public void next() {
        AudioBean audioBean = getNextAudioBean();
        Log.d(TAG, "next: " + audioBean);
        mAudioPlayer.loadAudio(audioBean);
    }

    /**
     * 切换播放和暂停
     */
    public void switchPlayOrPause() {
        if (isPauseState()) {
            resume();
        } else if (isStartState()) {
            pause();
        }
    }

    /**
     * 获取播放歌曲
     * @return
     */
    private AudioBean getPlaying() {
        if (null != mQueue && !mQueue.isEmpty()
                && mCurrrentIndex >= 0 && mCurrrentIndex < mQueue.size()) {
            return mQueue.get(mCurrrentIndex);
        } else {
            throw new AudioQueueEmptyException("getPlaying : the play queue is empty, or may be the index is error");
        }
    }

    /**
     * 获取当前播放歌曲
     *
     * @return
     */
    public AudioBean getCurrentAudioBean() {
        return getPlaying();
    }


    /**
     * 获取列表上一首
     *
     * @return
     */
    private AudioBean getPreviewAudioBean() {
        switch (mPlayMode) {
            case LOOP:
                mCurrrentIndex = (mCurrrentIndex + mQueue.size() - 1) % mQueue.size();
                break;
            case RANDOM:
                mCurrrentIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
                break;
            case REPEAT:
                break;
        }
        return getPlaying();
    }

    /**
     * 获取列表下一首
     *
     * @return
     */
    private AudioBean getNextAudioBean() {
        switch (mPlayMode) {
            case LOOP:
                mCurrrentIndex = (mCurrrentIndex + 1) % mQueue.size();
                break;
            case RANDOM:
                mCurrrentIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
                break;
            case REPEAT:
                break;
        }
        return getPlaying();
    }

    /**
     *  处理接收的播放完成和出错事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCurrentAudioEvent(AudioEvent event) {
        AudioEvent.Status code = event.eventCode;
        switch (code) {
            case EVENT_COMPLETION:
                // todo 处理接收的播放完成事件
                break;
            case EVENT_ERROR:
                // todo 处理接收的播放出错事件
                break;
        }
    }
}
