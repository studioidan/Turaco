package com.studioidan.turaco.CustomView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sprylab.android.widget.TextureVideoView;
import com.studioidan.turaco.R;
import com.studioidan.turaco.entities.Camera;
import com.studioidan.turaco.singeltones.Factory;
import com.studioidan.turaco.singeltones.ImageDownloader;

//import com.sprylab.android.widget.TextureVideoView;


/**
 * Created by macbook on 9/4/15.
 */
public class CustomVideoView extends LinearLayout implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener {
    private static final String TAG = "CustomVideoView";

    ObjectAnimator animation;
    private Camera mCamera;
    private boolean isStaticImage = false;
    private AsyncTask<String, Bitmap, Bitmap> imageDownloader;

    //views
    private TextureVideoView mVideoView;
    private LinearLayout llLoader;
    private ImageView imgLoader;
    LinearLayout titleHolder;
    TextView tvTitle;
    ImageView imgStaticImage;
    Context mContext;

    public TextureVideoView getmVideoView() {
        return mVideoView;
    }

    public CustomVideoView(Context context) {
        super(context);
        init(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context con) {
        this.mContext = con;
        LayoutInflater.from(getContext()).inflate(R.layout.custom_video_view, this, true);
        mVideoView = (TextureVideoView) findViewById(R.id.customVideoView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mVideoView.setOnInfoListener(this);
        }
        mVideoView.setOnErrorListener(this);
        llLoader = (LinearLayout) findViewById(R.id.loader);
        imgLoader = (ImageView) findViewById(R.id.imgLoader);
        titleHolder = (LinearLayout) findViewById(R.id.titleHolder);
        tvTitle = (TextView) findViewById(R.id.tvVideoTitle);
        imgStaticImage = (ImageView) findViewById(R.id.imgStaticImage);
    }

    public void setVideo(Camera camera) {
        isStaticImage = false;
        if (imageDownloader != null) imageDownloader.cancel(true);

        mCamera = camera;
        startAnimation();
        Uri uri = Uri.parse(mCamera.videoUrl);
        mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(this);
        mVideoView.start();

    }

    public void setStaticImage(Camera camera) {
        isStaticImage = true;
        if (imageDownloader != null) imageDownloader.cancel(true);
        mCamera = camera;
        mVideoView.stopPlayback();
        startAnimation();
        imageDownloader = new ImageDownloader(camera.staticImageUrl, new Factory.GenericCallbackOne() {
            @Override
            public void onDone(Object result) {
                stopAnimation();
                imgStaticImage.setVisibility(VISIBLE);
                if (result != null) {
                    Bitmap bitmap = (Bitmap) result;
                    imgStaticImage.setImageBitmap(bitmap);
                    imgStaticImage.setVisibility(VISIBLE);
                } else {
                    Toast.makeText(mContext, "Error loading image", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute(camera.staticImageUrl);
    }

    public boolean isPlaying() {
        if (mVideoView != null && mVideoView.isPlaying()) {
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
        imgLoader.setImageResource(isStaticImage ? R.drawable.img_static_image : R.drawable.turcaosmalltransparent);
        imgStaticImage.setVisibility(INVISIBLE);
        llLoader.setVisibility(VISIBLE);
        titleHolder.setVisibility(INVISIBLE);

        animation = ObjectAnimator.ofFloat(imgLoader, "rotationY", 0.0f, 360f);
        animation.setDuration(3600);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
    }

    public void showTitle() {

        tvTitle.setText(mCamera.name);
        titleHolder.setVisibility(isStaticImage ? INVISIBLE : VISIBLE);
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
