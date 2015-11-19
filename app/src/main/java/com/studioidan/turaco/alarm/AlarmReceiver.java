package com.studioidan.turaco.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by PopApp_laptop on 04/11/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public final String TAG = getClass().getName();

    public static final String ACTION_ALERT = "action.alert";
    public static final String ACTION_DISALERT = "action.disalert";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_ALERT.equals(intent.getAction())) {
            Log.d(TAG, "Got alarm");
            Intent myIntent = new Intent(context, AlarmActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }else if(ACTION_DISALERT.equals(intent.getAction())){
            /*here we need to make sure alert activity is down*/


        }
    }
}
