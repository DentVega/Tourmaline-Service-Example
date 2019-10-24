package com.brianvega.tourmalineserviceexample;

import android.app.Application;
import android.content.Context;

public class MainApplication extends Application {

    public static Context context;
    public static MainApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MainApplication.context = getApplicationContext();
    }

    public static MainApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return MainApplication.context;
    }

}
