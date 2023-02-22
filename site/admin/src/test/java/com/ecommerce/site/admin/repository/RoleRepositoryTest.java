package com.ecommerce.site.admin.repository;


import com.ecommerce.common.model.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Nguyen Thanh Phuong
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository repository;

    @Test
    @Rollback
    public void testCreateFirstRole() {
        Role roleAdmin = new Role()
                .toBuilder()
                .name("Admin")
                .description("Manage everything")
                .build();
        Role savedRole = repository.save(roleAdmin);

        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    @Rollback
    public void testCreateRestRoles() {
        Role roleSalesperson = new Role()
                .toBuilder()
                .name("Salesperson")
                .description("Manage product price, customers, shipping, orders and sales report")
                .build();
        Role roleEditor = new Role()
                .toBuilder()
                .name("Editor")
                .description("Manage category, brands, products, articles and menus")
                .build();
        Role roleShipper = new Role()
                .toBuilder()
                .name("Shipper")
                .description("View products, view orders and update order status")
                .build();
        Role roleAssistant = new Role()
                .toBuilder()
                .name("Assistant")
                .description("Manage questions and reviews")
                .build();

        repository.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));
    }

}
