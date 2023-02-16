package com.ecommerce.site.shop.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Nguyen Thanh Phuong
 */
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("../images/category-images", registry);
        exposeDirectory("../images/brand-logos", registry);
        exposeDirectory("../images/product-images", registry);
    }

    private void exposeDirectory(String pathPattern, @NotNull ResourceHandlerRegistry registry) {
        Path path = Paths.get(pathPattern);
        String absolutePath = path.toFile().getAbsolutePath();
        String logicalPath = pathPattern.replace("../", "") + "/**";

        registry.addResourceHandler(logicalPath).addResourceLocations("file:/" + absolutePath + "/");
    }

}
