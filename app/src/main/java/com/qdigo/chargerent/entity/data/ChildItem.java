package com.qdigo.chargerent.entity.data;

/**
 * Created by jpj on 2017-10-12.
 */

public class ChildItem {
    private String title;//子项显示的文字
    private int markerImgId;//每个子项的图标

    public ChildItem(String title, int markerImgId)
    {
        this.title = title;
        this.markerImgId = markerImgId;

    }

    public ChildItem(String title){
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getMarkerImgId() {
        return markerImgId;
    }
    public void setMarkerImgId(int markerImgId) {
        this.markerImgId = markerImgId;
    }


}

