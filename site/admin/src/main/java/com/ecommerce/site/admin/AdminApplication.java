package com.ecommerce.site.admin;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Nguyen Thanh Phuong
 */
@EnableJpaRepositories("com.ecommerce.site.admin.repository")
@EntityScan("com.ecommerce.common.model")
@EncryptablePropertySource(name = "EncryptedProperties", value = "classpath:encrypted.properties")
@SpringBootApplication
@EnableAspectJAutoProxy
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
