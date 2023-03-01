package com.ecommerce.site.admin.config;

import com.ecommerce.site.admin.paging.PagingAndSortingArgumentResolver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Nguyen Thanh Phuong
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        exposeDirectory("../images", registry);
    }

    @Override
    public void addArgumentResolvers(@NotNull List<HandlerMethodArgumentResolver> resolver) {
        resolver.add(new PagingAndSortingArgumentResolver());
    }

    private void exposeDirectory(String pathPattern, @NotNull ResourceHandlerRegistry registry) {
        Path path = Paths.get(pathPattern);
        String absolutePath = path.toFile().getAbsolutePath();
        String logicalPath = pathPattern.replace("../", "") + "/**";

        registry.addResourceHandler(logicalPath).addResourceLocations("file:" + absolutePath + "/");
    }

}
