package me.t3sl4.upcortex;

import android.app.Application;

import com.yariksoffice.lingver.Lingver;

public class DefaultApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Lingver.init(this, "tr");
    }
}