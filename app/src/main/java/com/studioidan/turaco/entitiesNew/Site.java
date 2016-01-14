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
    private int siteId;

    public ArrayList<Camera> Cameras;
    @SerializedName("Panels")
    private ArrayList<Panel> panels;

    public ArrayList<Panel> getPanels() {
        if (panels == null) {
            panels = new ArrayList<>();
        }
        return panels;
    }

    public ArrayList<Camera> getCameras() {
        if (Cameras == null)
            Cameras = new ArrayList<>();
        return Cameras;
    }

    public String getSiteId() {
        return "" + siteId;
    }

    public Site() {
        Cameras = new ArrayList<>();
        panels = new ArrayList<>();
    }
}
