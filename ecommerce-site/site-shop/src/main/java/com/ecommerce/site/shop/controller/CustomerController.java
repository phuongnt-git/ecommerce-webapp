package com.ecommerce.site.shop.controller;


import com.ecommerce.common.exception.UserNotFoundException;
import com.ecommerce.common.model.entity.Country;
import com.ecommerce.common.model.entity.Customer;
import com.ecommerce.common.model.entity.State;
import com.ecommerce.site.shop.security.CustomerUserDetails;
import com.ecommerce.site.shop.security.oauth2.CustomOAuth2User;
import com.ecommerce.site.shop.service.CustomerService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/register")
    public String customerRegister(ModelMap model) {
        Customer customer = new Customer();
        List<Country> countryList = customerService.findAllCountry();

        model.put("customer", customer);
        model.put("countryList", countryList);
        model.put("pageTitle", "Registration");
        return "customer/register_form";
    }

    @GetMapping("/verify/{verifyCode}")
    public String verifyCustomer(@PathVariable String verifyCode, ModelMap model) {
        boolean verifyCustomer = customerService.verifyCustomer(verifyCode);
        if(verifyCustomer) {
            model.put("pageTitle", "Verify successfully");
            return "customer/verify_success";
        }
        model.put("pageTitle", "Verify failed");
        return "customer/verify_fail";
    }

    @PostMapping ("/create")
    public String createCustomer(Customer customer,
                                 HttpServletRequest request,
                                 ModelMap model) throws MessagingException {
        model.put("pageTitle", "Congratulations!!");
        customerService.registerCustomer(customer, request);
        return "customer/register_success";
    }

    @GetMapping("/account")
    public String accountEdit(ModelMap model, HttpServletRequest request) {
        String email = getEmailOfAuthenticatedCustomer(request);

        Optional<Customer> customerByEmail = customerService.findCustomerByEmail(email);
        List<Country> countryList = customerService.findAllCountry();
        List<State> stateList = customerService.findStateByCountry(customerByEmail.get().getCountry().getId());

        model.put("customer", customerByEmail.get());
        model.put("countryList", countryList);
        model.put("stateList", stateList);
        model.put("pageTitle", "Edit your account information");
        return "/customer/account_form";
    }

    @PostMapping("/account_update")
    public String saveUpdateAccountInformation(HttpServletRequest request, Customer customer) {
        customerService.updateAccountInformation(customer);
        updateNameForAuthenticatedCustomer(request, customer);
        return "home";
    }

    @GetMapping("/set_password")
    public String setPasswordForOAuthUser(ModelMap model) {
        model.put("pageTitle", "Set your account password");
        return "customer/set_password_oauth2user";
    }

    @PostMapping("/set_password")
    public String processNewPasswordForOAuthUser(HttpServletRequest request, ModelMap model) {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) request.getUserPrincipal();
        CustomOAuth2User oAuth2User = (CustomOAuth2User) oAuth2AuthenticationToken.getPrincipal();
        String email = oAuth2User.getEmail();
        String password = request.getParameter("password");
        try {
            customerService.setPasswordNewOAuthUser(email, password);
        } catch (UserNotFoundException e) {
            model.put("pageTitle", e.getMessage());
            model.put("error", true);
            model.put("message", e.getMessage());
            return "customer/message";
        }
        return "redirect:/";
    }

    private void updateNameForAuthenticatedCustomer(HttpServletRequest request, Customer customer) {
        Principal userPrincipal = request.getUserPrincipal();
        String fullName = customer.getFirstName() + " " + customer.getLastName();
        if(userPrincipal instanceof UsernamePasswordAuthenticationToken ||
                userPrincipal instanceof RememberMeAuthenticationToken) {
            CustomerUserDetails userDetails = getCustomerUserDetails(userPrincipal);

            Customer authenticatedCustomer = userDetails.getCustomer();
            authenticatedCustomer.setFirstName(customer.getFirstName());
            authenticatedCustomer.setLastName(customer.getLastName());
        } else if (userPrincipal instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) oAuth2AuthenticationToken.getPrincipal();
            oAuth2User.setFullName(fullName);
        }
    }

    private CustomerUserDetails getCustomerUserDetails(Principal principal) {
        CustomerUserDetails userDetails;
        if(principal instanceof UsernamePasswordAuthenticationToken token) {
            userDetails = (CustomerUserDetails) token.getPrincipal();
        } else {
            var token = (RememberMeAuthenticationToken) principal;
            userDetails = (CustomerUserDetails) token.getPrincipal();
        }
        return userDetails;
    }

    private String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
        Principal userPrincipal = request.getUserPrincipal();
        String email = "";
        if(userPrincipal instanceof UsernamePasswordAuthenticationToken ||
        userPrincipal instanceof RememberMeAuthenticationToken) {
            email = userPrincipal.getName();
        } else if (userPrincipal instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) oAuth2AuthenticationToken.getPrincipal();
            email = oAuth2User.getEmail();
        }
        return email;
    }
}
