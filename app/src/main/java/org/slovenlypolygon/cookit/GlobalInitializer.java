package org.slovenlypolygon.cookit;

import android.app.Application;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class GlobalInitializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Picasso.setSingletonInstance(new Picasso.Builder(this).downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE)).build());
    }
}
