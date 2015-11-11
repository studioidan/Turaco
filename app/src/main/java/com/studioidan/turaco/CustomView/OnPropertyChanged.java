package com.studioidan.turaco.CustomView;

/**
 * Created by macbook on 9/4/15.
 */
public interface OnPropertyChanged {
    public enum ProperyChanges{PAUSED,DESTROIED,RESUMED,AlarmActivated,StateUpdated,CameraPictureUpdate};
    public void OnPropertyChanged(ProperyChanges mComingProperty, int mComingPOsition);

}
