package com.studioidan.turaco.Fragments.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.studioidan.turaco.R;
import com.studioidan.turaco.entities.CameraManager;
import com.studioidan.turaco.singeltones.DataStore;

/**
 * Created by PopApp_laptop on 19/11/2015.
 */
public class DialogCameraActions extends DialogFragment implements View.OnClickListener {

    public static final String ARG_ACTION = "arg.action";
    public static final String ACTION_COPY_ONE = "action.copy.one";
    public static final String ACTION_COPY_ALL = "action.copy.all";

    public String action = ACTION_COPY_ONE;

    //Views
    EditText etSource, etTarget;
    Button btnCopy, btnCancel;
    TextView tvTitle;

    public static DialogCameraActions getInstance(String action) {
        DialogCameraActions answer = new DialogCameraActions();
        Bundle bundle = new Bundle(1);
        bundle.putString(ARG_ACTION, action);
        answer.setArguments(bundle);
        return answer;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        action = getArguments().getString(ARG_ACTION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_camera_actions, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTitle = (TextView) view.findViewById(R.id.tvCameraActionsTitle);
        etSource = (EditText) view.findViewById(R.id.etSourceCamera);
        etTarget = (EditText) view.findViewById(R.id.etTargetCamera);
        btnCopy = (Button) view.findViewById(R.id.btnCopy);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        btnCopy.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        if (action.equals(ACTION_COPY_ALL)) {
            tvTitle.setText("Copy camera url to all other camera's");
            view.findViewById(R.id.rlTargetCamera).setVisibility(View.GONE);
        } else {
            tvTitle.setText("Copy camera url to another camera");
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnCancel) {
            dismiss();
            return;
        }
        String sourceStr = etSource.getText().toString().trim();
        if (sourceStr.length() == 0) {
            Toast.makeText(getActivity(), "invalid source camera", Toast.LENGTH_SHORT).show();
            return;
        }
        int source = Integer.parseInt(sourceStr);
        String targetStr;

        switch (v.getId()) {
            case R.id.btnCopy:
                if (action.equals(ACTION_COPY_ALL)) {
                    copyToAll(source);
                } else {
                    targetStr = etTarget.getText().toString().trim();
                    if (targetStr.length() == 0) {
                        Toast.makeText(getActivity(), "invalid target camera", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int target = Integer.parseInt(targetStr);
                    copyOne(source, target);
                }

                break;
        }
    }

    private void copyOne(int source, int target) {
        if (source > DataStore.getInstance().getCameras().size()) {
            Toast.makeText(getActivity(), "Source camera number is grater than the total camera's number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (target > DataStore.getInstance().getCameras().size()) {
            Toast.makeText(getActivity(), "Target camera number is grater than the total camera's number", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = CameraManager.getInstance().CopyOneCamera(source, target);
        Toast.makeText(getActivity(), success ? "Camera url was successfully copied" : "Failed to copy", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private void copyToAll(int source) {
        if (source > DataStore.getInstance().getCameras().size()) {
            Toast.makeText(getActivity(), "Source camera number is grater than the total camera's number", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < DataStore.getInstance().getCameras().size(); ++i) {
            CameraManager.getInstance().CopyOneCamera(source, i);
        }

        dismiss();
    }
}
