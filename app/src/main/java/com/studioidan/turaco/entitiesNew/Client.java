package com.studioidan.turaco.entitiesNew;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by PopApp_laptop on 30/11/2015.
 */
public class Client implements Serializable {
    @SerializedName("ClientId")
    public int clientId;
    @SerializedName("Sites")
    public ArrayList<Site> sites;

    public Client() {
        sites = new ArrayList<>();
    }
}

