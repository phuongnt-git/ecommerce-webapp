package com.ecommerce.site.shop.rest;


import com.ecommerce.site.shop.dto.StateDTO;
import com.ecommerce.site.shop.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StateRestController {

    @Autowired
    private StateRepository stateRepository;

    @GetMapping("/loadStatesByCountry/{countryId}")
    public ResponseEntity<?> getStatesByCountryId(@PathVariable(name = "countryId") Integer countryId) {
        List<StateDTO> stateList =
                stateRepository.findAllByCountry_IdOrderByName(countryId)
                        .stream()
                        .map(s -> new StateDTO(s.getName()))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(stateList);
    }
}
