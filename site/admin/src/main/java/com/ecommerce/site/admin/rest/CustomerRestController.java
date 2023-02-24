package com.ecommerce.site.admin.rest;

import com.ecommerce.site.admin.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nguyen Thanh Phuong
 */
@RestController
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/customers/check-email")
    public String checkDuplicateEmail(Integer id, String email) {
        if (customerService.checkEmailUnique(id, email)) {
            return "OK";
        } else {
            return "Duplicated";
        }
    }

}
