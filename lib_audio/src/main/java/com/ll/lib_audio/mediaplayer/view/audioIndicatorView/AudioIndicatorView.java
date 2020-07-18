package com.ll.lib_audio.mediaplayer.view.audioIndicatorView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ll.lib_audio.R;
import com.ll.lib_audio.mediaplayer.bean.AudioBean;
import com.ll.lib_audio.mediaplayer.core.AudioController;
import com.ll.lib_audio.mediaplayer.event.AudioEvent;
import com.ll.lib_audio.mediaplayer.view.adpaters.AudioIndicatorViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @Auther Kylin
 * @Data 2020/7/17 - 19:23
 * @Package com.ll.lib_audio.mediaplayer.view.adpaters
 * @Description
 */
public class AudioIndicatorView extends RelativeLayout
        implements ViewPager.OnPageChangeListener,
        AudioIndicatorViewAdapter.AudioIndicatorViewAdapterCallback {
    private WeakReference<Context> mContextReference;
    //    UI
    private ImageView mAlbumImg;
    private ImageView mIndicatorStylus;
    private ViewPager mViewPager;
    private AudioIndicatorViewAdapter mAdapter;
    //    datas
    private AudioBean mAudioBean;
    private ArrayList<AudioBean> mAudioList;

    public AudioIndicatorView(Context context) {
        this(context, null);
    }

    public AudioIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public AudioIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContextReference = new WeakReference<>(context);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mAudioBean = AudioController.getInstance().getCurrentAudioBean();
        mAudioList = AudioController.getInstance().getQueue();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    /**
     * 初始化UI
     */
    private void initView() {
        if (null == mContextReference.get()) {
            throw new RuntimeException("AudioIndicatorView context is null");
        }
        View mViewRoot = LayoutInflater.from(mContextReference.get())
                .inflate(R.layout.layout_audio_indicator, null);

        mIndicatorStylus = mViewRoot.findViewById(R.id.layout_audio_indicator_stylus);
        mViewPager = mViewRoot.findViewById(R.id.layout_audio_indicator_viewpager);

        setViewPager();
    }

    /**
     * 配置ViewPager
     */
    private void setViewPager() {
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mAdapter = new AudioIndicatorViewAdapter(mContextReference.get(), mAudioList, this);
        mViewPager.setAdapter(mAdapter);

        showLoadingView(false);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //指定要播放的position
        AudioController.getInstance().setCurrrentIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                showPlayView();
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
                showPauseView();
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                break;
        }
    }

    /**
     * EventBus动画监听器
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioEvent(AudioEvent event){
        switch (event.eventCode){
            case EVENT_LOAD:
                this.mAudioBean = event.audioBean;
                showLoadingView(true);
                break;
            case EVENT_PASUE:
                showPauseView();
                break;
            case EVENT_START:
            case EVENT_RESUME:
                showPlayView();
                break;
        }

    }

    /**
     * 设置当前播放歌曲
     * @param isSmootScroll
     */
    private void showLoadingView(boolean isSmootScroll) {
        mViewPager.setCurrentItem(mAudioList.indexOf(mAudioBean), isSmootScroll);
    }

    /**
     * 显示旋转动画
     */
    private void showPlayView() {
        if (null == mAdapter) {
            return;
        }

        RotateAnimation animation = mAdapter.getItemAnimationByPosition(mViewPager.getCurrentItem());
        if (null != animation) {
            animation.start();
        }
    }

    /**
     * 暂停旋转动画
     */
    private void showPauseView() {
        if (null == mAdapter) {
            return;
        }

        RotateAnimation animation = mAdapter.getItemAnimationByPosition(mViewPager.getCurrentItem());
        if (null != animation) {
            animation.cancel();
        }
    }


    @Override
    public void onPlayStatus() {

    }

    @Override
    public void onPauseStatus() {

    }
}
