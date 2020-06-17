package com.ll.lib_audio.mediaplayer.view.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.ll.lib_audio.R;
import com.ll.lib_audio.mediaplayer.app.AudioHelper;
import com.ll.lib_audio.mediaplayer.bean.AudioBean;
import com.ll.lib_audio.mediaplayer.core.AudioService;
import com.ll.lib_image_loader.glide.app.ImageLoaderManager;

/**
 * @Auther Kylin
 * @Data 2020/6/16 - 10:12
 * @Package com.ll.lib_audio.mediaplayer.notification
 * @Description 1：notification的创建和大小view的初始化
 *                          2：提供对外更新Notification的方法
 */
public class NotificationHelper {
    //------------------单例----------------------//
    private static NotificationHelper mInstance = null;

    private NotificationHelper() {
    }

    public static NotificationHelper getInstance() {
        if (null == mInstance) {
            synchronized (NotificationHelper.class) {
                if (null == mInstance) {
                    mInstance = new NotificationHelper();
                }
            }
        }
        return mInstance;
    }
    //------------------单例----------------------//

    private static final String CHANNER_ID = "channer_id_audio_notification";
    private static final String CHANNER_NAME = "channer_name_audio_notification";
    public static final int NOTIFICATION_ID = 0x0111;

    private String mPackageName;
    private Notification mNotification;
    private NotificationManager mNotificationManager;

    private RemoteViews mBigNotificationView;
    private RemoteViews mSmallNotificationView;

    private AudioBean mAudioBean;
    private NotificationHelperListener mNotificationHelperListener;

    public interface NotificationHelperListener {
        void onNotificationInit();
    }

    public void init(NotificationHelperListener listener) {
        mNotificationManager = (NotificationManager) AudioHelper.getInstance()
                .getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mPackageName = AudioHelper.getInstance().getContext().getPackageName();
        mNotificationHelperListener = listener;

        initNotifications();
        if (null != mNotificationHelperListener) {
            mNotificationHelperListener.onNotificationInit();
        }
    }

