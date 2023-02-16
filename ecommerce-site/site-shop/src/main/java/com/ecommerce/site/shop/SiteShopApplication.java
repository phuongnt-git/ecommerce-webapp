package com.ecommerce.site.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.ecommerce.common.model.entity")
public class SiteShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiteShopApplication.class, args);
    }

}
