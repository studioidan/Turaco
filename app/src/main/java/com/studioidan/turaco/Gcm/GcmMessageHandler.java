package com.studioidan.turaco.Gcm;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.studioidan.turaco.App;
import com.studioidan.turaco.Fragments.MainFragment;
import com.studioidan.turaco.MainActivity;
import com.studioidan.turaco.R;
import com.studioidan.turaco.alarm.AlarmReceiver;
import com.studioidan.turaco.entities.Panel;
import com.studioidan.turaco.entities.PushLog;
import com.studioidan.turaco.singeltones.DataStore;

import org.json.JSONObject;

import java.util.Date;


public class GcmMessageHandler extends GcmListenerService {
    public final String TAG = getClass().getName();

    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        try {
            String strJson = data.getString("message");
            JSONObject message = new JSONObject(strJson).getJSONObject("message");
            int opCode = message.getInt("pushOpCode");
            /*store this pushLog */
            String date = DateFormat.format("dd/MM/yyyy ", new Date().getTime()).toString();
            PushLog pushLog = new PushLog(date, opCode);
            DataStore.getInstance().getLogs().add(pushLog);
            DataStore.getInstance().setLogs(DataStore.getInstance().getLogs());

            if (opCode == 0) {
                createNotification("got test message", "click to open");
                return;
            }

            String panelStatus = message.getString("panelStatus");
            Panel panel = new Gson().fromJson(panelStatus, Panel.class);

            if (opCode == 1 || opCode == 2 || opCode == 3) {
                Intent intent = new Intent();
                if (opCode == 1)
                    intent.setAction(MainFragment.ACTION_ARM);
                else if (opCode == 2)
                    intent.setAction(MainFragment.ACTION_ARM_STAY);
                else if (opCode == 3) {
                    intent.setAction(MainFragment.ACTION_DISARM);
                    /* also make sure the alert activity is of */
                    App.getContext().sendBroadcast(new Intent(AlarmReceiver.ACTION_DISALERT));
                }
                intent.putExtra(MainFragment.EXTRA_PANEL, panel);
                LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);
            } else if (opCode == 4) /*Alert*/ {
                App.getContext().sendBroadcast(new Intent(AlarmReceiver.ACTION_ALERT));
            }
            Log.d(TAG, "got push code:" + opCode);

            if (opCode != 4) {
                if (!App.getContext().getPackageName().equalsIgnoreCase(((ActivityManager) App.getContext().getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0).topActivity.getPackageName())) {
                    String mode = "arm";
                    if (opCode == 2) mode = "arm stayed";
                    if (opCode == 3) mode = "dis armed";
                /* App is not in the foreground*/
                    createNotification("System is " + mode, "Click here to see");
                }
            }

        } catch (Exception ex) {
            Log.e(TAG, "push parsing failed! - " + ex.getMessage());
            createNotification("got push", "Click here to see");
        }
    }

    // Creates notification based on title and body received
    private void createNotification(String title, String body) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // intent triggered, you can add other intent for other actions
        Intent intent = new Intent(App.getContext(), MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(App.getContext(), 0, intent, 0);

        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.turcaosmalltransparent)
                .setContentTitle(title)
                .setContentText(body)
                .setLights(Color.parseColor("#49a0d6"), 1000, 1000)
                .setContentIntent(pIntent)
                .setAutoCancel(false)
                .setSound(soundUri);

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }

}