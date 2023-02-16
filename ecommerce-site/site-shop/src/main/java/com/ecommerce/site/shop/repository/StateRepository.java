package com.ecommerce.site.shop.repository;

import com.ecommerce.common.model.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Integer> {

    List<State> findAllByCountry_IdOrderByName(Integer country_id);
}
