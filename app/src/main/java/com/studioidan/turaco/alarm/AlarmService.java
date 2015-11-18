package com.studioidan.turaco.alarm;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.studioidan.turaco.App;
import com.studioidan.turaco.entities.Panel;
import com.studioidan.turaco.entities.PanelManager;
import com.studioidan.turaco.singeltones.DataStore;
import com.studioidan.turaco.singeltones.Factory;

/**
 * Created by PopApp_laptop on 04/11/2015.
 */
public class AlarmService extends Service {
    public static final String ACTION_ALERT = "action.alert";
    public static final String ACTION_DISALERT = "action.disalert";
    public static final String ACTION_PANEL = "action.panel";
    public static final String ACTION_START_REQUEST = "action.start.request";
    public static final String EXTRA_PANEL = "extra.panel";
    public final String TAG = getClass().getName();
    // private long INTERVAL = 5000;

    private Handler handler;
    private int counter = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Service Created!");
        super.onCreate();
        handler = new Handler();
        handler.post(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(new Intent(ACTION_START_REQUEST));

            PanelManager.getPanelStatus(App.getContext(), new Factory.GenericCallback() {
                @Override
                public void onDone(boolean success, Object result) {
                    if (success) {
                        Panel panel = (Panel) result;
                        LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(new Intent(ACTION_PANEL).putExtra(EXTRA_PANEL, panel));
                        //App.getContext().sendBroadcast(new Intent(ACTION_PANEL).putExtra(EXTRA_PANEL, panel));

                        //Alarm staff
                        //Log.d(TAG, "Panel Status call ended, alarm: " + panel.alarm);
                        if (!AlarmActivity.isAlarmShowing && panel.alarm) {
                            counter = 0;
                            Log.d(TAG, "Sending alert");
                            handler.removeCallbacks(runnable);
                            App.getContext().sendBroadcast(new Intent(ACTION_ALERT));
                        }
                        if (AlarmActivity.isAlarmShowing && panel.arm == PanelManager.PANEL_STATUS_DISARM) {
                            handler.removeCallbacks(runnable);
                            App.getContext().sendBroadcast(new Intent(ACTION_DISALERT));
                        }
                    }
                    handler.postDelayed(runnable, DataStore.getInstance().getApiInterval() * 1000);
                }
            });
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }
}
