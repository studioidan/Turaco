package com.studioidan.turaco.CustomView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;

import com.sprylab.android.widget.TextureVideoView;
import com.studioidan.turaco.R;
import com.studioidan.turaco.entities.Camera;

//import com.sprylab.android.widget.TextureVideoView;


/**
 * Created by macbook on 9/4/15.
 */
public class CustomVideoView extends LinearLayout implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener {
    private static final String TAG = "CustomVideoView";

    public TextureVideoView getmView() {
        return mView;
    }

    private TextureVideoView mView;
    private LinearLayout llLoader;
    private ImageView imgLoader;
    //private Uri mUri;
    private MediaController controller;
    ObjectAnimator animation;
    //private int position;
    private Camera mCamera;
    LinearLayout titleHolder;
    TextView tvTitle;


    public CustomVideoView(Context context) {
        super(context);
        init();
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.custom_video_view, this, true);
        mView = (TextureVideoView) findViewById(R.id.customVideoView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mView.setOnInfoListener(this);
        }
        mView.setOnErrorListener(this);
        llLoader = (LinearLayout) findViewById(R.id.loader);
        imgLoader = (ImageView) findViewById(R.id.imgLoader);
        titleHolder = (LinearLayout) findViewById(R.id.titleHolder);
        tvTitle = (TextView) findViewById(R.id.tvVideoTitle);
        controller = new MediaController(getContext());
    }

    public void setVideo(Camera camera) {
        //this.position = position;
        mCamera = camera;
        startAnimation();
        Log.d(TAG, "trying to load video: " + camera.url);
        //if (!mCamera.equals(camera)) {
        //mUri = uri;
        Uri uri = Uri.parse(mCamera.url);
        mView.setVideoURI(uri);
        //controller.setMediaPlayer(mView);
        //mView.setMediaController(controller);
        mView.requestFocus();
        mView.setOnPreparedListener(this);
        mView.start();
        //}
    }

    public boolean isPlaying() {
        if (mView != null && mView.isPlaying()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        stopAnimation();
        return false;
    }

    private void startAnimation() {
        titleHolder.setVisibility(INVISIBLE);
        llLoader.setVisibility(VISIBLE);

        animation = ObjectAnimator.ofFloat(imgLoader, "rotationY", 0.0f, 360f);
        animation.setDuration(3600);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
    }

    public void showTitle() {
        tvTitle.setText(mCamera.name);
        titleHolder.setVisibility(VISIBLE);
    }

    private void stopAnimation() {
        showTitle();
        llLoader.setVisibility(GONE);
        animation.cancel();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "onPrepared");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            stopAnimation();
        }
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            // Here the video starts
            Log.d(TAG, "Video starts");
            stopAnimation();
            return true;
        }
        return false;
    }
}
