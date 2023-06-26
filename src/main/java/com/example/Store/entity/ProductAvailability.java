package com.example.Store.entity;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "productAvailability")
@XmlEnum
public enum ProductAvailability {

    AVAILABLE("В наличии"),
    NOT_AVAILABLE("Нет в наличии"),
    IN_STOCK("На складе");

    private final String value;

    ProductAvailability(String value){
        this.value = value;
    }

    public String toString(){
        return value;
    }
}
