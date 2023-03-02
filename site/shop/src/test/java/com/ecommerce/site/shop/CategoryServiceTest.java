package com.ecommerce.site.shop;

import com.ecommerce.common.model.entity.Category;
import com.ecommerce.site.shop.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Nguyen Thanh Phuong
 */
@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void testListNoChildrenCategories() {
        List<Category> categoryList = categoryService.listNoChildrenCategories();
        categoryList.forEach(category -> System.out.println(category.getName()));
    }

}
