package com.ecommerce.site.shop.security.oauth2;

import com.ecommerce.common.model.entity.Customer;
import com.ecommerce.common.model.enums.AuthenticationType;
import com.ecommerce.site.shop.service.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(@NotNull HttpServletRequest request, HttpServletResponse response, @NotNull Authentication authentication) throws ServletException, IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        AuthenticationType authenticationType = getAuthenticationType(oAuth2User);
        String name = oAuth2User.getName();
        String email = oAuth2User.getEmail();
        String countryCode = request.getLocale().getCountry();

        Optional<Customer> optionalCustomer = customerService.findCustomerByEmail(email);
        if(optionalCustomer.isPresent()) {
            oAuth2User.setFullName(optionalCustomer.get().getFullName());
            customerService.updateAuthenticationType(optionalCustomer.get(), authenticationType);
        } else {
            customerService.addNewCustomerUponOAuthLogin(name, email, countryCode, authenticationType);
            response.sendRedirect("/ecommerce/customers/set_password");
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private AuthenticationType getAuthenticationType(@NotNull CustomOAuth2User principal) {
        String clientName = principal.getClientName();
        if("Facebook".equals(clientName)) {
            return AuthenticationType.FACEBOOK;
        }
        if("Google".equals(clientName)) {
            return AuthenticationType.GOOGLE;
        }
        return AuthenticationType.DATABASE;
    }
}
