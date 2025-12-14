package com.example.lifeline.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Ruta pública para acceder a las imágenes
    private static final String URL_PATH = "/uploads/images/**";

    // Ruta física absoluta donde se guardan las imágenes
    private static final String FILE_PATH =
            "file:" + System.getProperty("user.dir") + "/uploads/images/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler(URL_PATH)
                .addResourceLocations(FILE_PATH);
    }
}
