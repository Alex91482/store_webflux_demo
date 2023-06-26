package com.example.Store.entity;


import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

    /**
     * id товара
     */
    private Long id;
    /**
     * Название товара
     */
    private String name;
    /**
     * Описание товара
     */
    private String description;
    /**
     * Цена товара
     */
    private Double price;
    /**
     * Наличие товара
     */
    private ProductAvailability productAvailability;
    /**
     * категория товара
     */
    private ProductCategoryEnum productCategoryEnum;
    /**
     * Включена ли распродажа для данного товара
     */
    private Boolean sale;
    /**
     * Цена на распродаже
     */
    private Double priceSale;
    /**
     * в зависимости от цвета показываются разные фото
     */
    @XmlElement(name = "productColor")
    private List<ProductColor> listProductColor;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ProductAvailability getProductAvailability() {
        return productAvailability;
    }

    public void setProductAvailability(ProductAvailability productAvailability) {
        this.productAvailability = productAvailability;
    }

    public ProductCategoryEnum getProductCategoryEnum() {
        return productCategoryEnum;
    }

    public void setProductCategoryEnum(ProductCategoryEnum productCategoryEnum) {
        this.productCategoryEnum = productCategoryEnum;
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

    public List<ProductColor> getListProductColor() {
        return listProductColor;
    }

    public void setListProductColor(List<ProductColor> listProductColor) {
        this.listProductColor = listProductColor;
    }
}
