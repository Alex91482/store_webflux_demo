package com.example.Store.entity.video;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "locationOnThePage")
@XmlEnum
public enum LocationOnThePage {

    TOP("Вверху"),
    BOTTOM("Внизу");

    private final String value;

    LocationOnThePage(String value){
        this.value = value;
    }

    public String toString(){
        return value;
    }
}
