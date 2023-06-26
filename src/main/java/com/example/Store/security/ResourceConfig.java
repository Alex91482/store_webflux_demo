package com.example.Store.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class ResourceConfig implements WebFluxConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/styles/**")
                .addResourceLocations("classpath:/static/styles/");
        registry
                .addResourceHandler("static/styles/**")
                .addResourceLocations("classpath:/static/styles/");
        registry
                .addResourceHandler("/images/**")
                .addResourceLocations("classpath:/images/");
        registry
                .addResourceHandler("/video/**")
                .addResourceLocations("classpath:/video/");
        registry
                .addResourceHandler("/static/js/**")
                .addResourceLocations("classpath:/static/js/");
    }
}
