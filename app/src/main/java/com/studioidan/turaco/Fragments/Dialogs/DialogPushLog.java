package com.studioidan.turaco.Fragments.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.studioidan.turaco.Adapters.AdapterPushLog;
import com.studioidan.turaco.R;
import com.studioidan.turaco.entities.PushLog;
import com.studioidan.turaco.singeltones.DataStore;

import java.util.ArrayList;

/**
 * Created by PopApp_laptop on 01/12/2015.
 */
public class DialogPushLog extends DialogFragment {

    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dilaog_push_logs, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.listView);
        ArrayList<PushLog> pushLogs = DataStore.getInstance().getLogs();
        if (pushLogs.size() == 0) {
            Toast.makeText(getActivity(), "No Logs Yet!", Toast.LENGTH_SHORT).show();
            return;
        }
        listView.setAdapter(new AdapterPushLog(pushLogs, getActivity()));
    }
}
