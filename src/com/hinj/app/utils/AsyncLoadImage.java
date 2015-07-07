
package com.hinj.app.utils;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.hinj.app.activity.R;
import com.hinj.app.bean.CacheApkIcon;
import com.hinj.app.bean.CacheImg;

public class AsyncLoadImage {
    private final String TAG = AsyncLoadImage.class.getSimpleName();
    private int CACHE_IMAGE_SIZE = 100;
    private ConcurrentLinkedQueue<CacheImg> cacheImgs;
    private ConcurrentLinkedQueue<CacheApkIcon> cacheApkIcons;
    private Handler handler;
    private boolean isAllowLoading = true;
    // dump object
    private Object lock = new Object();

    public AsyncLoadImage(Handler handler) {
        this.handler = handler;
        this.cacheImgs = new ConcurrentLinkedQueue<CacheImg>();
        this.cacheApkIcons = new ConcurrentLinkedQueue<CacheApkIcon>();
    }

    public void loadImage(ImageView imageView,String path) {
        for (CacheImg img : cacheImgs) {
            if (img.getPath().equals(path)) {
                imageView.setImageBitmap(img.getIcon());
                return;
            }
        }
        imageView.setImageResource(R.drawable.format_picture);
        new LoadImageThread(path, imageView).start();
    }

    public void loadApkIcon(Context context, ImageView imageView,String path) {
        for (CacheApkIcon icon : cacheApkIcons) {
            if (icon.getPath().equals(path)) {
                imageView.setImageDrawable(icon.getIcon());
                return;
            }
        }
        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon));
        new LoadApkIconThread(context, path, imageView).start();
    }

    class LoadImageThread extends Thread {
        private String path;
        private ImageView imageView;

        public LoadImageThread(String path, ImageView imageView) {
            this.path = path;
            this.imageView = imageView;
        }

        @Override
        public void run() {
            Log.v(TAG, "thread id = " + this.getId() + " isAllowLoading = " + isAllowLoading);
            if (!isAllowLoading) {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (Exception e) {
                        Log.v(TAG, "Exception happened, ex = " + e.getMessage());
                    }
                }
            }
            Log.v(TAG, "thread id = " + this.getId() + " isAllowLoading = " + isAllowLoading);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            final Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            CacheImg img = new CacheImg(path, bitmap);
            if (cacheImgs.size() >= CACHE_IMAGE_SIZE) {
                cacheImgs.poll();
            }
            cacheImgs.add(img);
            String tagPath = (String) imageView.getTag();
            if (tagPath.equals(path)) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        }

    }

    class LoadApkIconThread extends Thread {
        private Context context;
        private ImageView imageView;
        private String path;

        public LoadApkIconThread(Context context, String path, ImageView imageView) {
            this.context = context;
            this.path = path;
            this.imageView = imageView;
        }

        @Override
        public void run() {
            Log.v(TAG, "thread id = " + this.getId() + " isAllowLoading = " + isAllowLoading);
            if (!isAllowLoading) {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (Exception e) {
                        Log.v(TAG, "Exception happened, ex = " + e.getMessage());
                    }
                }
            }
            Log.v(TAG, "thread id = " + this.getId() + " loading apk icon");
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            final Drawable icon;
            if (null != packageInfo) {
                ApplicationInfo info = packageInfo.applicationInfo;
                info.publicSourceDir = path;
                icon = info.loadIcon(pm);
            } else {
                icon = context.getResources().getDrawable(R.drawable.icon);
            }

            CacheApkIcon apkIcon = new CacheApkIcon(path, icon);
            if (cacheApkIcons.size() >= CACHE_IMAGE_SIZE) {
                cacheApkIcons.poll();
            }
            cacheApkIcons.add(apkIcon);
            if (path.equals(imageView.getTag())) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        imageView.setImageDrawable(icon);
                    }
                });
            }
        }

    }

    public void lock() {
        this.isAllowLoading = false;
    }

    public void unlock() {
        this.isAllowLoading = true;
        synchronized (lock) {
            try {
                this.lock.notifyAll();
            } catch (Exception e) {
                Log.v(TAG, "Exception happened, ex = " + e.getMessage());
            }
        }
    }
}
