package com.ecommerce.site.admin.repository;


import com.ecommerce.common.model.entity.Brand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Nguyen Thanh Phuong
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTest {

    @Autowired
    private BrandRepository repository;

    @Test
    public void testFindAll() {
        Iterable<Brand> iterable = repository.findAll();
        iterable.forEach(System.out::println);

        assertThat(iterable).isNotEmpty();
    }

    @Test
    public void testFindById() {
        Brand brand = repository.findById(1).orElse(null);

        assert brand != null;
    }

    @Test
    public void testUpdateName() {
        Brand test = repository.save(new Brand("test"));

        String newTest = "new test";
        test.setName(newTest);

        assertThat(test.getName()).isEqualTo(newTest);
    }

    @Test
    public void testDelete() {
        Brand brand = repository.save(new Brand("test"));
        repository.deleteById(brand.getId());

        Optional<Brand> result = repository.findById(brand.getId());

        assertThat(result.isEmpty());
    }

}
