package com.sample.bugreport.app;

import android.app.Application;

import com.haowen.bugreport.CrashHandler;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }
}
