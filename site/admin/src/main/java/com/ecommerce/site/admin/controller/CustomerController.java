package com.ecommerce.site.admin.controller;

import com.ecommerce.common.exception.CustomerNotFoundException;
import com.ecommerce.common.model.entity.Country;
import com.ecommerce.common.model.entity.Customer;
import com.ecommerce.common.model.entity.State;
import com.ecommerce.site.admin.annotation.PagingAndSortingParam;
import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.site.admin.repository.StateRepository;
import com.ecommerce.site.admin.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * @author Nguyen Thanh Phuong
 */
@Controller
public class CustomerController {

    private final String defaultRedirectUrl = "redirect:/customers/page/1?sortField=firstName&sortDir=asc";

    @Autowired
    private CustomerService customerService;

    @Autowired
    private StateRepository stateRepository;

    @GetMapping("/customers")
    public String listFirstPage() {
        return defaultRedirectUrl;
    }

    @GetMapping("/customers/page/{pageNumber}")
    public String listByPage(
            @PagingAndSortingParam(listName = "listCustomers", moduleUrl = "/customers") PagingAndSortingHelper helper,
            @PathVariable("pageNumber") int pageNumber) {
        customerService.listByPage(pageNumber, helper);

        return "customer/customers";
    }

    @GetMapping("/customers/{id}/enabled/{status}")
    public String updateCustomerEnabledStatus(@PathVariable("id") Integer id,
                                              @PathVariable("status") boolean enabled,
                                              @NotNull RedirectAttributes attributes,
                                              @NotNull HttpServletRequest request) {
        customerService.updateCustomerEnabledStatus(id, enabled);
        attributes.addFlashAttribute(String.format("The customer ID %s has been %s", id, enabled ? "enabled" : "disabled"));

        return String.format("redirect:%s", request.getHeader("Referer"));
    }

    @GetMapping("/customers/detail/{id}")
    public String viewCustomer(@PathVariable("id") Integer id,
                               @NotNull Model model,
                               RedirectAttributes attributes) {
        try {
            Customer customer = customerService.get(id);
            model.addAttribute("customer", customer);

            return "customer/customer_detail_modal";
        } catch (CustomerNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());

            return defaultRedirectUrl;
        }
    }

    @GetMapping("/customers/edit/{id}")
    public String editCustomer(@PathVariable("id") Integer id,
                               @NotNull Model model,
                               RedirectAttributes attributes) {
        try {
            Customer customer = customerService.get(id);
            List<Country> listCountries = customerService.listCountries();
            List<State> listStates = stateRepository.findByCountryOrderByNameAsc(new Country().toBuilder().id(id).build());

            model.addAttribute("listCountries", listCountries);
            model.addAttribute("listStates", listStates);
            model.addAttribute("customer", customer);
            model.addAttribute("pageTitle", String.format("Edit Customer (ID: %d)", id));

            return "customer/customer_form";
        } catch (CustomerNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());

            return defaultRedirectUrl;
        }
    }

    @PostMapping("/customers/save")
    public String saveCustomer(Customer customer,
                               @NotNull RedirectAttributes attributes) {
        customerService.save(customer);
        attributes.addFlashAttribute("message", String.format("The customer ID %s has been updated successfully", customer.getId()));

        return defaultRedirectUrl;
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable Integer id,
                                 @NotNull RedirectAttributes attributes,
                                 @NotNull HttpServletRequest request) {
        try {
            customerService.delete(id);
            attributes.addFlashAttribute("message", String.format("The customer ID %s has been deleted successfully", id));

        } catch (CustomerNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }

        return String.format("redirect:%s", request.getHeader("Referer"));
    }

}
