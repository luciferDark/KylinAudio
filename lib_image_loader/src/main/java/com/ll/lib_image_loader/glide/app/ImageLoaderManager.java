package com.ll.lib_image_loader.glide.app;


import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.ll.lib_image_loader.R;
import com.ll.lib_image_loader.util.ImageUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 图片加载封装接口类
 *
 * @author kylin
 * @date 2020/4/15
 */
public class ImageLoaderManager {
    //------------------单例----------------------//
    private static ImageLoaderManager mInstance;

    private ImageLoaderManager() {
    }

    public static ImageLoaderManager newInstance() {
        if (null == mInstance) {
            synchronized (ImageLoaderManager.class) {
                if (null == mInstance) {
                    mInstance = new ImageLoaderManager();
                }
            }
        }
        return mInstance;
    }
    //------------------单例----------------------//

    /**
     * 为ImageView加载图片
     *
     * @param view
     * @param url
     */
    public void loadImageForView(final ImageView view, String url) {
        loadImageForView(view, url, null);
    }

    /**
     * 为ImageView加载图片
     *
     * @param imageView
     * @param url
     * @param options
     */
    public void loadImageForView(final ImageView imageView, String url, RequestOptions options) {
        /**  if (TextUtils.isEmpty(url) || null == view) {
         return;
         }
         RequestOptions mOptions;
         if (null == options) {
         mOptions = initCommonRequestOption();
         } else {
         mOptions = options;
         }
         Glide.with(view.getContext())
         .asBitmap()
         .load(url)
         .apply(mOptions)
         .transition(BitmapTransitionOptions.withCrossFade())//图片加载显示效果
         .into(view);*/

        loadImageForTarget(imageView.getContext(),
                new ImageViewTarget<Drawable>(imageView) {
                    @Override
                    protected void setResource(@Nullable Drawable resource) {
                        if (null != imageView && null != resource) {
                            imageView.setImageDrawable(resource);
                        }
                    }
                },
                url,
                options);
    }

    /**
     * 加载一张圆形图片
     *
     * @param view
     * @param url
     */
    public void loadImageForCircle(final ImageView view, String url) {
        loadImageForView(view, url, null);
    }

    /**
     * 加载一张圆形图片
     *
     * @param view
     * @param url
     * @param options
     */
    public void loadImageForCircle(final ImageView view, String url, RequestOptions options) {
        loadImageForTarget(view.getContext(),
                new BitmapImageViewTarget(view) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable mDrawable = RoundedBitmapDrawableFactory
                                .create(view.getResources(), resource);
                        mDrawable.setCircular(true);
                        view.setImageDrawable(mDrawable);
                    }
                },
                url,
                options);
    }

    /**
     * 为viewGroup设置背景图并且进行毛玻璃模糊处理
     *
     * @param view
     * @param url
     */
    public void loadImageForViewGroupWithFluzzy(final ViewGroup view, String url) {
        loadImageForViewGroup(view, url, true);
    }

    /**
     * 为viewGroup设置背景图并且进行毛玻璃模糊处理
     *
     * @param view
     * @param url
     */
    public void loadImageForViewGroupWithoutFluzzy(final ViewGroup view, String url) {
        loadImageForViewGroup(view, url, false);
    }

    /**
     * 为viewGroup设置背景图并且进行毛玻璃模糊处理
     *
     * @param view
     * @param url
     * @param needFluzzy
     */
    public void loadImageForViewGroup(final ViewGroup view, String url, final boolean needFluzzy) {
        loadImageForViewGroup(view, url, null, needFluzzy);
    }

    /**
     * 为viewGroup设置背景图并且进行毛玻璃模糊处理
     *
     * @param view
     * @param url
     * @param options
     * @param needFluzzy
     */
    public void loadImageForViewGroup(final ViewGroup view, String url, RequestOptions options, final boolean needFluzzy) {
        loadImageForTarget(view.getContext(),
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull final Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        //使用Rxjava异步调用转换一下bitmap到drawable并设置为viewgroup的背景
                        Observable
                                .just(resource)
                                .map(new Function<Bitmap, Drawable>() {
                                    @Override
                                    public Drawable apply(Bitmap bitmap) throws Exception {
                                        Drawable drawable = new BitmapDrawable(
                                                needFluzzy ? ImageUtil.doBlur(bitmap, 100, true) : bitmap);
                                        return drawable;
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Drawable>() {
                                    @Override
                                    public void accept(Drawable drawable) throws Exception {
                                        if (null != drawable) {
                                            view.setBackground(drawable);
                                        }
                                    }
                                });
                    }
                },
                url,
                options);
    }

    /**
     *  为Notification加载图片
     * @param context
     * @param targetId
     * @param mRemoteView
     * @param notification
     * @param notificationId
     * @param url
     */
    public void loadImageForNotification(final Context context, int targetId, RemoteViews mRemoteView,
                                         Notification notification, int notificationId, String url) {
        loadImageForNotification(context, targetId, mRemoteView, notification, notificationId, url, null);
    }
    /**
     *  为Notification加载图片
     * @param context
     * @param targetId
     * @param mRemoteView
     * @param notification
     * @param notificationId
     * @param url
     * @param options
     */
    public void loadImageForNotification(final Context context, int targetId, RemoteViews mRemoteView,
                                         Notification notification, int notificationId, String url, RequestOptions options) {
        loadImageForTarget(context,
                createNotificationTarget(context, targetId, mRemoteView, notification, notificationId),
                url,
                options);
    }

    /**
     *  创建NotificationTarget
     * @param context
     * @param targetId
     * @param mRemoteView
     * @param notification
     * @param notificationId
     * @return
     */
    private Target createNotificationTarget(Context context, int targetId, RemoteViews mRemoteView,
                                            Notification notification, int notificationId) {
        NotificationTarget mTarget = new NotificationTarget(context, targetId, mRemoteView, notification, notificationId);
        return mTarget;
    }

    /**
     * 初始化默认的request option配置
     *
     * @return
     */
    private RequestOptions initCommonRequestOption() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.icon_img_load_place_holder)
                .error(R.drawable.icon_img_load_error)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)//磁盘缓存策略
                .skipMemoryCache(false)//使用内存缓存
                .priority(Priority.NORMAL);//线程调度优先级
        return options;
    }

    /**
     * 内部通用glide加载图片到target宿主
     *
     * @param context
     * @param target
     * @param url
     * @param options
     */
    private void loadImageForTarget(Context context, Target target, String url, RequestOptions options) {
        if (TextUtils.isEmpty(url) || null == target) {
            return;
        }
        RequestOptions mOptions;
        if (null == options) {
            mOptions = initCommonRequestOption();
        } else {
            mOptions = options;
        }

        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(mOptions)
                .transition(BitmapTransitionOptions.withCrossFade())//图片加载显示效果
                .into(target);
    }
}
