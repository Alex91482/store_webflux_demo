package com.example.Store.dao.interfaces;

import com.example.Store.entity.Product;
import com.example.Store.entity.ProductCategoryEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductDAO {

    void save(Product product);
    Mono<Void> update(Product product);
    Mono<Product> saveAndReturnProduct(Product product);
    Mono<Void> delete(Long id);
    Mono<Product> findById(Long id);
    Flux<Product> findByName(String name);
    Flux<Product> findAllProduct();
    Flux<Product> findAllProductByCategory(ProductCategoryEnum productCategoryEnum);
}
