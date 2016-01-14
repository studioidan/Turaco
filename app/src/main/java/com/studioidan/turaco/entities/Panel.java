package com.studioidan.turaco.entities;

import com.google.gson.annotations.SerializedName;
import com.studioidan.turaco.data.UserManager;
import com.studioidan.turaco.entitiesNew.Site;
import com.studioidan.turaco.singeltones.DataStore;

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
        try {
            if (panelStatus.panelID == null)
                panelStatus.panelID = findPanelID();
            if (panelStatus.panelID != null)
                panelStatus.siteID = findSiteID();
        } catch (Exception ex) {
        }

    }


    private String findPanelID() {
        for (Site s : UserManager.getInstance().getClientUser().client.getSites()) {
            for (Panel p : s.getPanels()) {
                if (p.panelStatus.id.equals(this.getPanelStatus().id))
                    return p.getPanelId();
            }
        }
        return null;
    }

    private String findSiteID() {
        for (Site s : UserManager.getInstance().getClientUser().client.getSites()) {
            for (Panel p : s.getPanels()) {
                if (p.getPanelId().equals(this.getPanelId()))
                    return s.getSiteId();
            }
        }
        return null;
    }

    public Panel() {
        name = "default";
        mac = "AA:BB:CC:DD:EE:FF";
        panelStatus = new PanelStatus();
    }


    public String getPanelId() {
        if (DataStore.getInstance().getIsDataSourceServer())
            return "" + PanelId;
        else {
            String panel = DataStore.getInstance().getPanel();
            return panel;
        }
    }
}
