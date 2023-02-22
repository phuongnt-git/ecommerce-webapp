package com.ecommerce.site.admin.config;

import com.ecommerce.site.admin.security.CustomCsrfTokenRepository;
import com.ecommerce.site.admin.security.CustomLoginFailureHandler;
import com.ecommerce.site.admin.security.CustomLoginSuccessHandler;
import com.ecommerce.site.admin.security.CustomUserDetailsServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRepository;

/**
 * @author Nguyen Thanh Phuong
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public CustomLoginFailureHandler loginFailureHandler() {
        return new CustomLoginFailureHandler();
    }

    @Bean
    public CustomLoginSuccessHandler loginSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new CustomCsrfTokenRepository();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(@NotNull AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(@NotNull HttpSecurity http) throws Exception {
        /*http.csrf(c -> c.csrfTokenRepository(csrfTokenRepository()));*/
        http.csrf().disable();

        http.authorizeHttpRequests()
                .requestMatchers("/css/**", "/img/**", "/js/**", "/webjars/**", "/vendors/**").permitAll()
                .requestMatchers("/users/**", "/settings/**", "/countries/**", "/states/**").hasAuthority("Admin")
                .requestMatchers("/categories/**", "/brands/**", "/articles/**", "/sections/**").hasAnyAuthority("Admin", "Editor")
                .requestMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editor")
                .requestMatchers("/products/edit/**", "/products/save", "/products/check-unique").hasAnyAuthority("Admin", "Editor", "Salesperson")
                .requestMatchers("/products", "/products/", "/products/detail/**", "/products/page/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
                .requestMatchers("/products/**", "/menus/**", "/articles/**").hasAnyAuthority("Admin", "Editor")
                .requestMatchers("/products/detail/**", "/customers/detail/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Assistant")
                .requestMatchers("/customers/**", "/orders/**", "/get_shipping_cost", "/reports/**").hasAnyAuthority("Admin", "Salesperson")
                .requestMatchers("/states/list_by_country/**").hasAnyAuthority("Admin", "Salesperson")
                .requestMatchers("/orders", "/orders/", "/orders/page/**", "/orders/detail/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")
                .requestMatchers("/orders_shipper/update/**").hasAuthority("Shipper")
                .requestMatchers("/reviews/**", "/questions/**").hasAnyAuthority("Admin", "Assistant")
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers(EndpointRequest.to(HealthEndpoint.class)).permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
                .anyRequest()
                .authenticated();

        http.formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .failureHandler(loginFailureHandler())
                .successHandler(loginSuccessHandler())
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();

        http.rememberMe()
                .userDetailsService(userDetailsService())
                .key("MyRememberMeKey")
                .tokenValiditySeconds(7 * 24 * 60 * 60);

        http.httpBasic();

        http.authenticationProvider(daoAuthenticationProvider());

        return http.build();
    }

}
