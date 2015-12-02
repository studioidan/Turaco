package com.studioidan.turaco.entitiesNew;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by PopApp_laptop on 30/11/2015.
 */
public class ClientUser implements Serializable {

    @SerializedName("FullName")
    public String fullName;
    @SerializedName("_Client")
    public Client client;

    public ClientUser() {
        fullName = "default";
        client = new Client();
    }
}
