package com.studioidan.turaco.entities;

import java.io.Serializable;

/**
 * Created by PopApp_laptop on 04/11/2015.
 */
public class Camera implements Serializable {
    public String name;
    public String videoUrl;
    public int image;
    public String imageUrl;
    public String staticImageUrl;

    public Camera(String name, String url) {
        this.name = name;
        this.videoUrl = url;
        this.imageUrl = "";
        staticImageUrl = "";
    }

    public Camera(Camera other) {
        this.name = other.name;
        this.videoUrl = other.videoUrl;
        this.image = other.image;
        this.imageUrl = other.imageUrl;
        this.staticImageUrl = other.staticImageUrl;
    }
}
