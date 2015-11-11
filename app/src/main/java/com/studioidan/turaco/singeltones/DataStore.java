package com.studioidan.turaco.singeltones;

import com.google.gson.reflect.TypeToken;
import com.studioidan.popapplibrary.CPM;
import com.studioidan.turaco.App;
import com.studioidan.turaco.entities.Camera;
import com.studioidan.turaco.entities.Keys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PopApp_laptop on 26/10/2015.
 */
public class DataStore {

    private static DataStore instance = null;

    private String baseUrl;
    private String panel;
    private String userName;
    private String password;
    private int apiInterval;
    private boolean isSirenOn;

    public boolean getIsSirenOn() {
        return isSirenOn;
    }

    public void setIsSirenOn(boolean isSirenOn) {
        this.isSirenOn = isSirenOn;
        CPM.putBoolean(Keys.IS_SIREN_ON, isSirenOn, App.getContext());
    }


    public int getApiInterval() {
        return apiInterval;
    }

    public void setApiInterval(int apiInterval) {
        this.apiInterval = apiInterval;
        CPM.putInt(Keys.INTERVAL, apiInterval, App.getContext());
    }


    public ArrayList<Camera> getCameras() {
        if (cameras == null)
            loadDefaultCameras();
        return cameras;
    }

    private ArrayList<Camera> cameras;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        CPM.putString(Keys.USERNAME, userName, App.getContext());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        CPM.putString(Keys.PASSWORD, password, App.getContext());
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        CPM.putString(Keys.BASE_URL, baseUrl, App.getContext());
    }

    public String getPanel() {
        return panel;
    }

    public void setPanel(String panel) {
        this.panel = panel;
        CPM.putString(Keys.PANEL, panel, App.getContext());
    }

    public static DataStore getInstance() {
        if (instance == null)
            instance = new DataStore();

        return instance;
    }

    private DataStore() {
        //baseUrl = CPM.getString(Keys.BASE_URL, "http://69.64.63.136", App.getContext());
        //panel = CPM.getString(Keys.PANEL, "8", App.getContext());

        baseUrl = CPM.getString(Keys.BASE_URL, "http://192.168.0.98", App.getContext());
        panel = CPM.getString(Keys.PANEL, "20", App.getContext());

        userName = CPM.getString(Keys.USERNAME, "e", App.getContext());
        password = CPM.getString(Keys.PASSWORD, "e", App.getContext());

        apiInterval = CPM.getInt(Keys.INTERVAL, 4, App.getContext());

        isSirenOn = CPM.getBoolean(Keys.IS_SIREN_ON,false,App.getContext());

        cameras = (ArrayList<Camera>) CPM.getArrayObject(Keys.CAMERAS, new TypeToken<List<Camera>>() {
        }.getType(), App.getContext());
        if (cameras == null)
            loadDefaultCameras();
    }

    private void loadDefaultCameras() {
        cameras = new ArrayList<>();
        cameras.add(new Camera("Camera1", "rtsp://213.57.178.193:4557/onvif1"));
        cameras.add(new Camera("Camera2", "rtsp://t:turaco33@213.57.178.193:554/Streaming/Channels/1?transportmode=unicast&profile=Profile_1"));
        cameras.add(new Camera("Camera3", "rtsp://213.57.178.193:4012/11"));
        cameras.add(new Camera("Camera4", "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov"));
        cameras.add(new Camera("Camera5", "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov"));
        cameras.add(new Camera("Camera6", "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov"));
    }
}
