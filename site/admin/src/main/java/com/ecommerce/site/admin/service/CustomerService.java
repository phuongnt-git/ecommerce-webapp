package com.ecommerce.site.admin.service;


import com.ecommerce.site.admin.repository.CountryRepository;
import com.ecommerce.site.admin.repository.CustomerRepository;
import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.common.model.entity.Country;
import com.ecommerce.common.model.entity.Customer;
import com.ecommerce.common.exception.CustomerNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static com.ecommerce.site.admin.constant.ApplicationConstant.CUSTOMERS_PER_PAGE;

/**
 * @author Nguyen Thanh Phuong
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void listByPage(int pageNumber, @NotNull PagingAndSortingHelper helper) {
        helper.listEntities(pageNumber, CUSTOMERS_PER_PAGE, customerRepository);
    }

    public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
        customerRepository.updateEnabledStatus(id, enabled);
    }

    public Customer get(Integer id) throws CustomerNotFoundException {
        try {
            return customerRepository.findById(id).orElse(null);
        } catch (NoSuchElementException ex) {
            throw new CustomerNotFoundException(String.format("Could not find any customers with ID %s", id));
        }
    }

    public List<Country> listCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    public boolean checkEmailUnique(Integer id, String email) {
        Customer existCustomer = customerRepository.findByEmail(email);

        return existCustomer == null || existCustomer.getId().equals(id);
    }

    public void save(@NotNull Customer customerInForm) {
        Customer customerInDb = customerRepository.findById(customerInForm.getId()).orElse(null);

        if (!customerInForm.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
            customerInForm.setPassword(encodedPassword);
        } else {
            customerInForm.setPassword(Objects.requireNonNull(customerInDb).getPassword());
        }

        customerInForm.setEnabled(Objects.requireNonNull(customerInDb).isEnabled());
        customerInForm.setCreatedTime(customerInDb.getCreatedTime());
        customerInForm.setVerificationCode(customerInDb.getVerificationCode());
        customerInForm.setAuthenticationType(customerInDb.getAuthenticationType());
        customerInForm.setResetPasswordToken(customerInDb.getResetPasswordToken());

        customerRepository.save(customerInForm);
    }

    public void delete(Integer id) throws CustomerNotFoundException {
        Long count = customerRepository.countById(id);
        if (count == null || count == 0) {
            throw new CustomerNotFoundException(String.format("Could not find any customers with ID %s", id));
        }

        customerRepository.deleteById(id);
    }

}
