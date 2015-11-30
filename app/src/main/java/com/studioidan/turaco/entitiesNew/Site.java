package com.studioidan.turaco.entitiesNew;

import com.google.gson.annotations.SerializedName;
import com.studioidan.turaco.entities.Camera;
import com.studioidan.turaco.entities.Panel;

import java.util.ArrayList;

/**
 * Created by PopApp_laptop on 30/11/2015.
 */
public class Site {
    @SerializedName("SiteId")
    public int siteId;
    public ArrayList<Camera> Cameras;
    public ArrayList<Panel> Panels;
}
