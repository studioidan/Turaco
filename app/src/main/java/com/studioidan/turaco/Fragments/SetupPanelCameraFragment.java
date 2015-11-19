package com.studioidan.turaco.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.studioidan.turaco.Fragments.Dialogs.DialogSetPanelAddress;
import com.studioidan.turaco.R;

/**
 * Created by macbook on 8/28/15.
 */
public class SetupPanelCameraFragment extends BaseFragment implements View.OnClickListener {

    ImageButton CamerEdit, PanelEdit;

    public static SetupPanelCameraFragment newInstance() {
        return new SetupPanelCameraFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.setup_panel_layout, null);
        CamerEdit = (ImageButton) mView.findViewById(R.id.imbtn_camera_edit_setuplayout);
        PanelEdit = (ImageButton) mView.findViewById(R.id.btnSetPanel);

        CamerEdit.setOnClickListener(this);
        PanelEdit.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imbtn_camera_edit_setuplayout:
                addContentFragment(SetupCameraFragment.newInstance(), true);
                break;
            case R.id.btnSetPanel:
                startChangeUrl();
                break;
        }
    }


    private void startChangeUrl() {
        DialogSetPanelAddress setPanelAddressDialog = new DialogSetPanelAddress();
        //setPanelAddressDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        setPanelAddressDialog.show(getChildFragmentManager(), "TAG");
    }

    @Override
    public String tag() {
        return getClass().getName();
    }
}
