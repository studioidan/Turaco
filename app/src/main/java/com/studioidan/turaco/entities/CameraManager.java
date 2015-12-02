package com.studioidan.turaco.entities;

import com.studioidan.turaco.R;
import com.studioidan.turaco.singeltones.DataStore;

/**
 * Created by PopApp_laptop on 18/11/2015.
 */
public class CameraManager {
    private static CameraManager instance;

    public static CameraManager getInstance() {
        if (instance == null)
            instance = new CameraManager();
        return instance;
    }

    private DataStore ds = DataStore.getInstance();

    public static int[] cameraIconsBlue = new int[]{
            R.drawable.ic_1_blue,
            R.drawable.ic_2_blue,
            R.drawable.ic_3_blue,
            R.drawable.ic_4_blue,
            R.drawable.ic_5_blue,
            R.drawable.ic_6_blue,
            R.drawable.ic_7_blue,
            R.drawable.ic_8_blue,
            R.drawable.ic_9_blue};

    public static int[] cameraIconsWhite = new int[]{
            R.drawable.ic_1_white,
            R.drawable.ic_2_white,
            R.drawable.ic_3_white,
            R.drawable.ic_4_white,
            R.drawable.ic_5_white,
            R.drawable.ic_6_white,
            R.drawable.ic_7_white,
            R.drawable.ic_8_white,
            R.drawable.ic_9_white};

    public boolean saveCameras() {
        return ds.setCameras(ds.getCameras());
    }

    public boolean CopyOneCamera(int sourcePosition, int targetPosition) {

        if (sourcePosition == targetPosition)
            return true;

        if (sourcePosition > ds.getCameras().size() || targetPosition > ds.getCameras().size())
            return false;
        /*get source camera*/
        Camera cameraSrc = ds.getCameras().get(sourcePosition);
        /*copy it*/
        //Camera cameraTrg = new Camera(cameraSrc);
        /*replace the old one*/
        //ds.getCameras().set(targetPosition, cameraTrg);
        ds.getCameras().get(targetPosition).setVideoLink(cameraSrc.getVideoLink());

        return saveCameras();
    }

}
