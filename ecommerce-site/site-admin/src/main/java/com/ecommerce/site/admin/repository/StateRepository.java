package com.ecommerce.site.admin.repository;

import com.ecommerce.common.model.entity.Country;
import com.ecommerce.common.model.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @author Nguyen Thanh Phuong
 */
@Repository
public interface StateRepository extends JpaRepository<State, Integer> {

    List<State> findByCountryOrderByNameAsc(Country country);

}