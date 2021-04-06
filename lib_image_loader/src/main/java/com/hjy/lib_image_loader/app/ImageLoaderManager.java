package com.hjy.lib_image_loader.app;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.hjy.lib_image_loader.R;
import com.hjy.lib_image_loader.utils.BlurUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 图片加载类，与外界的唯一通信类，
 * 支持为各种view，notification，appwidget,viewgroup加载图片
 * Created by hjy on 2019/12/18
 */
public class ImageLoaderManager {

    private ImageLoaderManager() {
    }

    /**
     * 内部类构造的单例方式
     */
    private static class SingletonHolder {
        private static ImageLoaderManager instance = new ImageLoaderManager();
    }

    public static ImageLoaderManager getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 为ImageView加载图片
     *
     * @param url
     * @param imageView
     */
    public void displayImageForView(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .transition(BitmapTransitionOptions.withCrossFade())//过渡效果
                .into(imageView);
    }

    /**
     * 为ImageView加载圆形图片
     *
     * @param url
     * @param imageView
     */
    public void displayImageForCircle(String url, final ImageView imageView) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .into(new BitmapImageViewTarget(imageView) {
                    //将imageview包装成target
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                                .create(imageView.getResources(), resource);
                        drawable.setCircular(true);
                        imageView.setImageDrawable(drawable);
                    }
                });
    }

    /**
     * 为viewgroup设置背景并模糊处理
     *
     * @param url
     * @param viewGroup
     */
    public void displayImageForViewGroup(String url, final ViewGroup viewGroup, final boolean isGSBlur) {
        Glide.with(viewGroup.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull final Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Observable.just(resource).map(new Function<Bitmap, Drawable>() {
                            @Override
                            public Drawable apply(Bitmap bitmap) throws Exception {
                                Bitmap mBitmap = resource;
                                //将bitmap进行模糊处理并转化为drawable
                                if (isGSBlur) {
                                    mBitmap = BlurUtil.doBlur(resource, 100, true);
                                }
                                Drawable drawable = new BitmapDrawable(mBitmap);
                                return drawable;
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Drawable>() {
                                    @Override
                                    public void accept(Drawable drawable) throws Exception {
                                        viewGroup.setBackground(drawable);
                                    }
                                });
                    }
                });
    }

    /**
     * 为Notification中的id控件加载图片
     *
     * @param context        上下文
     * @param url            图片路径
     * @param id             要加载图片的控件id
     * @param rv             Notification的布局
     * @param notification   notification对象
     * @param notificationId notification唯一对应的id，通过这个id可以由NotificationManage找到这个notification
     */
    public void displayImageForNotification(Context context, String url, int id,
                                            RemoteViews rv, Notification notification,
                                            int notificationId) {
        this.displayImageForTarget(context, url, initNotificationTarget(context, id, rv,
                notification, notificationId));
    }

    /**
     * 构造一个Notification  Target
     *
     * @param context
     * @param id
     * @param rv
     * @param notification
     * @param notificationId
     * @return
     */
    private NotificationTarget initNotificationTarget(Context context, int id,
                                                      RemoteViews rv, Notification notification,
                                                      int notificationId) {

        NotificationTarget target = new NotificationTarget(context, id, rv, notification, notificationId);
        return target;
    }

    /**
     * 为非view加载图片
     *
     * @param context
     * @param url
     * @param target
     */
    private void displayImageForTarget(Context context, String url, Target target) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .transition(BitmapTransitionOptions.withCrossFade())
                .fitCenter()
                .into(target);
    }

    /**
     * 加载图片配置
     *
     * @return
     */
    private RequestOptions initCommonRequestOption() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.b4y)
                .error(R.mipmap.b4y)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)//缓存策略
                .skipMemoryCache(false)//表示使用内存缓存
                .priority(Priority.NORMAL);//图片下载线程的优先级
        return options;
    }
}
