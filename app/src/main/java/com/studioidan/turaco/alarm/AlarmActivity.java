package com.studioidan.turaco.alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.studioidan.turaco.App;
import com.studioidan.turaco.CustomView.CustomVideoView;
import com.studioidan.turaco.Fragments.MainFragment;
import com.studioidan.turaco.R;
import com.studioidan.turaco.entities.PanelManager;
import com.studioidan.turaco.singeltones.DataStore;
import com.studioidan.turaco.singeltones.Factory;


/**
 * Created by macbook on 9/27/15.
 */
public class AlarmActivity extends Activity implements View.OnClickListener {
    public static final String ACTION_DISALERT = "action.disalert";

    CustomVideoView mVideoView;
    Button btnCloseAndDisarm, btnClose;
    public static boolean isAlarmShowing = false;

    ProgressDialog mProgess;
    MediaPlayer mp;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.panel_alarm_layout);

        init();
        startVideo();
        startSiren();
    }

    private void startSiren() {
        if (DataStore.getInstance().getIsSirenOn()) {
            mp = MediaPlayer.create(this, R.raw.siren);
            mp.start();
        }
    }

    private void init() {
        mVideoView = (CustomVideoView) findViewById(R.id.cvvAlarm);
        btnCloseAndDisarm = (Button) findViewById(R.id.btnAlarmCloseAndDisarm);
        btnClose = (Button) findViewById(R.id.btnAlarmClose);

        btnCloseAndDisarm.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        mProgess = new ProgressDialog(this);
    }

    private void startVideo() {
        mVideoView.setVideo(DataStore.getInstance().getCameras().get(0));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAlarmCloseAndDisarm:
                showProgressDialog("Connecting to panel", "Please wait...");
                PanelManager.makePanelCommand(AlarmActivity.this, "disarm", new Factory.GenericCallback() {
                    @Override
                    public void onDone(boolean success, Object result) {
                        if (success) {
                            hideProgressDialog();
                            LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(new Intent(PanelManager.ACTION_PANEL_STATUS).putExtra(PanelManager.EXTRA_PANEL_STATUS, PanelManager.PANEL_STATUS_DISARM));
                            AlarmActivity.this.finish();
                        }
                    }
                });
                break;

            case R.id.btnAlarmClose:
                this.finish();
                break;
        }
    }

    protected void showProgressDialog(String title, String message) {
        mProgess.setTitle(title);
        mProgess.setMessage(message);
        mProgess.show();
    }

    protected void hideProgressDialog() {
        mProgess.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.getmVideoView().pause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isAlarmShowing = true;
        registerReceiver(receiver, new IntentFilter(ACTION_DISALERT));
    }

    @Override
    protected void onStop() {
        super.onStop();
        isAlarmShowing = false;
        unregisterReceiver(receiver);

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if (ACTION_DISALERT.equals(intent.getAction())) {
                if (MainFragment.isShown) {
                    AlarmActivity.this.finish();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(AlarmActivity.this);
                builder.setTitle("Panel status");
                builder.setMessage("Turaco security system remotely disarmed!");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AlarmActivity.this.finish();
                    }
                });

                if (alert == null) {
                    alert = builder.create();
                    alert.show();
                }
            }
        }
    };
}
