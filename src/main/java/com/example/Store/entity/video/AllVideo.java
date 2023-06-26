package com.example.Store.entity.video;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

@XmlRootElement(namespace = "AllVideo")
@XmlAccessorType(XmlAccessType.FIELD)
public class AllVideo {

    /**
     * корневой элемент для видео
     */
    @XmlElement(name = "video")
    private Map<Integer, VideoOnPage> videoOnPageMap;


    public Map<Integer, VideoOnPage> getVideoOnPageMap() {
        return videoOnPageMap;
    }

    public void setVideoOnPageMap(Map<Integer, VideoOnPage> videoOnPageMap) {
        this.videoOnPageMap = videoOnPageMap;
    }
}
