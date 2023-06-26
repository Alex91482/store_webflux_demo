package com.example.Store.entity.video;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "video")
@XmlAccessorType(XmlAccessType.FIELD)
public class VideoOnPage {

    /**
     * id видео
     */
    private int id;
    /**
     * путь к видео
     */
    private String fileName;
    /**
     * расположение на странице
     */
    private LocationOnThePage locationOnThePage;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
