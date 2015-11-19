package com.studioidan.turaco.Base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.studioidan.turaco.MainActivity;
import com.studioidan.turaco.singeltones.DataStore;


/**
 * Created by macbook on 8/19/15.
 */
public abstract class BaseFragment extends Fragment {

    /**
     * Progress bar for waiting to data
     */
    ProgressDialog mProgess;

    protected DataStore ds = DataStore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mProgess = new ProgressDialog(getActivity());
        //mProgess.setCancelable(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void addContentFragment(BaseFragment mComingFragment, boolean addToBackStack) {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).addContentFragment(mComingFragment, addToBackStack);
        }
    }

    public void hideVirtualKeyBoard() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).hideVirtualKeyBoard();
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
    public void onResume() {
        super.onResume();
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected void setContext(Context mComingContext) {

    }
}
