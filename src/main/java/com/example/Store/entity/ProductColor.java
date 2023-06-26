package com.example.Store.entity;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "productColor")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductColor {

    /**
     * цвет
     */
    private String color;
    /**
     * путь к изображениям
     */
    @XmlElement(name = "pathToImage")
    private List<String> pathToImage;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getPathToImage() {
        return pathToImage;
    }

    public void setPathToImage(List<String> pathToImage) {
        this.pathToImage = pathToImage;
    }
}
