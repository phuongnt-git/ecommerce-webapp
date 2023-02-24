package com.ecommerce.site.admin.rest;

import com.ecommerce.site.admin.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nguyen Thanh Phuong
 */
@RestController
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("categories/check-unique")
    public String checkUnique(@Param("id") Integer id,
                              @Param("name") String name,
                              @Param("alias") String alias) {
        return categoryService.checkUnique(id, name, alias);
    }

}
