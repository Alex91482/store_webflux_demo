package com.example.Store.entity;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "ProductCategoryEnum")
@XmlEnum
public enum ProductCategoryEnum {

    TYPE_ONE("Тип 1"),
    TYPE_TWO("Тип 2"),
    OTHER("Другое");

    private final String value;

    ProductCategoryEnum(String value){
        this.value = value;
    }

    public String toString(){
        return value;
    }
}
