package com.studioidan.turaco;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.studioidan.turaco.data.UserManager;
import com.studioidan.turaco.singeltones.DataStore;

import io.fabric.sdk.android.Fabric;
import pl.tajchert.nammu.Nammu;

/**
 * Created by PopApp_laptop on 26/10/2015.
 */
public class App extends Application {

    private static Context context;
    public static final String VERSION = "3.2";

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
        Nammu.init(getApplicationContext());
    }

    public static Context getContext() {
        return App.context;
    }

}
