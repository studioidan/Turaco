package com.studioidan.turaco.Fragments.Dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.studioidan.turaco.R;
import com.studioidan.turaco.entities.Camera;
import com.studioidan.turaco.singeltones.DataStore;

/**
 * Created by PopApp_laptop on 18/11/2015.
 */
public class DialogSetCameraSettings extends DialogFragment implements View.OnClickListener {

    public static final String ARG_CAMERA_POSITION = "arg.camera";

    private Camera mCamera;

    EditText etCameraName, etCameraVideoLink, etCameraImageUrl;
    Button btnLoadIcon, btnSave;

    public static DialogSetCameraSettings getInstance(int position) {
        DialogSetCameraSettings answer = new DialogSetCameraSettings();
        Bundle bundle = new Bundle(1);
        bundle.getInt(ARG_CAMERA_POSITION, position);
        answer.setArguments(bundle);
        return answer;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int position = getArguments().getInt(ARG_CAMERA_POSITION);
        mCamera = DataStore.getInstance().getCameras().get(position);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.camer_setup_dilog_layout, container);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etCameraName = (EditText) view.findViewById(R.id.etCameraName);
        etCameraVideoLink = (EditText) view.findViewById(R.id.etCameraVideoUrl);
        etCameraImageUrl = (EditText) view.findViewById(R.id.etCameraImageUrl);

        btnLoadIcon = (Button) view.findViewById(R.id.btnLoadIcon);
        btnSave = (Button) view.findViewById(R.id.btnSave);

        btnLoadIcon.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        etCameraName.setText(mCamera.name);
        etCameraVideoLink.setText(mCamera.url);

    }

    @Override
    public void onClick(View v) {

    }
}
