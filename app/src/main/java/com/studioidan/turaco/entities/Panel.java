package com.studioidan.turaco.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by PopApp_laptop on 02/12/2015.
 */
public class Panel implements Serializable {
    private int PanelId;
    public String name;
    public String mac;

    @SerializedName("PanelStatus")
    private PanelStatus panelStatus;

    public PanelStatus getPanelStatus() {
        return panelStatus;
    }

    public void setPanelStatus(PanelStatus panelStatus) {

        this.panelStatus = panelStatus;
    }

    public Panel() {
        name = "default";
        mac = "AA:BB:CC:DD:EE:FF";
        panelStatus = new PanelStatus();
    }


    public String getPanelId() {
        return "" + PanelId;
    }
}
