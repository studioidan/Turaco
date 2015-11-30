package com.studioidan.turaco.entitiesNew;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by PopApp_laptop on 30/11/2015.
 */
public class Client {
    @SerializedName("ClientId")
    public int clientId;
    @SerializedName("Sites")
    public ArrayList<Site> sites;
}

