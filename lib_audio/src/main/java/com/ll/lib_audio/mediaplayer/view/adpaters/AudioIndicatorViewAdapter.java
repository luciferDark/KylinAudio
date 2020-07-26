package com.ll.lib_audio.mediaplayer.view.adpaters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.ll.lib_audio.R;
import com.ll.lib_audio.mediaplayer.bean.AudioBean;
import com.ll.lib_audio.mediaplayer.core.AudioController;
import com.ll.lib_image_loader.glide.app.ImageLoaderManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @Auther Kylin
 * @Data 2020/7/18  - 19:23
 * @Package com.ll.lib_audio.mediaplayer.view.adpaters
 * @Description
 */
public class AudioIndicatorViewAdapter extends PagerAdapter {
    private final String TAG = "IndicatorViewAdapter";
    private ArrayList<AudioBean> mAudioList;
    private WeakReference<Context> mContextReference;
    private AudioIndicatorViewAdapterCallback mCallback;

    private SparseArray<RotateAnimation> mAnimations;

    public AudioIndicatorViewAdapter(Context context,
                                     ArrayList<AudioBean> audioList,
                                     AudioIndicatorViewAdapterCallback callback) {
        this.mAudioList = audioList;
        this.mContextReference = new WeakReference<>(context);
        this.mCallback = callback;

        this.mAnimations = new SparseArray<>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View mViewRoot = LayoutInflater.from(mContextReference.get()).inflate(R.layout.layout_audio_indicator_item, null);
        ImageView mAlbumImg = mViewRoot.findViewById(R.id.layout_audio_indicator_album_img);
        container.addView(mViewRoot);
        ImageLoaderManager.newInstance().loadImageForCircle(mAlbumImg, mAudioList.get(position).getAlbumPic());
        createItemAnimation(mViewRoot, position);
        return mViewRoot;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        int count =  null == mAudioList ? 0 : mAudioList.size();
        return count;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    /**
     * 为Item创建旋转动画，并保存起来
     * @param view
     * @return
     */
    public RotateAnimation createItemAnimation(View view, int position){
        RotateAnimation mAnimation = new RotateAnimation(0, 360
                ,Animation.RELATIVE_TO_SELF, 0.5f
                ,Animation.RELATIVE_TO_SELF, 0.5f);
        mAnimation.setDuration(4000);
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setFillAfter(true);
//        mAnimation.setFillEnabled(true);
//        mAnimation.setFillBefore(true);
        mAnimation.setInterpolator(new LinearInterpolator());
        view.setAnimation(mAnimation);

        if (null != mAnimations){
            mAnimations.put(position, mAnimation);
        }
        if (mAudioList.get(position) == AudioController.getInstance().getCurrentAudioBean()){
            //当前播放歌曲,且是播放状态,则开始动画
            if (AudioController.getInstance().isStartState()){
                mAnimation.start();
            }
        }
        return mAnimation;
    }

    /**
     * 对外提供接口获取对应item动画
     * @param position
     * @return
     */
    public RotateAnimation getItemAnimationByPosition(int position){
        return null == mAnimations? null : mAnimations.get(position);
    }

    public interface AudioIndicatorViewAdapterCallback {
        void onPlayStatus();

        void onPauseStatus();
    }
}
