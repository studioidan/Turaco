package com.studioidan.turaco.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.studioidan.turaco.Adapters.AdapterCameraSettings;
import com.studioidan.turaco.Base.BaseFragment;
import com.studioidan.turaco.Fragments.Dialogs.DialogCameraActions;
import com.studioidan.turaco.Fragments.Dialogs.DialogSetCameraSettings;
import com.studioidan.turaco.R;

/**
 * Created by popApp on 8/30/15.
 */
public class SetupCameraFragment extends BaseFragment implements View.OnClickListener, DialogInterface.OnDismissListener {

    TextView mChange2Cameras, mChangeAllCameras;
    ImageView mImageCamera2camer, mImageCamera2All;
    GridView mGrid;
    AdapterCameraSettings mAdapter;

    //dialogs
    DialogCameraActions dialogCameraActions;
    DialogSetCameraSettings dialogSetCameraSettings;

    public static SetupCameraFragment newInstance() {
        return new SetupCameraFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.setup_camera_layout, container, false);

        mChange2Cameras = (TextView) mView.findViewById(R.id.tv_changecamer2camera);
        mChangeAllCameras = (TextView) mView.findViewById(R.id.tv_changeCamerAll);
        mImageCamera2All = (ImageView) mView.findViewById(R.id.imv_camera2all);
        mImageCamera2camer = (ImageView) mView.findViewById(R.id.imv_caner2acamer);
        mGrid = (GridView) mView.findViewById(R.id.gv_cameralist);

        mChangeAllCameras.setOnClickListener(this);
        mChange2Cameras.setOnClickListener(this);
        mImageCamera2All.setOnClickListener(this);
        mImageCamera2camer.setOnClickListener(this);

        mAdapter = new AdapterCameraSettings(ds.getCameras(), getActivity());
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogSetCameraSettings = DialogSetCameraSettings.getInstance(position);
                dialogSetCameraSettings.show(getChildFragmentManager(), "TAG");
               // dialogSetCameraSettings.getDialog().setOnDismissListener(SetupCameraFragment.this);
            }
        });


        getChildFragmentManager().executePendingTransactions();

        return mView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.imv_camera2all:
            case R.id.tv_changeCamerAll:

                dialogCameraActions = DialogCameraActions.getInstance(DialogCameraActions.ACTION_COPY_ALL);
                dialogCameraActions.show(getChildFragmentManager(), "TAG");
                //dialogCameraActions.getDialog().setOnDismissListener(this);

                break;
            case R.id.tv_changecamer2camera:
            case R.id.imv_caner2acamer:

                dialogCameraActions = DialogCameraActions.getInstance(DialogCameraActions.ACTION_COPY_ONE);
                dialogCameraActions.show(getChildFragmentManager(), "TAG");
                //dialogCameraActions.getDialog().setOnDismissListener(this);
                break;
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        mAdapter.notifyDataSetChanged();
    }
}
