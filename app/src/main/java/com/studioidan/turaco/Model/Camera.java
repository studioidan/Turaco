package com.studioidan.turaco.Model;

import java.io.File;

/**
 * Created by macbook on 8/29/15.
 */
public class Camera extends Model {
    public String getCameraId() {
        return CameraId;
    }

    public void setCameraId(String cameraId) {
        CameraId = cameraId;
    }

    public String getCameraUrl() {
        return cameraUrl;
    }

    public void setCameraUrl(String cameraUrl) {
        this.cameraUrl = cameraUrl;
    }

    String CameraId;
    String cameraUrl;

    public int getmIconLogo() {
        return mIconLogo;
    }

    public void setmIconLogo(int mIconLogo) {
        this.mIconLogo = mIconLogo;
    }

    int mIconLogo;

    public String getPictureUrl() {
        return PictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        PictureUrl = pictureUrl;
    }

    String PictureUrl="";

    public File getmPictureFile() {
        return mPictureFile;
    }

    public void setmPictureFile(File mPictureFile) {
        this.mPictureFile = mPictureFile;
    }

    File mPictureFile;

    public Camera(String mID,String mCURK){
        setID(Integer.parseInt(mID));
        setCameraId(mID);
        setCameraUrl(mCURK);
    }
}
