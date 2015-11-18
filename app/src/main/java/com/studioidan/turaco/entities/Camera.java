package com.studioidan.turaco.entities;

import java.io.Serializable;

/**
 * Created by PopApp_laptop on 04/11/2015.
 */
public class Camera implements Serializable {
    public String name;
    public String url;
    public int image;
    public String imageUrl;

    public Camera(String name, String url) {
        this.name = name;
        this.url = url;
        this.imageUrl = "";
    }
}
