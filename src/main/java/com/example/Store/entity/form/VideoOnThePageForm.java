package com.example.Store.entity.form;

import com.example.Store.entity.video.LocationOnThePage;

public class VideoOnThePageForm {
    /**
     * название файла
     */
    private String fileName;
    /**
     * место для размещения (всего два, верх и низ)
     */
    private LocationOnThePage locationOnThePage;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocationOnThePage getLocationOnThePage() {
        return locationOnThePage;
    }

    public void setLocationOnThePage(LocationOnThePage locationOnThePage) {
        this.locationOnThePage = locationOnThePage;
    }
}
