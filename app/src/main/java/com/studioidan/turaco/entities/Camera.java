package com.studioidan.turaco.entities;

import java.io.Serializable;

/**
 * Created by PopApp_laptop on 04/11/2015.
 */
public class Camera implements Serializable {
    public int CameraId;
    public int fk_zone;
    public String location;
    private String name;
    private String videoLink;
    private String imageLink;
    private String iconUrl;
    public int image;
    public boolean showVideoByDefault;

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Camera(String name, String url) {
        this(name, url, "");
    }

    public Camera(String name, String url, String staticImageUrl) {
        this.name = name;
        this.videoLink = url;
        this.iconUrl = "";
        this.imageLink = staticImageUrl;
    }

    public Camera(Camera other) {
        this.name = other.name;
        this.videoLink = other.videoLink;
        this.image = other.image;
        this.iconUrl = other.iconUrl;
        this.imageLink = other.imageLink;
    }

    public String getVideoLink() {
        if (videoLink == null)
            videoLink = "";
        return videoLink;
    }

    public String getImageLink() {
        if (imageLink == null)
            imageLink = "";
        return imageLink;
    }

    public String getIconUrl() {
        if (iconUrl == null)
            iconUrl = "";
        return iconUrl;
    }
}
