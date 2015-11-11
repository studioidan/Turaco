package com.studioidan.turaco.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.studioidan.turaco.CustomAdapter.CamerasAdapter;
import com.studioidan.turaco.CustomView.CustomVideoView;
import com.studioidan.turaco.Model.Camera;
import com.studioidan.turaco.R;
import com.studioidan.turaco.connection.manager.CamerasManager;

import java.util.List;


/**
 * Created by macbook on 8/31/15.
 */
public class CameraListFragment extends BaseFragment implements View.OnClickListener {
    CamerasAdapter mAdapter;
    CustomVideoView mVideoView;
    View mView;
    ListView mList;
    float ratW, ratH;
    boolean isFullScreen = false;

    public static CameraListFragment newInstance() {
        return new CameraListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.camera_list_layout, null);
        mList = (ListView) mView.findViewById(R.id.ls_camera_list_layout);
        mVideoView = (CustomVideoView) mView.findViewById(R.id.videoView1);
        mVideoView.setOnClickListener(this);
        //llFullScreen = (LinearLayout) mView.findViewById(R.id.fullScreen);
        //videosHolder = (LinearLayout) mView.findViewById(R.id.videosHolder);
        mAdapter = new CamerasAdapter(getActivity());
        mList.setAdapter(mAdapter);
       /* mVideoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mVideoView.getUri() != null && mVideoView.isPlaying()) {
                    addContentFragment(MainFragment.newInstance(mVideoView.getUri().toString()), true);

                }
                return false;
            }
        });*/


        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mVideoView.setVideoURI(Uri.parse(((Camera) ((List) CamerasManager.getSharedManager(getActivity()).getManagers()).get(i)).getCameraUrl()), i);
                mAdapter.setPlayingItem(i);
            }
        });

        mVideoView.setVideoURI(Uri.parse(((Camera) ((List) CamerasManager.getSharedManager(getActivity()).getManagers()).get(0)).getCameraUrl()), 0);
        mAdapter.setPlayingItem(0);
        return mView;
    }

    @Override
    public String tag() {
        return getClass().getName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calculateSizeData();
    }

    private void calculateSizeData() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                int width = ((LinearLayout) mVideoView.getParent()).getMeasuredWidth();
                int height = ((LinearLayout) mVideoView.getParent()).getMeasuredHeight();

                int w = mVideoView.getMeasuredWidth();
                int h = mVideoView.getMeasuredHeight();

                ratW = (float) width / (float) h;
                ratH = (float) height / (float) w;
            }
        });

    }

    @Override
    public void onClick(View v) {
        int DURATION = 500;

        if (!isFullScreen) {
            mList.setVisibility(View.GONE);
            mVideoView.animate().scaleX(ratH).scaleY(ratW).rotation(90f).setDuration(DURATION).start();
            isFullScreen = true;
        } else {
            mVideoView.animate().rotationBy(-90.0f).setDuration(DURATION).start();
            mVideoView.animate().scaleY(0.95f).start();
            mVideoView.animate().scaleX(0.95f).start();
            mList.setVisibility(View.VISIBLE);
            isFullScreen = false;
        }
    }
}