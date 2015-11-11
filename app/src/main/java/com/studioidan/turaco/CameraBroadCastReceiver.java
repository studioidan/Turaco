package com.studioidan.turaco;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.studioidan.turaco.connection.APIErrorManager;
import com.studioidan.turaco.connection.SignalManager;
import com.studioidan.turaco.connection.manager.CamerasManager;


/**
 * Created by macbook on 9/19/15.
 */
public class CameraBroadCastReceiver extends BroadcastReceiver {
    Context mContext;
    @Override
    public void onReceive( Context context, Intent intent) {
        mContext=context;
        int mComingCameraPosition=intent.getIntExtra("CameraPosition",-1);
        CamerasManager.getSharedManager(context).requestCameraPictureUpdate(mComingCameraPosition).onError(new SignalManager.SignalErrorObserver() {
            @Override
            public void onError(SignalManager signal, APIErrorManager error) {
                //Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
