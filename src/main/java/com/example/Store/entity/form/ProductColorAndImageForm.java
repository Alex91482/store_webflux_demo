package com.example.Store.entity.form;

import java.util.List;

public class ProductColorAndImageForm {
    /**
     * название цвета
     */
    private String colorName;
    /**
     * название изображения
     */
    private List<String> imageName;

    public ProductColorAndImageForm(){}

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public List<String> getImageName() {
        return imageName;
    }

    public void setImageName(List<String> imageName) {
        this.imageName = imageName;
    }

    @Override
    public String toString() {
        return "ProductColorAndImageForm{" +
                "colorName='" + colorName + '\'' +
                ", imageName=" + imageName +
                '}';
    }
}
