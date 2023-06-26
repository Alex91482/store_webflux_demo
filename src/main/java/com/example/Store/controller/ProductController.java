package com.example.Store.controller;

import com.example.Store.entity.Product;
import com.example.Store.entity.ProductCategoryEnum;
import com.example.Store.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/product/")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/allProduct")
    public Flux<Product> getAllProduct(){
        return productService.findAll();
    }

    @GetMapping("/byId/{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable long id){
        Mono<Product> product = productService.getById(id);
        return product.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/allTypeOne")
    public Flux<Product> getAllTypeOne(){
        return productService.findAllByCategory(ProductCategoryEnum.TYPE_ONE);
    }

    @GetMapping("/allTypeTwo")
    public Flux<Product> getAllTypeTwo(){
        return productService.findAllByCategory(ProductCategoryEnum.TYPE_TWO);
    }

    @GetMapping("/allOther")
    public Flux<Product> getAllOther(){
        return productService.findAllByCategory(ProductCategoryEnum.OTHER);
    }
}
