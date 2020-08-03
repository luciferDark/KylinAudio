package com.ll.lib_audio.mediaplayer.view;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ll.lib_audio.R;
import com.ll.lib_audio.mediaplayer.bean.AudioBean;
import com.ll.lib_audio.mediaplayer.core.AudioController;
import com.ll.lib_audio.mediaplayer.event.AudioEvent;
import com.ll.lib_audio.mediaplayer.greendao.GreenDaoHelper;
import com.ll.lib_audio.mediaplayer.view.adpaters.BottomListRecycleViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * @Auther kylin
 * @Data 2020/8/1 - 22:40
 * @Package com.ll.lib_audio.mediaplayer.view
 * @Description 底部播放列表弹窗
 */
public class BottomListView extends BottomSheetDialog implements View.OnClickListener {
    private Context mContext;
    private DisplayMetrics mDM;

    //    UI
    private TextView mListCount;
    private ImageView mPlayModeImg;
    private TextView mPlayModeDesc;

    private LinearLayout mFavorAll;
    private ImageView mDeletAll;

    private RecyclerView mAudioList;
    private TextView mClose;

    //    data
    private AudioBean mCurrentAudio;
    private ArrayList<AudioBean> mCurrentList;
    private AudioController.PlayMode mPlayMode;

    private BottomListRecycleViewAdapter mAdapter;

    public BottomListView(@NonNull Context context) {
        this(context, 0);
    }

    public BottomListView(@NonNull Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mDM = context.getResources().getDisplayMetrics();
    }

    protected BottomListView(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bottom_list_view);
        initData();
        initUI();
        initUIState();
    }

    private void initData() {
        this.mCurrentAudio = AudioController.getInstance().getCurrentAudioBean();
        this.mCurrentList = AudioController.getInstance().getQueue();
        this.mPlayMode = AudioController.getInstance().getPlayMode();
    }

    private void initUI() {
        mListCount = findViewById(R.id.bottom_list_view_list_count);
        mPlayModeImg = findViewById(R.id.bottom_list_view_play_mode);
        mPlayModeDesc = findViewById(R.id.bottom_list_view_play_mode_desc);

        mFavorAll = findViewById(R.id.bottom_list_view_favor_all);

        mDeletAll = findViewById(R.id.bottom_list_view_delete_list);
        mAudioList = findViewById(R.id.bottom_list_view_recycle_view);

        mClose = findViewById(R.id.bottom_list_view_btn_close);

        mPlayModeImg.setOnClickListener(this);
        mPlayModeDesc.setOnClickListener(this);
        mFavorAll.setOnClickListener(this);
        mDeletAll.setOnClickListener(this);
        mClose.setOnClickListener(this);

        /**
         * 充满宽度，也要以在style中定义
         */
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = mDM.widthPixels; //设置宽度
        dialogWindow.setAttributes(lp);

        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    private void initUIState() {
        mListCount.setText("(" + mCurrentList.size() + ")");

        updatePlayMode(mPlayMode);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mAudioList.setLayoutManager(linearLayoutManager);

        mAdapter = new BottomListRecycleViewAdapter(mCurrentList,mCurrentAudio,mContext);
        mAudioList.setAdapter(mAdapter);
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
     * 更新播放模式
     *
     * @param mPlayMode
     */
    private void updatePlayMode(AudioController.PlayMode mPlayMode) {
        String showText = "";
        switch (mPlayMode) {
            case LOOP:
                mPlayModeImg.setImageResource(R.mipmap.player_loop);
                showText = mContext.getResources().getString(R.string.audio_music_play_mode_loop);
                break;
            case RANDOM:
                mPlayModeImg.setImageResource(R.mipmap.player_random);
                showText = mContext.getResources().getString(R.string.audio_music_play_mode_random);
                break;
            case REPEAT:
                mPlayModeImg.setImageResource(R.mipmap.player_once);
                showText = mContext.getResources().getString(R.string.audio_music_play_mode_once);
                break;
        }
        mPlayModeDesc.setText(showText);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bottom_list_view_play_mode
                || v.getId() == R.id.bottom_list_view_play_mode_desc) {
            changePlayMode();
        } else if (v.getId() == R.id.bottom_list_view_favor_all) {
            favorAll();
        } else if (v.getId() == R.id.bottom_list_view_delete_list) {

        } else if (v.getId() == R.id.bottom_list_view_btn_close) {
            this.cancel();
        }
    }

    /**
     * 添加列表歌曲收藏
     */
    private void favorAll() {
        if (null == mCurrentList) {
            return;
        }
        for (AudioBean bean : mCurrentList) {
            if (GreenDaoHelper.getInstance().queryFavouriteAudioBean(bean) != null){
                GreenDaoHelper.getInstance().addFavouriteAudioBean(bean);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioEvent(AudioEvent event) {
        switch (event.eventCode) {
            case EVENT_LOAD:
                onAudioEvent_loadEvent(event);
                break;
            case EVENT_START:
            case EVENT_RESUME:
                onAudioEvent_startEvent(event);
                break;
            case EVENT_PASUE:
                onAudioEvent_pauseEvent(event);
                break;
            case EVENT_PLAY_MODE:
                onAudioEvent_playModeEvent(event);
                break;
        }
    }

    /**
     * 处理监听事件-加载完成播放
     */
    private void onAudioEvent_loadEvent(AudioEvent event) {
        if (null == mAdapter){
            return;
        }
        mAdapter.updateCurrentAudio(event.audioBean);
    }

    /**
     * 处理监听事件-开始播放
     */
    private void onAudioEvent_startEvent(AudioEvent event) {
    }

    /**
     * 处理监听事件-暂停播放
     */
    private void onAudioEvent_pauseEvent(AudioEvent event) {
    }

    /**
     * 处理监听事件-播放模式
     */
    private void onAudioEvent_playModeEvent(AudioEvent event) {
        updatePlayMode(event.playMode);
    }

}
