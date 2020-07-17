package com.ll.lib_audio.mediaplayer.view;

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

    /**
     * 初始化
     */
    private void init() {
        initData();

        initUI();
        initUIState();
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
    }

    /**
     * 初始化控件状态
     */
    private void initUIState() {
        ImageLoaderManager
                .newInstance()
                .loadImageForViewGroupWithFluzzy(mRootLayout, mAudioBean.getPlayBackground());

        mSongNameTxt.setText(mAudioBean.getName());
        mSongNameTxt.requestFocus();
        mSongSingerTxt.setText(mAudioBean.getSinger());
        mProgress.setProgress(0);
        mProgress.setEnabled(false);

        updatePlayModeView(false);
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
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_comment) {
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_play_mode) {
            changePlayMode();
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_pre) {
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_play_or_pause) {
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_next) {
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_music_list) {
        }
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
            case EVENT_PLAY_MODE:
                onAudioEvent_PlayModeChanged(event.playMode);
                break;
            case EVENT_ADD_FAVOURITE:
                onAudioEvent_ShowFavouriteView(event, true);
                break;
            case EVENT_REMOVE_FAVOURITE:
                onAudioEvent_ShowFavouriteView(event, false);
                break;
        }
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
     *  监听播放模式修改事件
     * @param playMode
     */
    private void onAudioEvent_PlayModeChanged(AudioController.PlayMode playMode) {
        if (null != playMode){
            mPlayMode = playMode;
            updatePlayModeView(true);
        }
    }
}
