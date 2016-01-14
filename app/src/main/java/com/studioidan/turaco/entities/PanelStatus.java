package com.studioidan.turaco.entities;

import com.studioidan.turaco.App;
import com.studioidan.turaco.R;

import java.io.Serializable;

/**
 * Created by PopApp_laptop on 03/11/2015.
 */
public class PanelStatus implements Serializable {
    public static final int PANEL_STATUS_DISARMED = 0;
    public static final int PANEL_STATUS_ARMED = 1;
    public static final int PANEL_STATUS_ARM_STAY = 2;

    public static final String PANEL_COMMAND_ARM = "arm";
    public static final String PANEL_COMMAND_DISARM = "disarm";
    public static final String PANEL_COMMAND_ARM_STAY = "stayArm";

    public String getStringFromPanelStatus() {
        if (alarm)
            return "Alarm";
        else if (arm == PANEL_STATUS_ARMED)
            return "Armed";
        else if (arm == PANEL_STATUS_ARM_STAY)
            return "Arm Stay";
        else if (arm == PANEL_STATUS_DISARMED)
            return "Disarmed";
        return "";
    }

    public int getColorFromPanelStatus() {
        if (alarm)
            return App.getContext().getResources().getColor(R.color.redColor);
        else if (arm == PANEL_STATUS_ARMED)
            return App.getContext().getResources().getColor(R.color.redColor);

        else if (arm == PANEL_STATUS_ARM_STAY)
            return App.getContext().getResources().getColor(R.color.orangeColor);
        else if (arm == PANEL_STATUS_DISARMED)
            return App.getContext().getResources().getColor(R.color.greenColor);

        return App.getContext().getResources().getColor(R.color.redColor);

    }

    public int arm;
    public String id;
    public boolean mediatorConnected;
    public boolean receiverConnected;
    public boolean alarm;
    public int zonesTamper;
    public int panelWarnnings;
    public boolean warn_battery;
    public boolean warn_power;
    public boolean warn_tamper;
    public String panelID;
    public String siteID;
    public String last_update;
    public boolean deleted;
}
