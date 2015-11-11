package com.studioidan.turaco.entities;

import java.io.Serializable;

/**
 * Created by PopApp_laptop on 03/11/2015.
 */
public class Panel implements Serializable {
    public  static final int PANEL_STATUS_DISARMED = 0;
    public  static final int PANEL_STATUS_ARMED = 1;

    public int arm;
    public boolean mediatorConnected;
    public boolean receiverConnected;
    public boolean alarm;
    public int zonesTamper;
    public int panelWarnnings;
    public boolean warn_battery;
    public boolean warn_power;
    public boolean warn_tamper;
    public String id;
    public String last_update;
    public boolean deleted;
}
