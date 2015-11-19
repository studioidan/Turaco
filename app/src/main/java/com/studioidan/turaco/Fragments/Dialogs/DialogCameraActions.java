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

import com.studioidan.turaco.R;

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
        switch (v.getId()) {
            case R.id.btnCopy:

                break;
            case R.id.btnCancel:
                dismiss();
                break;
        }
    }
}
