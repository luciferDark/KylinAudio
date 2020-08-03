package com.ll.lib_audio.mediaplayer.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ll.lib_audio.R;
import com.ll.lib_audio.mediaplayer.bean.AudioBean;
import com.ll.lib_audio.mediaplayer.core.AudioController;
import com.ll.lib_audio.mediaplayer.event.AudioEvent;
import com.ll.lib_image_loader.glide.app.ImageLoaderManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @Auther kylin
 * @Data 2020/6/7 - 16:40
 * @Package com.ll.lib_audio.mediaplayer.view
 * @Description
 */
public class BottomAudioView extends RelativeLayout {
    private Context mContext;

    /**
     * Views
     */
    private ImageView mAlbumImg;
    private TextView mMusicTitle;
    private TextView mMusicSingle;
    private ImageView mPlayOrPauseBtn;
    private ImageView mMusicList;

    /**
     * Beans
     */
    private AudioBean mAudioBean;

    /**
     * Animator
     */
    private ObjectAnimator mRotateAnimator;

    public BottomAudioView(Context context) {
        this(context, null);
    }

    public BottomAudioView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomAudioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        EventBus.getDefault().register(this);
        mContext = context;
        init(context, attrs, defStyleAttr);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.layout_bottom_audio_player, this);
        mAlbumImg = root.findViewById(R.id.bottom_audio_player_album_img);
        mMusicTitle = root.findViewById(R.id.bottom_audio_player_music_title);
        mMusicSingle = root.findViewById(R.id.bottom_audio_player_music_single);
        mPlayOrPauseBtn = root.findViewById(R.id.bottom_audio_player_start_or_pause);
        mMusicList = root.findViewById(R.id.bottom_audio_player_music_list);

        mRotateAnimator = ObjectAnimator.ofFloat(mAlbumImg, View.ROTATION.getName(), 0f, 360);
        mRotateAnimator.setDuration(2 * 1000);
        mRotateAnimator.setRepeatCount(Animation.INFINITE);
        mRotateAnimator.setInterpolator(new LinearInterpolator());
        if (AudioController.getInstance().isStartState()) {
            mRotateAnimator.start();
        } else {
            mRotateAnimator.pause();
        }

        root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpIntoMusicActivity();
            }
        });

        mPlayOrPauseBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPlayOrPause();
            }
        });

        mMusicList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickMusicList();
            }
        });
    }

    private void onClickPlayOrPause() {
        if (AudioController.getInstance().isIdleState() || AudioController.getInstance().isStopState()) {
            AudioController.getInstance().play();
        } else {
            AudioController.getInstance().switchPlayOrPause();
        }
    }

    private void onClickMusicList() {
        //todo 显示音乐播放列表
        if (null == mContext){
            return;
        }
        BottomListView listView = new BottomListView(mContext);
        listView.show();
    }

    private void jumpIntoMusicActivity() {
        //todo 跳转到音乐播放器
        MusicPlayActivity.startMusicActivity(mContext);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioEvent(AudioEvent event) {
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
        }
    }

    /**
     * 处理音频加载完成事件
     *
     * @param event
     */
    private void onAudioEvent_ShowLoadView(AudioEvent event) {
        this.mAudioBean = event.audioBean;
        if (null == this.mAudioBean) {
            return;
        }
        this.mMusicTitle.setText(this.mAudioBean.getName());
        this.mMusicSingle.setText(this.mAudioBean.getSinger());
        this.mPlayOrPauseBtn.setImageResource(R.mipmap.note_btn_pause_white);
        ImageLoaderManager.newInstance().loadImageForCircle(this.mAlbumImg, this.mAudioBean.getAlbumPic());
    }

    /**
     * 处理音频开始播放事件
     *
     * @param event
     */
    private void onAudioEvent_ShowStartView(AudioEvent event) {
        this.mPlayOrPauseBtn.setImageResource(R.mipmap.note_btn_play_white);
        if (this.mRotateAnimator.isPaused()) {
            this.mRotateAnimator.resume();
        } else {
            this.mRotateAnimator.start();
        }
    }

    /**
     * 处理音频暂停播放事件
     *
     * @param event
     */
    private void onAudioEvent_ShowPauseView(AudioEvent event) {
        this.mPlayOrPauseBtn.setImageResource(R.mipmap.note_btn_pause_white);
//        if (this.mRotateAnimator.isStarted() || this.mRotateAnimator.isRunning()) {
            this.mRotateAnimator.pause();
//        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
        if (null != mRotateAnimator) {
            mRotateAnimator.cancel();
            mRotateAnimator = null;
        }
    }
}
