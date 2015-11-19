package com.studioidan.turaco.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.studioidan.turaco.App;
import com.studioidan.turaco.Fragments.VideoWatchFragment;
import com.studioidan.turaco.Fragments.SetupMainFragment;
import com.studioidan.turaco.MainActivity;
import com.studioidan.turaco.R;
import com.studioidan.turaco.singeltones.DataStore;


/**
 * Created by macbook on 8/25/15.
 */
public class HeaderBar extends LinearLayout implements View.OnClickListener {
    ImageView mCamera, mChangeUrl;
    TextView mUser, mAppVersion, mPanelVersion;
    private ProgressWheel progressWheel;

    public ImageView getImgHeader() {
        return imgHeader;
    }

    ImageView imgHeader;

    //dfd
    public HeaderBar(Context context) {


        super(context);
        init();
    }

    public HeaderBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.hearder_bar, this, true);


        mCamera = (ImageView) findViewById(R.id.imbtn_camera_main_layout);
        mChangeUrl = (ImageView) findViewById(R.id.imbtn_changeurl_main_layout);

        mUser = (TextView) findViewById(R.id.lbl_user_name_main_layout);
        mAppVersion = (TextView) findViewById(R.id.lbl_appversion_main_layout);
        mPanelVersion = (TextView) findViewById(R.id.lbl_pnl_main_layout);
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        imgHeader = (ImageView) findViewById(R.id.img_user_icon_main_layout);
        mCamera.setOnClickListener(this);
        mChangeUrl.setOnClickListener(this);
        imgHeader.setOnClickListener(this);
    }

    public void showHeaderBar() {
        if (getVisibility() == View.INVISIBLE || getVisibility() == View.GONE) {
            setVisibility(View.VISIBLE);
        }
        updateHeaderBar();
    }

    public void hideHeaderBar() {
        if (getVisibility() == View.VISIBLE) {
            setVisibility(View.GONE);
        }
    }

    public void updateHeaderBar() {
        mUser.setText("Hello " + DataStore.getInstance().getUserName());
        mPanelVersion.setText("panel " + DataStore.getInstance().getPanel());
        mAppVersion.setText("ver " + App.VERSION);


    }

    public void showWheel(boolean mode) {
        //progressWheel.setVisibility(mode ? VISIBLE : INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        // Fragment fragment = ((MainActivity) getContext()).getCurrentFragment();
        // boolean addToBack = true;

        switch (view.getId()) {
            case R.id.imbtn_camera_main_layout:
                ((MainActivity) getContext()).addContentFragment(VideoWatchFragment.newInstance(), true);
                break;

            case R.id.imbtn_changeurl_main_layout:
                ((MainActivity) getContext()).addContentFragment(SetupMainFragment.newInstance(), true);
                break;
            case R.id.img_user_icon_main_layout:
                ((MainActivity) getContext()).onBackPressed();
                break;

        }
    }
}
