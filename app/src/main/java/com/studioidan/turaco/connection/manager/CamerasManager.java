package com.studioidan.turaco.connection.manager;

import android.content.Context;
import android.preference.PreferenceManager;

import com.studioidan.turaco.CustomView.AsyncCircularImageView;
import com.studioidan.turaco.CustomView.OnPropertyChanged;
import com.studioidan.turaco.Model.Camera;
import com.studioidan.turaco.Model.Model;
import com.studioidan.turaco.R;
import com.studioidan.turaco.connection.ListModelManagerBase;
import com.studioidan.turaco.connection.SignalManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbook on 8/29/15.
 */
public class CamerasManager extends ListModelManagerBase<Camera> {

    private SignalManager getCameraSignal, changeCameraSignal, updateCameraPicture;
    private static CamerasManager sharedManager;
    private String[] mCameras = new String[]{
            "rtsp://admin:magalcom3@192.168.0.175:554/Streaming/Channels/101?transportmode=unicast&profile=Profile_1",
            "rtsp://admin:magalcom3@192.168.0.177:554/Streaming/Channels/1?transportmode=unicast&profile=Profile_1",
            "rtsp://e:turaco31@62.219.121.230:554/Streaming/Channels/1?transportmode=unicast&profile=Profile_1",
            "rtsp://admin:magalcom3@213.57.178.193:554/Streaming/Channels/1?transportmode=unicast&profile=Profile_1",
            "rtsp://admin:magalcom3@62.219.121.230:554/Streaming/Channels/101?transportmode=unicast&profile=Profile_1",
            "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov",
    };

    private String[] pictures = {
            "", "", "", "", "", "",
    };

    public String[] getmCameraName() {
        return mCameraName;
    }

    private String[] mCameraName = new String[]{
            "Entrance", "Living Room", "Dining Room", "Garage", "Kids Room", "Garden", "Pool", "Street"
    };

    ArrayList<OnPropertyChanged> mCameraPicturesListener;
    private int[] mPhotos = new int[]{R.drawable.entrance, R.drawable.livingroom, R.drawable.ic_places_garage_kitchen, R.drawable.ic_places_garage, R.drawable.ic_places_bed_room, R.drawable.garden, R.drawable.pool, R.drawable.ic_places_camera};
    private int[] mSelectedPhotos = new int[]{R.drawable.entrance, R.drawable.livingroom, R.drawable.ic_places_popup_garage_kitchen, R.drawable.ic_places_popup_garage, R.drawable.ic_places_popup_bed_room, R.drawable.garden, R.drawable.pool, R.drawable.ic_places_popup_camera};

    public int[] getmPhotos() {
        return mPhotos;
    }

    public int[] getmSelectedPhotos() {
        return mSelectedPhotos;
    }

    public static CamerasManager getSharedManager(Context context) {
        if (sharedManager == null) {
            sharedManager = new CamerasManager(context);
            sharedManager.getCameras();
        }
        return sharedManager;
    }

    public CamerasManager(Context context) {
        super(context);
    }

    @Override
    public void addManager(Camera manager) {
        if (getManagers() == null) {
            managers = new ArrayList<Camera>();
            managers.add(manager);
        } else if (!getManagers().contains(manager)) {
            getManagers().add(manager);
        }
    }

    public SignalManager getCameras() {
        if (getCameraSignal == null || !getCameraSignal.isLoading()) {
            getCameraSignal = new SignalManager(getContext());
        }
        if (!getCameraSignal.isLoading()) {
            for (int i = 0; i < mCameras.length; i++) {
                Camera mCamera = new Camera(i + "", getCameraLink(i) == "" ? mCameras[i] : getCameraLink(i));
                mCamera.setTitle(getCameraName(i) == "" ? mCameraName[i] : getCameraName(i));
                mCamera.setmIconLogo(getCameraIconResource(i) == -1 ? 0 : getCameraIconResource(i));
                mCamera.setPictureUrl(getCameraPictureLink(i) == "" ? pictures[i] : getCameraPictureLink(i));

                addManager(mCamera);
            }
            getCameraSignal.setCompleted(true);
        }
        return getCameraSignal;
    }

    @Override
    public boolean removeManager(Camera manager) {
        if (getManagers().contains(manager)) {
            getManagers().remove(manager);
            return true;
        }
        return false;
    }

    @Override
    protected void onModelChanged(Model oldModel, Model newModel) {
        int lodindex = ((List) getManagers()).indexOf(oldModel);
        int newindex = ((List) getManagers()).indexOf(newModel);
//        ((List)getManagers()).set(lodindex,newModel);
//        ((List)getManagers()).set(newindex,oldModel);
        ((Camera) ((List) getManagers()).get(newindex)).setCameraUrl(((Camera) ((List) getManagers()).get(lodindex)).getCameraUrl());
        storeCameraLink(lodindex, newindex);

    }