    /**
     * 初始化Notification
     */
    private void initNotifications() {
        if (null == mNotification) {
            //创建布局
            initUILayout();
            initListener();
            // 创建通知
            Intent intent = new Intent(AudioHelper.getInstance().getContext(), null);//todo add Intent
            PendingIntent pendingIntent = PendingIntent.getActivity(AudioHelper.getInstance().getContext()
                    , 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 安卓8.0
                NotificationChannel channel = new NotificationChannel(CHANNER_ID, CHANNER_NAME, NotificationManager.IMPORTANCE_HIGH);
                // channel.enableLights(true);
                channel.enableVibration(true);
                mNotificationManager.createNotificationChannel(channel);
            } else {

            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(AudioHelper.getInstance().getContext(), CHANNER_ID)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContent(mSmallNotificationView)
                    .setCustomBigContentView(mBigNotificationView);
            mNotification = builder.build();
        }

    }

    /**
     * 初始化UI布局
     */
    private void initUILayout() {
        int layoutBigId = R.layout.layout_notification_big;
        int layoutSmallId = R.layout.layout_notification_small;

        mBigNotificationView = new RemoteViews(mPackageName, layoutBigId);
        mBigNotificationView.setImageViewResource(R.id.notification_big_music_play, R.mipmap.note_btn_play_white);
        mBigNotificationView.setImageViewResource(R.id.notification_big_music_next, R.mipmap.note_btn_next_white);
        mBigNotificationView.setImageViewResource(R.id.notification_big_music_prev, R.mipmap.note_btn_pre_white);
        mBigNotificationView.setImageViewResource(R.id.notification_big_music_favor, isFavor() ? R.mipmap.note_btn_loved : R.mipmap.note_btn_love_white);

        mSmallNotificationView = new RemoteViews(mPackageName, layoutSmallId);
        mSmallNotificationView.setImageViewResource(R.id.notification_small_music_play, R.mipmap.note_btn_play_white);
        mSmallNotificationView.setImageViewResource(R.id.notification_small_music_next, R.mipmap.note_btn_next_white);

        updateUIViewByAudiobean(mAudioBean);
    }

    /**
     * 初始化按钮监听器
     */
    private void initListener() {
        PendingIntent pendingPlayPause = createNotificationPendingIntent(AudioService.AudioBroadcastReceiver.REQUEST_CODE_PLAY_PAUSE,
                AudioService.AudioBroadcastReceiver.EXTRA_PLAY_PAUSE);
        PendingIntent pendingPrev = createNotificationPendingIntent(AudioService.AudioBroadcastReceiver.REQUEST_CODE_PREV,
                AudioService.AudioBroadcastReceiver.EXTRA_PREV);
        PendingIntent pendingNext = createNotificationPendingIntent(AudioService.AudioBroadcastReceiver.REQUEST_CODE_NEXT,
                AudioService.AudioBroadcastReceiver.EXTRA_NEXT);
        PendingIntent pendingFavor = createNotificationPendingIntent(AudioService.AudioBroadcastReceiver.REQUEST_CODE_FAVOR,
                AudioService.AudioBroadcastReceiver.EXTRA_FAVOR);

        mBigNotificationView.setOnClickPendingIntent(R.id.notification_big_music_play, pendingPlayPause);
        mBigNotificationView.setOnClickPendingIntent(R.id.notification_big_music_prev, pendingPrev);
        mBigNotificationView.setOnClickPendingIntent(R.id.notification_big_music_next, pendingNext);
        mBigNotificationView.setOnClickPendingIntent(R.id.notification_big_music_favor, pendingFavor);

        mSmallNotificationView.setOnClickPendingIntent(R.id.notification_small_music_play, pendingPlayPause);
        mSmallNotificationView.setOnClickPendingIntent(R.id.notification_small_music_next, pendingNext);
    }

    /**
     * 创建PendingIntent
     *
     * @param requestCode
     * @param extraValue
     * @return
     */
    private PendingIntent createNotificationPendingIntent(int requestCode, String extraValue) {
        Intent intent = new Intent();
        intent.setAction(AudioService.AudioBroadcastReceiver.ACTION_AUDIO_BROADCAST);
        intent.putExtra(AudioService.AudioBroadcastReceiver.EXTRA, extraValue);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(AudioHelper.getInstance().getContext(),
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    private boolean isFavor() {
        return false;
    }

    /**
     * 更新为加载状态
     */
    public void showLoadStatus(AudioBean bean) {
        if (null == bean || null == mBigNotificationView || null == mSmallNotificationView) {
            return;
        }
        mAudioBean = bean;
        mBigNotificationView.setImageViewResource(R.id.notification_big_music_play, R.mipmap.note_btn_pause_white);
        mSmallNotificationView.setImageViewResource(R.id.notification_small_music_play, R.mipmap.note_btn_pause_white);

        updateUIViewByAudiobean(mAudioBean);
        //todo 收藏状态的处理

        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    /**
     * 更新为播放状态
     */
    public void showPlayStatus() {
        if (null == mBigNotificationView || null == mSmallNotificationView) {
            return;
        }
        mBigNotificationView.setImageViewResource(R.id.notification_big_music_play, R.mipmap.note_btn_play_white);
        mSmallNotificationView.setImageViewResource(R.id.notification_small_music_play, R.mipmap.note_btn_play_white);
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    /**
     * 更新为暂停状态
     */
    public void showPauseStatus() {
        if (null == mBigNotificationView || null == mSmallNotificationView) {
            return;
        }
        mBigNotificationView.setImageViewResource(R.id.notification_big_music_play, R.mipmap.note_btn_pause_white);
        mSmallNotificationView.setImageViewResource(R.id.notification_small_music_play, R.mipmap.note_btn_pause_white);
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    /**
     * 更新收藏状态
     */
    public void showFavorStatus() {

    }

    /**
     * 更新界面显示，歌曲名称、歌手、专辑icon
     *
     * @param audioBean
     */
    private void updateUIViewByAudiobean(AudioBean audioBean) {
        if (null != audioBean) {
            Context mContext = AudioHelper.getInstance().getContext();
            mBigNotificationView.setTextViewText(R.id.notification_big_audio_name, audioBean.getName());
            mBigNotificationView.setTextViewText(R.id.notification_big_audio_single, audioBean.getSinger());
            ImageLoaderManager.newInstance().loadImageForNotification(mContext, R.id.notification_big_album_img,
                    mBigNotificationView, mNotification, NOTIFICATION_ID, audioBean.getAlbumPic());

            mSmallNotificationView.setTextViewText(R.id.notification_small_audio_name, audioBean.getName());
            mSmallNotificationView.setTextViewText(R.id.notification_small_audio_single, audioBean.getSinger());
            ImageLoaderManager.newInstance().loadImageForNotification(mContext, R.id.notification_small_album_img,
                    mSmallNotificationView, mNotification, NOTIFICATION_ID, audioBean.getAlbumPic());
        }
    }

    public Notification getNotification() {
        return mNotification;
    }
}