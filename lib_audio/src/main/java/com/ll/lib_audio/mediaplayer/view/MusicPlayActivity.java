package com.ll.lib_audio.mediaplayer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ll.lib_audio.R;

/**
 * @Auther Kylin
 * @Data 2020/7/15 - 16:59
 * @Package com.ll.lib_audio.mediaplayer.view
 * @Description
 */
public class MusicPlayActivity extends FragmentActivity
        implements View.OnClickListener {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_music_play_acitivity);
        init();
    }

    private void init() {
        initUI();
        initListener();
    }

    private void initUI() {
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
        } else if (v.getId() == R.id.layout_music_play_top_btn_share) {
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_favor) {
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_download) {
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_ring) {
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_comment) {
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_play_mode) {
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_pre) {
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_play_or_pause) {
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_next) {
        } else if (v.getId() == R.id.layout_music_play_bottom_btn_music_list) {
        }
    }
}
