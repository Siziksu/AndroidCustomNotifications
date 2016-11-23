package com.siziksu.notifications;

import android.app.Application;

import com.siziksu.notifications.common.Preferences;

public final class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Preferences.init(this);
    }
}