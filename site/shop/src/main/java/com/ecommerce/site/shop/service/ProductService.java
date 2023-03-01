package com.ecommerce.site.shop.service;

import com.ecommerce.common.exception.ProductNotFoundException;
import com.ecommerce.common.model.entity.Product;
import com.ecommerce.site.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Nguyen Thanh Phuong
 */
@Service
public class ProductService {

    public static final int PRODUCTS_PER_PAGE = 10;

    public static final int SEARCH_RESULTS_PER_PAGE = 10;

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> listByCategory(int pageNum, Integer categoryId) {
        String categoryIdMatch = "-" + categoryId + "-";
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);

        return productRepository.listByCategory(categoryId, categoryIdMatch, pageable);
    }

    public Product getProduct(String alias) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findByAlias(alias);

        if (product.isPresent()) {
            return product.get();
        }

        throw new ProductNotFoundException(String.format("Could not find any products with alias %s", alias));
    }

    public Page<Product> search(String keyword, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE);

        return productRepository.search(keyword, pageable);
    }

}
