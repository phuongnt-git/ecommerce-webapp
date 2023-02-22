package com.ecommerce.site.admin.service;

import com.ecommerce.common.exception.UserNotFoundException;
import com.ecommerce.common.model.entity.Role;
import com.ecommerce.common.model.entity.User;
import com.ecommerce.site.admin.repository.RoleRepository;
import com.ecommerce.site.admin.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "/application-test.properties")
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Value("${sql.script.insert.user}")
    private String sqlInsertUser;

    @Value("${sql.script.delete.user}")
    private String sqlDeleteUser;

    @Value("${sql.script.delete.user-role}")
    private String sqlDeleteUserRole;

    @Value("${sql.script.delete.role}")
    private String sqlDeleteRole;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlInsertUser);
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteUserRole);
        jdbc.execute(sqlDeleteUser);
        jdbc.execute(sqlDeleteRole);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindUser() {
        User user = userService.findByEmail("test@gmail.com");

        assertThat(user).isNotNull();
    }

    @Test
    public void testCheckUserExists() {
        Assertions.assertTrue(userService.checkUserExists(1), "@BeforeTransaction creates user : return true");
        Assertions.assertFalse(userService.checkUserExists(0), "No user have id 0: return false");
    }

    @Test
    @Rollback
    public void testDeleteUser() throws UserNotFoundException {
        Optional<User> user = userRepository.findById(1);
        Assertions.assertTrue(user.isPresent(), "return true");

        userService.deleteById(1);

        user = userRepository.findById(1);
        Assertions.assertFalse(user.isPresent(), "return false");
    }


    @Test
    @SqlGroup({@Sql(scripts = "/script/data.sql")})
    public void testGetUserRoles() {
        Optional<User> user = userRepository.findById(2);
        if (user.isPresent()) {
            for (Role role : user.get().getRoles()) {
                System.out.println(role.getName());
            }

            Assertions.assertEquals(3, user.get().getRoles().size());
        }
    }

}
