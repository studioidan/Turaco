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

    public static int[] cameraIcons = new int[]{R.drawable.entrance, R.drawable.livingroom, R.drawable.ic_places_garage_kitchen, R.drawable.ic_places_garage, R.drawable.ic_places_bed_room, R.drawable.garden, R.drawable.pool, R.drawable.ic_places_camera};
    public static int[] cameraIconsSelected = new int[]{R.drawable.entrance, R.drawable.livingroom, R.drawable.ic_places_popup_garage_kitchen, R.drawable.ic_places_popup_garage, R.drawable.ic_places_popup_bed_room, R.drawable.garden, R.drawable.pool, R.drawable.ic_places_popup_camera};

    public boolean SaveCameras() {
        return ds.setCameras(ds.getCameras());
    }
}
