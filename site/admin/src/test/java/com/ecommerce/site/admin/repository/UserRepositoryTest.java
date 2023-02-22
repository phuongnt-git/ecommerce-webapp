package com.ecommerce.site.admin.repository;


import com.ecommerce.common.model.entity.Role;
import com.ecommerce.common.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Nguyen Thanh Phuong
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUserTable() {
    }

    @Test
    public void testCreateUserWithOneRole() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User userAdmin = new User()
                .toBuilder()
                .email("admin@gmail.com")
                .password("admin@123")
                .firstName("")
                .lastName("Admin")
                .build();

        userAdmin.addRole(roleAdmin);

        User savedUser = repository.save(userAdmin);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateUserWithManyRoles() {
        User user = new User()
                .toBuilder()
                .email("test@gmail.com")
                .password("test@123")
                .firstName("test")
                .lastName("test")
                .build();

        Role roleEditor = new Role().toBuilder().id(3).build();
        Role roleAssistant = new Role().toBuilder().id(5).build();

        user.addRole(roleEditor);
        user.addRole(roleAssistant);

        User savedUser = repository.save(user);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        List<User> userList = repository.findAll();
        userList.forEach(System.out::println);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void testGetUserById() {
        User userById = repository.findById(1).get();
        System.out.println(userById);

        assertThat(userById).isNotNull();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void testUpdateUserDetail() {
        User userById = repository.findById(1).get();
        userById.setEnabled(true);

        repository.save(userById);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void testUpdateUserRoles() {
        User userById = repository.findById(4).get();
        Role roleEditor = new Role().toBuilder().id(3).build();
        Role roleSalesperson = new Role().toBuilder().id(2).build();

        userById.getRoles().remove(roleEditor);
        userById.addRole(roleSalesperson);

        repository.save(userById);
    }

    @Test
    public void testDeleteUser() {
        repository.deleteById(2);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "admin@gmail.com";
        User user = repository.findByEmail(email);

        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById() {
        Long countById = repository.countById(1);

        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser() {
        repository.updateEnabledStatus(1, false);
    }

    @Test
    public void testEnableUser() {
        repository.updateEnabledStatus(3, true);
    }

    @Test
    public void testListFirstPage() {
        int pageNumber = 0;
        int pageSize = 5;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repository.findAll(pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(System.out::println);

        assertThat(listUsers.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUser() {
        String keyword = "gmail";

        int pageNumber = 0;
        int pageSize = 5;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repository.findAll(keyword, pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(System.out::println);

        assertThat(listUsers.size()).isGreaterThan(0);
    }

}
