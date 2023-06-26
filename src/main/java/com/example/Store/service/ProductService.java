package com.example.Store.service;

import com.example.Store.dao.ProductDAOImpl;
import com.example.Store.entity.Product;
import com.example.Store.entity.ProductCategoryEnum;
import com.example.Store.entity.ProductColor;

import com.example.Store.entity.form.ProductColorAndImageForm;
import com.example.Store.entity.form.ProductForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private static final String PATH_TO_RESOURCE = "/resource/images/";

    private final ProductDAOImpl productDAO;

    public ProductService(ProductDAOImpl productDAO){
        this.productDAO = productDAO;
    }

    public Mono<Void> deleteProduct(Long id){
        logger.info("Delete product by id = {}", id);
        return productDAO.delete(id);
    }

    public Mono<Product> getById(long id){
        return productDAO.findById(id);
    }

    public Flux<Product> findAll(){
        return productDAO.findAllProduct();
    }

    public Flux<Product> findAllByCategory(ProductCategoryEnum categoryEnum){
        return productDAO.findAllProductByCategory(categoryEnum);
    }

    public Mono<Product> createRecordProduct(ProductForm productForm)
    {
        return Mono.create(event -> {
            if(productForm == null){
                logger.warn("Product information cannot be null!");
                event.success(new Product()); //информация о продукте не может быть null
                return;
            }
            logger.info(productForm.toString());
            //проверка полей
            if(productForm.getName() == null
                    || productForm.getProductAvailability() == null
                    || productForm.getPrice() == null)
            {
                event.success(new Product());
                return;
            }
            if(productForm.getName().trim().equals(""))
            {
                event.success(new Product());
                return;
            }
            if(productForm.getProductCategoryEnum() == null){
                productForm.setProductCategoryEnum(ProductCategoryEnum.OTHER);
            }
            if (productForm.getSale() == null){
                productForm.setSale(false);
            }
            if(productForm.getPriceSale() == null){
                productForm.setPriceSale(0.0);
            }
            //сохранение сущности

            Long id = productForm.getId();

            if(id == null){
                //если неn переданно id значит товар новый
                id = ThreadLocalRandom.current().nextLong(); //генерируем случайный long в текущем потоке
            }
            if(id < 0){
                //если id отрицательное умножаем на -1
                id = id * (-1);
            }

            var product = new Product();
            product.setId(id);
            product.setName(productForm.getName()); //название продукта
            product.setPrice(productForm.getPrice()); //цена продукта
            product.setDescription(productForm.getDescription() != null ? productForm.getDescription() : ""); //описание продукта
            product.setProductAvailability(productForm.getProductAvailability()); //наличие
            product.setProductCategoryEnum(productForm.getProductCategoryEnum()); //категория товара
            product.setSale(productForm.getSale()); //включена ли распродажа
            product.setPriceSale(productForm.getPriceSale()); //цена на распродаже

            if(productForm.getListProductColor() != null && productForm.getListProductColor().size() > 0){
                var listImages = new ArrayList<ProductColor>();
                for(ProductColorAndImageForm images : productForm.getListProductColor()){
                    if(images.getColorName() != null
                            && images.getImageName() != null
                            && images.getImageName().size() > 0)
                    {
                        var colorAndImage = new ProductColor();
                        var imageNames = new ArrayList<String>();
                        colorAndImage.setColor(images.getColorName());
                        for(String imagesName : images.getImageName()){
                            if (imagesName.contains("/")) {
                                var sublist = List.of(imagesName.split("/"));
                                imageNames.add(PATH_TO_RESOURCE + sublist.get(sublist.size() -1)); //создаем путь к изображению
                            }else{
                                var sublist = List.of(imagesName.split("\\\\"));
                                imageNames.add(PATH_TO_RESOURCE + sublist.get(sublist.size() -1)); //создаем путь к изображению
                            }
                        }
                        colorAndImage.setPathToImage(imageNames);
                        listImages.add(colorAndImage);
                    }
                }
                product.setListProductColor(listImages); //изображения по цветам
            }
            event.success(product);

        }).flatMap(product -> productDAO.saveAndReturnProduct((Product) product));
    }
}
