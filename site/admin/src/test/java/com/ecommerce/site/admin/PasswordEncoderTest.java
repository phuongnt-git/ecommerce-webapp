package com.ecommerce.site.admin;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Nguyen Thanh Phuong
 */
public class PasswordEncoderTest {

    @Test
    public void testEncodePassword() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String password = "test";
        String encodedPassword = passwordEncoder.encode(password);

        System.out.println(encodedPassword);

        boolean matches = passwordEncoder.matches(password, encodedPassword);

        assertThat(matches).isTrue();
    }

}
