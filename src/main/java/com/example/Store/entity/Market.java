package com.example.Store.entity;

import javax.xml.bind.annotation.*;
import java.util.Map;

@XmlRootElement(namespace = "market")
@XmlAccessorType(XmlAccessType.FIELD)
public class Market {

    /**
     * Market являестя корневым элементом xml файла
     */
    @XmlElement(name = "product")
    private Map<Long, Product> productMap;

    public Map<Long, Product> getProductMap() {
        return productMap;
    }

    public void setProductMap(Map<Long, Product> productMap) {
        this.productMap = productMap;
    }
}
