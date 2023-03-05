package com.ecommerce.site.admin.repository;

import com.ecommerce.common.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Nguyen Thanh Phuong
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, CustomPagingAndSortingRepository<Order, Integer> {

    @Query("SELECT o FROM Order o WHERE o.firstName LIKE %?1% OR"
            + " o.lastName LIKE %?1% OR o.phoneNumber LIKE %?1% OR"
            + " o.addressLine1 LIKE %?1% OR o.addressLine2 LIKE %?1% OR"
            + " o.postalCode LIKE %?1% OR o.city LIKE %?1% OR"
            + " o.state LIKE %?1% OR o.country LIKE %?1% OR"
            + " o.paymentMethod LIKE %?1% OR o.status LIKE %?1% OR"
            + " o.customer.firstName LIKE %?1% OR"
            + " o.customer.lastName LIKE %?1%")
    Page<Order> findAll(String keyword, Pageable pageable);

    Long countById(Integer id);

}