package com.studioidan.turaco.entitiesNew;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PopApp_laptop on 30/11/2015.
 */
public class ClientUser {

    @SerializedName("FullName")
    public String fullName;
    @SerializedName("_Client")
    public Client client;
}
