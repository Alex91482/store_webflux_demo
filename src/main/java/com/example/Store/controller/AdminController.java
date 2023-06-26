package com.example.Store.controller;

import com.example.Store.entity.Product;
import com.example.Store.entity.form.ProductForm;
import com.example.Store.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;



@Controller
@RequestMapping("/admin/")
public class AdminController {

    private final ProductService productService;

    public AdminController(ProductService productService){
        this.productService = productService;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Mono<Void> deleteProductById(@PathVariable long id){
        return productService.deleteProduct(id);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Mono<HttpStatus> saveOrUpdateProduct(ProductForm productForm) {
        return productService.createRecordProduct(productForm)
                .map(result -> {
                    if(result.getId() != null){
                        return HttpStatus.OK;
                    }
                    return HttpStatus.BAD_REQUEST;
                });
    }
}
