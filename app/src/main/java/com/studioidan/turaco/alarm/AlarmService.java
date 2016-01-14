package com.studioidan.turaco.alarm;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.studioidan.turaco.App;
import com.studioidan.turaco.entities.PanelManager;
import com.studioidan.turaco.entities.PanelStatus;
import com.studioidan.turaco.singeltones.Factory;

/**
 * Created by PopApp_laptop on 04/11/2015.
 */
public class AlarmService extends Service {
    //public static final String ACTION_DISALERT = "action.disalert";
    public static final String ACTION_PANEL = "action.panel";
    public static final String ACTION_START_REQUEST = "action.start.request";
    public static final String EXTRA_PANEL = "extra.panel";
    public final String TAG = getClass().getName();

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

            PanelManager.getCurrentPanelStatus(App.getContext(), new Factory.GenericCallback() {
                @Override
                public void onDone(boolean success, Object result) {
                    if (success) {
                        PanelStatus ps = (PanelStatus) result;
                        LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(new Intent(ACTION_PANEL).putExtra(EXTRA_PANEL, ps));

                        //Alarm staff
                        //Log.d(TAG, "Panel Status call ended, alarm: " + panel.alarm);
                        if (!AlarmActivity.isAlarmShowing && ps.alarm) {
                            counter = 0;
                            Log.d(TAG, "Sending alert");
                            handler.removeCallbacks(runnable);
                            App.getContext().sendBroadcast(new Intent(AlarmReceiver.ACTION_ALERT));
                        }
                        if (AlarmActivity.isAlarmShowing && ps.arm == PanelManager.PANEL_STATUS_DISARM) {
                            handler.removeCallbacks(runnable);
                            App.getContext().sendBroadcast(new Intent(AlarmReceiver.ACTION_DISALERT));
                        }
                    }
                    handler.postDelayed(runnable, 3 * 1000);
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
