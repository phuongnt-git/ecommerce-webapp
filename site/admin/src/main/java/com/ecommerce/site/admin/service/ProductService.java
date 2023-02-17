package com.ecommerce.site.admin.service;

import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.site.admin.repository.ProductRepository;
import com.ecommerce.common.model.entity.Product;
import com.ecommerce.common.exception.ProductNotFoundException;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static com.ecommerce.site.admin.constant.ApplicationConstant.PRODUCTS_PER_PAGE;

/**
 * @author Nguyen Thanh Phuong
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listAll() {
        return productRepository.findAll();
    }

    public void listByPage(int pageNumber, @NotNull PagingAndSortingHelper helper, Integer categoryId) {
        Pageable pageable = helper.createPageable(PRODUCTS_PER_PAGE, pageNumber);
        String keyword = helper.getKeyword();
        Page<Product> page;

        if (keyword != null && !keyword.isEmpty()) {
            if (categoryId != null && categoryId > 0) {
                String categoryIdMatch = "-" + categoryId + "-";
                page = productRepository.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
            } else {
                page = productRepository.findAll(keyword, pageable);
            }
        } else {
            if (categoryId != null && categoryId > 0) {
                String categoryIdMatch = "-" + categoryId + "-";
                page = productRepository.findAllInCategory(categoryId, categoryIdMatch, pageable);
            } else {
                page = productRepository.findAll(pageable);
            }
        }

        helper.updateModelAttributes(pageNumber, page);
    }

    public void searchProducts(int pageNum, @NotNull PagingAndSortingHelper helper) {
        Pageable pageable = helper.createPageable(PRODUCTS_PER_PAGE, pageNum);
        String keyword = helper.getKeyword();
        Page<Product> page = productRepository.searchProductsByName(keyword, pageable);
        helper.updateModelAttributes(pageNum, page);
    }

    public Product save(@NotNull Product product) {
        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }

        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            String defaultAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }

        product.setUpdatedTime(new Date());

        Product updatedProduct = productRepository.save(product);
        // repository.updateReviewCountAndAverageRating(updatedProduct.getId());

        return updatedProduct;
    }

    public void saveProductPrice(@NotNull Product productInForm) {
        Product productInDb = productRepository.findById(productInForm.getId()).orElse(null);
        if (productInDb != null) {
            productInDb.setCost(productInForm.getCost());
            productInDb.setPrice(productInForm.getPrice());
            productInDb.setDiscountPercent(productInForm.getDiscountPercent());

            productRepository.save(productInDb);
        }
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);
        Product productByName = productRepository.findByName(name);

        if (isCreatingNew) {
            if (productByName != null) {
                return "Duplicate";
            }
        } else {
            if (productByName != null && !productByName.getId().equals(id)) {
                return "Duplicate";
            }
        }

        return "OK";
    }

    public void updateEnabledStatus(Integer id, boolean enabled) {
        productRepository.updateEnabledStatus(id, enabled);
    }

    public void delete(Integer id) throws ProductNotFoundException {
        Long countById = productRepository.countById(id);

        if (countById == null || countById == 0) {
            throw new ProductNotFoundException(String.format("Could not find any product with ID %s", id));
        }

        productRepository.deleteById(id);
    }

    public Product get(Integer id) throws ProductNotFoundException {
        try {
            return productRepository.findById(id).orElse(null);
        } catch (NoSuchElementException e) {
            throw new ProductNotFoundException(String.format("Could not find any product with ID %s", id));
        }
    }

}
