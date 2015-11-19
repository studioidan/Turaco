package com.studioidan.turaco.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.studioidan.turaco.Adapters.AdapterCamera;
import com.studioidan.turaco.Base.BaseFragment;
import com.studioidan.turaco.CustomView.CustomVideoView;
import com.studioidan.turaco.R;


/**
 * Created by popApp.
 */
public class VideoWatchFragment extends BaseFragment implements View.OnClickListener {
    AdapterCamera mAdapter;
    CustomVideoView mVideoView;
    View mView;
    ListView mList;
    float ratW, ratH;
    boolean isFullScreen = false;

    public static VideoWatchFragment newInstance() {
        return new VideoWatchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //super.onCreateView(inflater,container,savedInstanceState);
        mView = inflater.inflate(R.layout.camera_list_layout, container, false);
        mList = (ListView) mView.findViewById(R.id.ls_camera_list_layout);
        mVideoView = (CustomVideoView) mView.findViewById(R.id.videoView1);
        mVideoView.setOnClickListener(this);

        mAdapter = new AdapterCamera(getActivity(),ds.getCameras());
        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mVideoView.setVideo(ds.getCameras().get(i));
                mAdapter.setPlayingItem(i);
            }
        });

        mVideoView.setVideo(ds.getCameras().get(0));
        mAdapter.setPlayingItem(0);


        return mView;
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