    public SignalManager changeCamera(Camera old, Camera New, int oldPosition, int newPosition) {
        if (getCameraSignal == null || !getCameraSignal.isLoading()) {
            getCameraSignal = new SignalManager(getContext());
        }
        if (!getCameraSignal.isLoading()) {
            if (New == null) {//ChangeAll cameras
                int size = getManagers().size();
                sharedManager.getManagers().removeAll(getManagers());
                for (int i = 0; i < size; i++) {
                    sharedManager.addManager(new Camera(i + "", old.getCameraUrl()));
                }
            } else {
                onModelChanged(old, New);
            }
        }

        storeCameraLink(1001, 1001);
        return changeCameraSignal;
    }

    public SignalManager updateCameraInfo(int mComingPosition, String mComingURLm, String mComingTitle) {
        ((Camera) ((List) getManagers()).get(mComingPosition)).setCameraUrl(mComingURLm);
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("Camera" + mComingPosition, mComingURLm).commit();

        ((Camera) ((List) getManagers()).get(mComingPosition)).setTitle(mComingTitle);
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("CameraName" + mComingPosition, mComingTitle).commit();

        return null;
    }

    private void storeCameraLink(int mComingOld, int mComingNew) {
        if (mComingOld != 1001 && mComingNew != 1001) {     //TODO this for copyimg one camera to another;
            String moldurl = ((Camera) ((List) getManagers()).get(mComingOld)).getCameraUrl();
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("Camera" + mComingOld, moldurl).commit();
            String mnewurl = ((Camera) ((List) getManagers()).get(mComingNew)).getCameraUrl();
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("Camera" + mComingNew, mnewurl).commit();
        } else {
            for (int i = 0; i < getManagers().size(); i++) {
                String mID = ((Camera) ((List) getManagers()).get(i)).getCameraId();
                String mnewurl = ((Camera) ((List) getManagers()).get(i)).getCameraUrl();
                PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("Camera" + mID, mnewurl).commit();
            }
        }

    }

    private String getCameraLink(int mComingPosition) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getString("Camera" + mComingPosition, "");
    }

    private String getCameraName(int mComingCameraName) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getString("CameraName" + mComingCameraName, "");
    }

    private String getCameraPictureLink(int mComingCameraName) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getString("CameraPicture" + mComingCameraName, "");
    }

    private int getCameraIconResource(int mComingCameraName) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("CameraIcon" + mComingCameraName, -1);
    }

    public SignalManager updateCameraPictureLink(int position, String mPictureLinkd) {
        ((Camera) ((List) getManagers()).get(position)).setPictureUrl(mPictureLinkd);
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("CameraPicture" + position, mPictureLinkd).commit();
/*
        AlarmManager mAlarm = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        int Alarm_id = 33 + position;
        Intent mIntent = new Intent(getContext(), CameraBroadCastReceiver.class);
        mIntent.putExtra("CameraPosition", position);
        PendingIntent mPending = PendingIntent.getBroadcast(getContext(), Alarm_id, mIntent, 0);
//        mAlarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, Calendar.getInstance().getTimeInMillis(),,);
        mAlarm.cancel(mPending);

        //CHANGED BY EREZ TO REDUCE LATENCY
        // mAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, mPending);
        mAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 8000, mPending);
        */
        return null;
    }

    public SignalManager updateCameraIconResource(int position, int mIconImageResource) {
        ((Camera) ((List) getManagers()).get(position)).setmIconLogo(mIconImageResource);
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putInt("CameraIcon" + position, mIconImageResource).commit();

        return null;
    }

    public SignalManager requestCameraPictureUpdate(final int mComingCameraPosition) {
        if (updateCameraPicture == null || !updateCameraPicture.isLoading()) {
            updateCameraPicture = new SignalManager(getContext());
        }
        if (!updateCameraPicture.isLoading()) {
            if (mCameraPicturesListener != null) {
                for (int i = 0; i < mCameraPicturesListener.size(); i++) {
                    if ((((AsyncCircularImageView) mCameraPicturesListener.get(i)).getTag()) != null) {

                        if (((int) ((AsyncCircularImageView) mCameraPicturesListener.get(i)).getTag()) == mComingCameraPosition) {
                            mCameraPicturesListener.get(i).OnPropertyChanged(OnPropertyChanged.ProperyChanges.CameraPictureUpdate, mComingCameraPosition);
                        }
                    }
                }
            }
        }
        return updateCameraPicture;
    }

    public void addCameraPictureListener(OnPropertyChanged mComingListener) {
        if (mCameraPicturesListener == null) {
            mCameraPicturesListener = new ArrayList<>();
        }
        if (!mCameraPicturesListener.contains(mComingListener)) {
            mCameraPicturesListener.add(mComingListener);
        }
    }

    public void clearlistenera() {
        mCameraPicturesListener = null;

    }
}
