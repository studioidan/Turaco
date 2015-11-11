package com.studioidan.turaco.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.studioidan.turaco.CustomView.AsyncCircularImageView;
import com.studioidan.turaco.CustomView.OnPropertyChanged;
import com.studioidan.turaco.Model.Camera;
import com.studioidan.turaco.R;
import com.studioidan.turaco.connection.manager.CamerasManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbook on 9/8/15.
 */
public class CamerasAdapter extends BaseAdapter implements OnPropertyChanged {

    private List<OnPropertyChanged> mVideoListener;
    Context mContext;
    private int playingItem;

    public CamerasAdapter(Context mComingContext){
        mContext=mComingContext;
        playingItem=-100;
        CamerasManager.getSharedManager(mContext).clearlistenera();
    }
    @Override
    public int getCount() {
        return CamerasManager.getSharedManager(mContext).getManagers().size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view=(View) LayoutInflater.from(mContext).inflate(R.layout.camera_list_item, null);
            view.setTag(view);
        }else{
            view=(View)view.getTag();
        }
//            if(mVideoListener==null){
//                mVideoListener=new ArrayList<>();
//            }

//        CustomVideoView mView=(CustomVideoView)view.findViewById(R.id.vd_camera_list_item);
//        mView.setVideoURI(Uri.parse(((Camera) ((List) CamerasManager.getSharedManager(mContext).getManagers()).get(i)).getCameraUrl()));

//            if(!mVideoListener.contains((OnPropertyChanged)mView)){
//                mVideoListener.add((OnPropertyChanged)mView);
//            }
//            MediaPlayer.OnPreparedListener mlistener=new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mediaPlayer) {
//
//                }
//            };
//
//            mView.setOnPreparedListener(mlistener);
//            mView.start();
        AsyncCircularImageView mImage=(AsyncCircularImageView)view.findViewById(R.id.vd_camera_list_item);
        if(!((Camera)((List) CamerasManager.getSharedManager(mContext).getManagers()).get(i)).getPictureUrl().isEmpty()){
//            if(mVideoListener==null){
//                mVideoListener=new ArrayList<>();
//            }
//            if(!mVideoListener.contains(mImage)){
//                mVideoListener.add(i,mImage);
//            }
            mImage.setImageURIAsync(((Camera) ((List) CamerasManager.getSharedManager(mContext).getManagers()).get(i)).getPictureUrl());
            mImage.setTag(i);
            CamerasManager.getSharedManager(mContext).addCameraPictureListener(mImage);
        }else if(((Camera)((List) CamerasManager.getSharedManager(mContext).getManagers()).get(i)).getmIconLogo()!=-1){
            mImage.setImageURIAsync("drawable://"+((Camera) ((List) CamerasManager.getSharedManager(mContext).getManagers()).get(i)).getmIconLogo());
            mImage.setTag(null);
        }
        TextView mView2=(TextView)view.findViewById(R.id.lbl_camera_list_item);
        mView2.setText(((Camera)((List) CamerasManager.getSharedManager(mContext).getManagers()).get(i)).getTitle());
        checkCurentPlaying(view, i);

        return view;
    }

    private void checkCurentPlaying(View view, int i) {
        if(playingItem==i){
            view.findViewById(R.id.im_curreuntplaying).setVisibility(View.VISIBLE);
        }else{
            view.findViewById(R.id.im_curreuntplaying).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void OnPropertyChanged(ProperyChanges mComingProperty,int mComingPosition) {
//        if(mVideoListener!=null) {
//            for (int i = 0; i < mVideoListener.size(); i++) {
//                ((OnPropertyChanged) mVideoListener.get(i)).OnPropertyChanged(mComingProperty,i);
//            }
//        }
    }

    public void setPlayingItem(int playingItem) {
        this.playingItem = playingItem;
        notifyDataSetChanged();
    }
}
