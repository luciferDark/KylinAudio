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

/**
 * @Auther Kylin
 * @Data 2020/6/16 - 10:12
 * @Package com.ll.lib_audio.mediaplayer.notification
 * @Description
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

    private void initNotifications() {
        if (null == mNotification) {
            //创建布局
            initLayout();
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

    private void initLayout() {
        int layoutBigId = R.layout.layout_notification_big;
        int layoutSmallId = R.layout.layout_notification_small;
        mBigNotificationView = new RemoteViews(mPackageName, layoutBigId);
        mSmallNotificationView = new RemoteViews(mPackageName, layoutSmallId);

        mBigNotificationView.setTextViewText(R.id.notification_big_audio_name, mAudioBean.getName());
        mBigNotificationView.setTextViewText(R.id.notification_big_audio_single, mAudioBean.getSinger());
    }
}
