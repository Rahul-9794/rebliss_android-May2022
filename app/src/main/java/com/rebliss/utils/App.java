package com.rebliss.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.StrictMode;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.rebliss.domain.constant.Constant;



public class App extends Application {
    public static Context context;
   public static  ImageLoaderConfiguration config;
   public static   DisplayImageOptions defaultOptions;
   public static   ImageLoader imageLoader;
   public static  Typeface LATO_BOLD, LATO_REGULAR, LATO_BLACK_ITALIC,
           LATO_SEMI_BOLD, LATO_SEMI_BOLD_ITALIC, ARIAL_REGULAR;



    @Override
    public void onCreate() {
        super.onCreate();
        context = getBaseContext();
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        initFont();

    }

    private void initFont(){

        LATO_BOLD = Typeface.createFromAsset(getApplicationContext().getAssets(), Constant.LATO_BOLD);
        LATO_REGULAR = Typeface.createFromAsset(getApplicationContext().getAssets(), Constant.LATO_REGULAR);
        LATO_BLACK_ITALIC = Typeface.createFromAsset(getApplicationContext().getAssets(), Constant.LATO_BLACK_ITALIC);
        LATO_SEMI_BOLD = Typeface.createFromAsset(getApplicationContext().getAssets(), Constant.LATO_SEMI_BOLD);
        LATO_SEMI_BOLD_ITALIC = Typeface.createFromAsset(getApplicationContext().getAssets(), Constant.LATO_SEMI_BOLD_ITALIC);
        ARIAL_REGULAR = Typeface.createFromAsset(getApplicationContext().getAssets(), Constant.ARIAL_REGULAR);

    }


}
