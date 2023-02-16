package com.ecommerce.site.shop.repository;


import com.ecommerce.common.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Integer> {

    List<Country> findAllByOrderByName();

    Country findByCode(String countryCode);
}
