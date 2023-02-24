package com.ecommerce.site.admin.rest;

import com.ecommerce.site.admin.service.ProductService;
import com.ecommerce.common.model.dto.ProductDto;
import com.ecommerce.common.model.entity.Product;
import com.ecommerce.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nguyen Thanh Phuong
 */
@RestController
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products/check-unique")
    public String checkUnique(Integer id, String name) {
        return productService.checkUnique(id, name);
    }

    @GetMapping("/products/get/{id}")
    public ProductDto getProductInfo(@PathVariable("id") Integer id) throws ProductNotFoundException {
        Product product = productService.get(id);
        return new ProductDto(product.getName(), product.getMainImagePath(), product.getDiscountPrice(), product.getCost());
    }

}
