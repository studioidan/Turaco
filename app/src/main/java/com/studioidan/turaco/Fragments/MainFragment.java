package com.studioidan.turaco.Fragments;

import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.studioidan.turaco.MainActivity;
import com.studioidan.turaco.R;


/**
 * Created by macbook on 8/19/15.
 */
public class MainFragment extends BaseFragment implements View.OnClickListener{

    VideoView mVideoView;
    String mVidADdress="";
    View MFragment;
    AlertDialog mDialog;
//    MediaPlayer.OnPreparedListener mlistener;
    public static MainFragment newInstance(String Url){
        MainFragment mainFragment=new MainFragment();
        Bundle args = new Bundle();
        args.putString("someInt", Url);
        mainFragment.setArguments(args);
        return mainFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVidADdress=getArguments().getString("someInt");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(MFragment==null) {
            MFragment = (View) inflater.inflate(R.layout.main_fragment_layout, null);
            mVideoView = (VideoView) MFragment.findViewById(R.id.videoView);

//            mlistener=new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mediaPlayer) {
////                    mVideoView.setZOrderOnTop(true);
//                    mVideoView.setZOrderMediaOverlay(false);
////                    mVideoView.requestLayout();
////                    mVideoView.requestFocus();
//                    mVideoView.start();
//                }
//            };




        }
        return MFragment;
    }

    @Override
    public void onPause() {
        if(mVideoView!=null&&mVideoView.isPlaying())
            mVideoView.pause();
        super.onPause();

    }

    @Override
    public void onResume() {
        if(mVideoView!=null&&mVidADdress!=""){
            startVideoStream();
        }
        super.onResume();
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ((MainActivity)getActivity()).getmBar().hideHeaderBar();
    }

    @Override
    public void onDestroy() {
        if(mVideoView!=null){
            mVideoView.stopPlayback();
        }
        super.onDestroy();
        ((MainActivity)getActivity()).getmBar().showHeaderBar();
        ((MainActivity)getActivity()).getmBar().updateHeaderBar();
    }


    @Override
    public void onClick(View view) {

    }

    private void startVideoStream() {
        if(mVidADdress!=""&&!mVideoView.isPlaying()) {
            Uri muri = Uri.parse(mVidADdress);
            mVideoView.setVideoURI(muri);
            mVideoView.start();

//            mVideoView.setOnPreparedListener(mlistener);

        }else if(mVidADdress==""){
            Toast.makeText(getActivity(), "No Url, Change Url", Toast.LENGTH_SHORT).show();
        }else if(mVideoView.isPlaying()){
            Toast.makeText(getActivity(), "Camera is already Playing", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public String tag() {
        return getClass().getName();
    }
}
