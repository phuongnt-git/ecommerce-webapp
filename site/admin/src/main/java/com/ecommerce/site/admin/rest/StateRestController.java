package com.ecommerce.site.admin.rest;

import com.ecommerce.site.admin.repository.StateRepository;
import com.ecommerce.common.model.dto.StateDto;
import com.ecommerce.common.model.entity.Country;
import com.ecommerce.common.model.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nguyen Thanh Phuong
 */
@RestController
public class StateRestController {

    @Autowired
    private StateRepository repository;

    @GetMapping("/states/list-by-country/{id}")
    public List<StateDto> listByCountry(@PathVariable("id") Integer countryId) {
        List<State> listStates = repository.findByCountryOrderByNameAsc(new Country().toBuilder().id(countryId).build());
        List<StateDto> result = new ArrayList<>();

        for (State state : listStates) {
            result.add(new StateDto(state.getId(), state.getName()));
        }
        return result;
    }

    @PostMapping("/states/save")
    public String save(@RequestBody State state) {
        State savedState = repository.save(state);
        return String.valueOf(savedState.getId());
    }

    @DeleteMapping("/states/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        repository.deleteById(id);
    }

}
