package com.ecommerce.site.admin.security;

import com.ecommerce.site.admin.service.UserService;
import com.ecommerce.common.model.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Nguyen Thanh Phuong
 */
@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @NotNull Authentication authentication) throws IOException, ServletException {
        UserDetailsImpl userDetails =  (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        if (user.getFailedAttempt() > 0) {
            userService.resetFailedAttempts(user);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
