package com.brianvega.tourmalineserviceexample;

import android.app.Application;
import android.content.Context;

import androidx.core.content.ContextCompat;

import com.brianvega.tourmalineserviceexample.models.Message;

import java.util.ArrayList;
import java.util.List;

public class MainApplication extends Application {

    public static Context context;
    public static MainApplication instance;
    public static List<Message> messages = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MainApplication.context = getApplicationContext();
        messages.add(new Message("Init Logs", ContextCompat.getColor(this, R.color.red)));
    }

    public static MainApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return MainApplication.context;
    }

}
