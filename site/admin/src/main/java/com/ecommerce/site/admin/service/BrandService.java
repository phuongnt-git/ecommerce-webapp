package com.ecommerce.site.admin.service;


import com.ecommerce.site.admin.repository.BrandRepository;
import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.common.model.entity.Brand;
import com.ecommerce.common.exception.BrandNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static com.ecommerce.site.admin.constant.ApplicationConstant.BRANDS_PER_PAGE;

/**
 * @author Nguyen Thanh Phuong
 */
@Service
public class BrandService {

    @Autowired
    private BrandRepository repository;

    public List<Brand> listAll() {
        return repository.findAll();
    }

    public void listByPage(int pageNumber, @NotNull PagingAndSortingHelper helper) {
        helper.listEntities(pageNumber, BRANDS_PER_PAGE, repository);
    }

    public Brand save(Brand brand) {
        return repository.save(brand);
    }

    public Brand get(Integer id) throws BrandNotFoundException {
        try {
            return repository.findById(id).orElse(null);
        } catch (NoSuchElementException e) {
            throw new BrandNotFoundException(String.format("Could not found any brand with ID %s", id));
        }
    }

    public void delete(Integer id) throws BrandNotFoundException {
        Long countById = repository.countById(id);
        if (countById == null || countById == 0) {
            throw new BrandNotFoundException(String.format("Could not found any brand with ID %s", id));
        }

        repository.deleteById(id);
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = id == null;
        Brand brandByName = repository.findByName(name);

        if (isCreatingNew) {
            if (brandByName != null) {
                return "Duplicate";
            }
        } else {
            if (brandByName != null && !brandByName.getId().equals(id)) {
                return "Duplicate";
            }
        }

        return "OK";
    }

}
