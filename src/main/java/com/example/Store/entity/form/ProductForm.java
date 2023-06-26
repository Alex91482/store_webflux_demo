package com.example.Store.entity.form;

import com.example.Store.entity.ProductAvailability;
import com.example.Store.entity.ProductCategoryEnum;

import java.util.List;

public class ProductForm {

    /**
     * id товара если обновляем товар
     */
    private Long id;
    /**
     * Название товара
     */
    private String name;
    /**
     * Цена товара
     */
    private Double price;
    /**
     * описание продукта
     */
    private String description;
    /**
     * Наличие товара
     */
    private ProductAvailability productAvailability;
    /**
     * Включена ли распродажа для данного товара
     */
    private Boolean sale;
    /**
     * Цена на распродаже
     */
    private Double priceSale;
    /**
     * категория товара
     */
    private ProductCategoryEnum productCategoryEnum;
    /**
     * в зависимости от цвета показываются разные фото
     */
    private List<ProductColorAndImageForm> listProductColor;

    public ProductForm(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductAvailability getProductAvailability() {
        return productAvailability;
    }

    public void setProductAvailability(ProductAvailability productAvailability) {
        this.productAvailability = productAvailability;
    }

    public Boolean getSale() {
        return sale;
    }

    public void setSale(Boolean sale) {
        this.sale = sale;
    }

    public Double getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(Double priceSale) {
        this.priceSale = priceSale;
    }

    public ProductCategoryEnum getProductCategoryEnum() {
        return productCategoryEnum;
    }

    public void setProductCategoryEnum(ProductCategoryEnum productCategoryEnum) {
        this.productCategoryEnum = productCategoryEnum;
    }

    public List<ProductColorAndImageForm> getListProductColor() {
        return listProductColor;
    }

    public void setListProductColor(List<ProductColorAndImageForm> listProductColor) {
        this.listProductColor = listProductColor;
    }

    @Override
    public String toString() {
        return "ProductForm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", productAvailability=" + productAvailability +
                ", sale=" + sale +
                ", priceSale=" + priceSale +
                ", productCategoryEnum=" + productCategoryEnum +
                ", listProductColor=" + listProductColor +
                '}';
    }
}
