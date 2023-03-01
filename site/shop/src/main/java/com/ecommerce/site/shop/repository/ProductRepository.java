package com.ecommerce.site.shop.repository;

import com.ecommerce.common.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Nguyen Thanh Phuong
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.enabled = TRUE AND (p.category.id = ?1 OR p.category.allParentIds LIKE %?2%) ORDER BY p.name ASC")
    Page<Product> listByCategory(Integer categoryId, String categoryIdMatch, Pageable pageable);

    Optional<Product> findByAlias(String alias);

    /*@Query(value = "SELECT * FROM products WHERE enabled = true AND MATCH(name, short_description, full_description) AGAINST (?1)", nativeQuery = true)*/
    @Query("SELECT p FROM Product p WHERE p.enabled = TRUE AND CONCAT(p.name, p.shortDescription, p.fullDescription) LIKE %?1%")
    Page<Product> search(String keyword, Pageable pageable);

}