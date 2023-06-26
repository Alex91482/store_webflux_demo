package com.example.Store.configuration;

import com.example.Store.entity.Product;
import com.example.Store.entity.ProductCategoryEnum;
import com.example.Store.entity.form.ProductForm;
import com.example.Store.entity.video.VideoOnPage;
import com.example.Store.service.ImageService;
import com.example.Store.service.ProductService;
import com.example.Store.service.VideoOnPageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FunctionalController {

    private static final String BASE_URL = "/product";
    private static final String BASE_URL_VIDEO = "/videoOnPage";
    private static final String BASE_URL_ADMIN = "/admin";

    private final ProductService productService;
    private final VideoOnPageService videoOnPageService;
    private final ImageService imageService;

    public FunctionalController(ProductService productService, VideoOnPageService videoOnPageService, ImageService imageService){
        this.productService = productService;
        this.videoOnPageService = videoOnPageService;
        this.imageService = imageService;
    }

    @Bean
    public RouterFunction<ServerResponse> getProductById(){
        return route()
                .GET(BASE_URL.concat("/byId/{id}"), request -> {
                    long id = Long.parseLong(request.pathVariable("id"));
                    return ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(productService.getById(id), Product.class);
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> getAllProduct(){
        return route()
                .GET(BASE_URL.concat("/allProduct"), request ->
                     ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(productService.findAll(), Product.class)
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> getAllTypeOne(){
        return route()
                .GET(BASE_URL.concat("/allTypeOne"), request ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(productService.findAllByCategory(ProductCategoryEnum.TYPE_ONE), Product.class)
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> getAllTypeTwo(){
        return route()
                .GET(BASE_URL.concat("/allTypeTwo"), request ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(productService.findAllByCategory(ProductCategoryEnum.TYPE_TWO), Product.class)
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> getAllOther(){
        return route()
                .GET(BASE_URL.concat("/allOther"), request ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(productService.findAllByCategory(ProductCategoryEnum.OTHER), Product.class)
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> deleteProductById(){
        return route()
                .GET(BASE_URL_ADMIN.concat("/delete/{id}"), request -> {
                    long id = Long.parseLong(request.pathVariable("id"));
                    return ServerResponse
                            .ok()
                            .body(productService.deleteProduct(id), Void.class);
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> saveOrUpdateProduct(){
        return route()
                .POST(BASE_URL_ADMIN.concat("/save"), request ->
                    request.body(toMono(ProductForm.class))
                            .doOnNext(productService::createRecordProduct)
                            .then(ServerResponse.ok().build())
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> getTopVideo(){
        return route()
                .GET(BASE_URL_VIDEO.concat("/top"), request ->
                    ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(videoOnPageService.getTopVideoOnThePage(), VideoOnPage.class)
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> getBottomVideo(){
        return route()
                .GET(BASE_URL_VIDEO.concat("/bottom"), request ->
                    ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(videoOnPageService.getBottomVideoOnThePage(), VideoOnPage.class)
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> getImageSource(){
        return route()
                .GET("/resource/images/{name}", request -> {
                    String imageName = request.pathVariable("name");
                    return ServerResponse.
                            ok()
                            .contentType(MediaType.IMAGE_PNG)
                            .body(imageService.getImage(imageName), byte[].class);
                })
                .build();
    }
}
