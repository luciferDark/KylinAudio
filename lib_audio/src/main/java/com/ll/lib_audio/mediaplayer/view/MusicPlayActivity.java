package com.ll.lib_audio.mediaplayer.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ll.lib_audio.R;
import com.ll.lib_audio.mediaplayer.bean.AudioBean;
import com.ll.lib_audio.mediaplayer.core.AudioController;
import com.ll.lib_audio.mediaplayer.event.AudioEvent;
import com.ll.lib_audio.mediaplayer.greendao.GreenDaoHelper;
import com.ll.lib_audio.mediaplayer.view.audioIndicatorView.AudioIndicatorView;
import com.ll.lib_image_loader.glide.app.ImageLoaderManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @Auther Kylin
 * @Data 2020/7/15 - 16:59
 * @Package com.ll.lib_audio.mediaplayer.view
 * @Description
 */
public class MusicPlayActivity extends FragmentActivity
        implements View.OnClickListener {
    //    UI
    private RelativeLayout mRootLayout;
    private ImageView mBackBtn;
    private ImageView mShateBtn;
    private TextView mSongNameTxt;
    private TextView mSongSingerTxt;

    private ImageView mFavorBtn;
    private ImageView mDownloadBtn;
    private ImageView mRingBtn;
    private ImageView mCommentBtn;
    private ImageView mInfoBtn;

    private TextView mCurrentTimeTxt;
    private TextView mEndTimeTxt;
    private SeekBar mProgress;

    private ImageView mPlayModeBtn;
    private ImageView mPreSongBtn;
    private ImageView mPlayPauseBtn;
    private ImageView mNextSongBtn;
    private ImageView mMusicListBtn;
    private AudioIndicatorView mAudioIndicatorView;

    //    Datas
    private AudioBean mAudioBean;
    private AudioController.PlayMode mPlayMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(MusicPlayActivity.this);
        setContentView(R.layout.layout_music_play_acitivity);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(MusicPlayActivity.this);
    }

    public static void startMusicActivity(Context context){
        if(context == null){
            return;
        }

        Intent intent = new Intent(context, MusicPlayActivity.class);
        context.startActivity(intent);
    }

    /**
     * 初始化
     */
    private void init() {
        initData();

        initUI();
        initUIState(true);
        initListener();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mAudioBean = AudioController.getInstance().getCurrentAudioBean();
        mPlayMode = AudioController.getInstance().getPlayMode();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        mRootLayout = findViewById(R.id.layout_music_play_root);

        mBackBtn = findViewById(R.id.layout_music_play_top_btn_back);
        mShateBtn = findViewById(R.id.layout_music_play_top_btn_share);
        mSongNameTxt = findViewById(R.id.layout_music_play_top_txt_song_name);
        mSongSingerTxt = findViewById(R.id.layout_music_play_top_txt_singer);

        mFavorBtn = findViewById(R.id.layout_music_play_bottom_btn_favor);
        mDownloadBtn = findViewById(R.id.layout_music_play_bottom_btn_download);
        mRingBtn = findViewById(R.id.layout_music_play_bottom_btn_ring);
        mCommentBtn = findViewById(R.id.layout_music_play_bottom_btn_comment);

        mCurrentTimeTxt = findViewById(R.id.layout_music_play_bottom_txt_current_time);
        mEndTimeTxt = findViewById(R.id.layout_music_play_bottom_txt_end_time);
        mProgress = findViewById(R.id.layout_music_play_bottom_progress);

        mPlayModeBtn = findViewById(R.id.layout_music_play_bottom_btn_play_mode);
        mPreSongBtn = findViewById(R.id.layout_music_play_bottom_btn_pre);
        mPlayPauseBtn = findViewById(R.id.layout_music_play_bottom_btn_play_or_pause);
        mNextSongBtn = findViewById(R.id.layout_music_play_bottom_btn_next);
        mMusicListBtn = findViewById(R.id.layout_music_play_bottom_btn_music_list);

        mAudioIndicatorView = findViewById(R.id.layout_music_play_audio_indicator_view);
    }

    /**
     * 初始化控件状态
     */
    private void initUIState(boolean firstIn) {
        ImageLoaderManager
                .newInstance()
                .loadImageForViewGroupWithFluzzy(mRootLayout, mAudioBean.getAlbumPic());

        mSongNameTxt.setText(mAudioBean.getName());
        mSongNameTxt.requestFocus();
        mSongSingerTxt.setText(mAudioBean.getSinger());
        mCurrentTimeTxt.setText("00:00");
        mEndTimeTxt.setText(mAudioBean.getTotalTime());
        mProgress.setProgress(0);
        mProgress.setEnabled(false);
        if (firstIn){
            if (AudioController.getInstance().isStartState()){
                onAudioEvent_ShowStartView(null);
            } else {
                onAudioEvent_ShowPauseView(null);
            }
        }
        changeFavorUI(isFavor(mAudioBean));
    }

    /**
     * 更新播放模式UI
     */
    private void updatePlayModeView(boolean showToast) {
        String toastMsg = "";
        switch (mPlayMode) {
            case LOOP:
                mPlayModeBtn.setImageResource(R.mipmap.player_loop);
                toastMsg = getResources().getString(R.string.audio_music_play_mode_loop);
                break;
            case RANDOM:
                mPlayModeBtn.setImageResource(R.mipmap.player_random);
                toastMsg = getResources().getString(R.string.audio_music_play_mode_random);
                break;
            case REPEAT:
                mPlayModeBtn.setImageResource(R.mipmap.player_once);
                toastMsg = getResources().getString(R.string.audio_music_play_mode_once);
                break;
        }
        if (showToast){
            Toast.makeText(MusicPlayActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 初始化控件监听器
     */
    private void initListener() {
        updatePlayModeView(false);

        mBackBtn.setOnClickListener(this);
        mShateBtn.setOnClickListener(this);

        mFavorBtn.setOnClickListener(this);
        mDownloadBtn.setOnClickListener(this);
        mRingBtn.setOnClickListener(this);
        mCommentBtn.setOnClickListener(this);

        mPlayModeBtn.setOnClickListener(this);
        mPreSongBtn.setOnClickListener(this);
        mPlayPauseBtn.setOnClickListener(this);
        mNextSongBtn.setOnClickListener(this);
        mMusicListBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_music_play_top_btn_back) {
            onBackPressed();
        } else if (v.getId() == R.id.layout_music_play_top_btn_share) {
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_favor) {
            AudioController.getInstance().changeFavourite();
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_download) {
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_ring) {
            mAudioIndicatorView.playPauseStylusAnimation(true);
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_comment) {
            mAudioIndicatorView.playPauseStylusAnimation(false);
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_play_mode) {
            changePlayMode();
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_pre) {
            AudioController.getInstance().preview();
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_play_or_pause) {
            AudioController.getInstance().switchPlayOrPause();
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_next) {
            AudioController.getInstance().next();
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_music_list) {
            showBottomListView();
        }
    }

    private void showBottomListView() {
        BottomListView listView = new BottomListView(MusicPlayActivity.this);
        listView.show();
    }

    /**
     * 改变播放模式
     */
    private void changePlayMode() {
        switch (mPlayMode) {
            case LOOP:
                AudioController.getInstance().setPlayMode(AudioController.PlayMode.RANDOM);
                break;
            case RANDOM:
                AudioController.getInstance().setPlayMode(AudioController.PlayMode.REPEAT);
                break;
            case REPEAT:
                AudioController.getInstance().setPlayMode(AudioController.PlayMode.LOOP);
                break;
        }
    }

    /**
     *  监听EventBus事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioEvent(AudioEvent event) {
        AudioEvent.Status code = event.eventCode;
        switch (code) {
            case EVENT_LOAD:
                onAudioEvent_ShowLoadView(event);
                break;
            case EVENT_START:
            case EVENT_RESUME:
                onAudioEvent_ShowStartView(event);
                break;
            case EVENT_PASUE:
                onAudioEvent_ShowPauseView(event);
                break;
            case EVENT_PLAY_MODE:
                onAudioEvent_PlayModeChanged(event.playMode);
                break;
            case EVENT_ADD_FAVOURITE:
                onAudioEvent_ShowFavouriteView(event, true);
                break;
            case EVENT_REMOVE_FAVOURITE:
                onAudioEvent_ShowFavouriteView(event, false);
                break;
            case EVENT_REMOVE_FROM_QUEUE:
                onAudioEvent_RemoveAuidoFromQueue(event);
                break;
        }
    }

    /**
     *  监听音乐加载事件
     * @param event
     */
    private void onAudioEvent_ShowLoadView(AudioEvent event) {
        initData();
        initUIState(false);
    }

    /**
     *  监听音乐开始播放事件
     * @param event
     */
    private void onAudioEvent_ShowStartView(AudioEvent event) {
        mPlayPauseBtn.setImageResource(R.mipmap.audio_aj6);
    }

    /**
     *  监听暂停事件
     * @param event
     */
    private void onAudioEvent_ShowPauseView(AudioEvent event) {
        mPlayPauseBtn.setImageResource(R.mipmap.audio_aj7);
    }

    /**
     *  监听收藏事件
     * @param event
     * @param addOrRemove
     */
    private void onAudioEvent_ShowFavouriteView(AudioEvent event, boolean addOrRemove) {
        changeFavorUI(addOrRemove);
    }

    public void changeFavorUI(boolean isFavourited){
        if (null == mFavorBtn){
            return;
        }
        mFavorBtn.setImageResource(isFavourited ? R.mipmap.audio_aeh : R.mipmap.audio_aef);
    }

    /**
     * 判断歌曲是否收藏了
     * @param bean
     */
    private boolean isFavor(AudioBean bean) {
        return GreenDaoHelper.getInstance().queryFavouriteAudioBean(bean) != null;
    }

    /**
     *  监听播放模式修改事件
     * @param playMode
     */
    private void onAudioEvent_PlayModeChanged(AudioController.PlayMode playMode) {
        if (null != playMode){
            mPlayMode = playMode;
            updatePlayModeView(true);
        }
    }

    /**
     *  监听音乐移除出列表事件
     * @param event
     */
    private void onAudioEvent_RemoveAuidoFromQueue(AudioEvent event) {
        initData();
    }
}
