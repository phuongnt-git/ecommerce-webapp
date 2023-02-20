package com.ecommerce.site.admin.service;

import com.ecommerce.common.model.entity.User;
import com.ecommerce.site.admin.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Value("${sql.script.insert.user}")
    private String sqlInsertUser;

    @Value("${sql.script.delete.user}")
    private String sqlDeleteUser;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlInsertUser);
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteUser);
    }

    @Mock
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void getUserByEmailService() {
        User user = userService.findByEmail("admin@gmail.com");

        assertThat(user).isNotNull();
    }

}
