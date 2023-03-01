package com.ecommerce.site.shop.rest;


import com.ecommerce.common.model.entity.Country;
import com.ecommerce.common.model.entity.State;
import com.ecommerce.site.shop.dto.StateDto;
import com.ecommerce.site.shop.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StateRestController {

    @Autowired
    private StateRepository stateRepository;

    @GetMapping("/settings/list_states_by_country/{id}")
    public List<StateDto> listByCountry(@PathVariable("id") Integer countryId) {
        List<State> listStates = stateRepository.findByCountryOrderByNameAsc(new Country(countryId));
        List<StateDto> result = new ArrayList<>();

        for (State state : listStates) {
            result.add(new StateDto(state.getId(), state.getName()));
        }

        return result;
    }

}
