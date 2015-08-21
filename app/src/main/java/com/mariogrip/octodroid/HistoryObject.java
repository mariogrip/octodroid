package com.mariogrip.octodroid;

/**
 * Created by neil on 8/18/15.
 */
public class HistoryObject  {

    private float BedActTemp;
    private float HotEndActTemp;
    private float BedTargetTemp;
    private float HotEndTargetTemp;
    private int time;

    public HistoryObject(){

    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public float getBedActTemp() {
        return BedActTemp;
    }

    public void setBedActTemp(float bedActTemp) {
        BedActTemp = bedActTemp;
    }

    public float getHotEndActTemp() {
        return HotEndActTemp;
    }

    public void setHotEndActTemp(float hotEndActTemp) {
        HotEndActTemp = hotEndActTemp;
    }

    public float getBedTargetTemp() {
        return BedTargetTemp;
    }

    public void setBedTargetTemp(float bedTargetTemp) {
        BedTargetTemp = bedTargetTemp;
    }

    public float getHotEndTargetTemp() {
        return HotEndTargetTemp;
    }

    public void setHotEndTargetTemp(float hotEndTargetTemp) {
        HotEndTargetTemp = hotEndTargetTemp;
    }
}
