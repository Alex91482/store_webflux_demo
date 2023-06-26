package com.example.Store.controller;

import com.example.Store.service.ImageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactor.core.publisher.Mono;

@Controller
public class ImageOnPageController {

    private final ImageService imageService;

    public ImageOnPageController(ImageService imageService){
        this.imageService = imageService;
    }

    @RequestMapping(value = "/resource/images/{name}", method = RequestMethod.GET)
    public Mono<byte[]> getImageSource(@PathVariable String name){
        return imageService.getImage(name);
    }
}
