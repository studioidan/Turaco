package com.studioidan.turaco;

import android.app.Application;
import android.content.Context;

import com.studioidan.turaco.data.UserManager;
import com.studioidan.turaco.singeltones.DataStore;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by PopApp_laptop on 26/10/2015.
 */
public class App extends Application {

    public String BaseUrl = "";
    private static Context context;
    public  static final String VERSION = "1.7";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        init();
    }

    private void init() {
        App.context = getApplicationContext();
        DataStore.getInstance();
        UserManager.getInstance();
    }

    public static Context getContext() {
        return App.context;
    }

}
