package com.studioidan.turaco.Fragments.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.studioidan.turaco.MainActivity;
import com.studioidan.turaco.R;
import com.studioidan.turaco.singeltones.DataStore;

/**
 * Created by PopApp_laptop on 04/11/2015.
 */
public class DialogSetPanelAddress extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ///Views
    private Spinner spinner;
    private Button btnSave, btnViewPushLogs;
    EditText etIp, etPanel;
    SeekBar sb;
    TextView tvInterval;
    ToggleButton toggleButton;
    boolean isSpinnerFirst = true;

    //Vars
    ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_set_panel_address, container);
        spinner = (Spinner) v.findViewById(R.id.spPanelAddress);
        btnSave = (Button) v.findViewById(R.id.btnPanelAddressSave);
        btnViewPushLogs = (Button) v.findViewById(R.id.btnPanelViewLogs);
        etIp = (EditText) v.findViewById(R.id.etIp);
        etPanel = (EditText) v.findViewById(R.id.etPanel);
        sb = (SeekBar) v.findViewById(R.id.seekBar);
        tvInterval = (TextView) v.findViewById(R.id.tvInterval);
        toggleButton = (ToggleButton) v.findViewById(R.id.toggleButton);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setadapter();
        btnSave.setOnClickListener(this);
        btnViewPushLogs.setOnClickListener(this);
        tvInterval.setText("" + DataStore.getInstance().getApiInterval());
        etIp.setText(DataStore.getInstance().getBaseUrl());
        etPanel.setText(DataStore.getInstance().getPanel());
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int interval = progress + 2;
                tvInterval.setText("" + (interval));
                DataStore.getInstance().setApiInterval(interval);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb.setProgress(DataStore.getInstance().getApiInterval() - 2);
        toggleButton.setOnClickListener(this);
        toggleButton.setChecked(DataStore.getInstance().getIsSirenOn());

        //show correct ip address in spinner
        for (int i = 0; i < getActivity().getResources().getStringArray(R.array.ips).length; ++i) {
            String ip = getActivity().getResources().getStringArray(R.array.ips)[i];
            if (ip.equals(etIp.getText().toString().trim())) {
                spinner.setSelection(i);
                break;
            }
        }

        spinner.setOnItemSelectedListener(this);

    }

    private void setadapter() {
        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, getResources().getStringArray(R.array.ips));
        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        String ip;
        switch (v.getId()) {
            case R.id.btnPanelAddressSave:
                ip = etIp.getText().toString().trim();
                if (!ip.startsWith("http://")) {
                    Toast.makeText(getActivity(), "ip must start with: http://", Toast.LENGTH_SHORT).show();
                    return;
                }
                DataStore.getInstance().setBaseUrl(ip);
                DataStore.getInstance().setPanel(etPanel.getText().toString().trim());
                dismiss();
                ((MainActivity) getActivity()).getmBar().updateHeaderBar();
                break;

            case R.id.toggleButton:
                DataStore.getInstance().setIsSirenOn(toggleButton.isChecked());
                break;
            case R.id.btnPanelViewLogs:
                DialogPushLog dialogPushLog = new DialogPushLog();
                dialogPushLog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                dialogPushLog.show(getChildFragmentManager(), "TAG");
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (isSpinnerFirst) {
            isSpinnerFirst = false;
            return;
        }
        String ip = adapter.getItem(spinner.getSelectedItemPosition());
        etIp.setText(ip);
        //DataStore.getInstance().setBaseUrl(ip);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
