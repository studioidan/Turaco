package com.studioidan.turaco.entitiesNew;

import com.google.gson.annotations.SerializedName;
import com.studioidan.turaco.entities.Camera;
import com.studioidan.turaco.entities.Panel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by PopApp_laptop on 30/11/2015.
 */
public class Site implements Serializable {
    @SerializedName("SiteId")
    public int siteId;
    public ArrayList<Camera> Cameras;
    public ArrayList<Panel> Panels;

    public Site() {
        Cameras = new ArrayList<>();
        Panels = new ArrayList<>();
    }
}
