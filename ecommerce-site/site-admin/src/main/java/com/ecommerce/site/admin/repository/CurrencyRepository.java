package com.ecommerce.site.admin.repository;

import com.ecommerce.common.model.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @author Nguyen Thanh Phuong
 */
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    List<Currency> findAllByOrderByNameAsc();

}