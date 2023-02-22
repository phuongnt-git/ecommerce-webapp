package com.ecommerce.site.admin.repository;


import com.ecommerce.common.model.entity.Brand;
import com.ecommerce.common.model.entity.Category;
import com.ecommerce.common.model.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Nguyen Thanh Phuong
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateProduct() {
        Brand brand = entityManager.find(Brand.class, 37);
        Category category = entityManager.find(Category.class, 5);

        Product product = new Product();
        product.setName("Acer Aspire Desktop");
        product.setAlias("acer_aspire_desktop");
        product.setShortDescription("Short description for Acer Aspire");
        product.setFullDescription("Full description for Acer Aspire");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(678);
        product.setCost(600);
        product.setEnabled(true);
        product.setInStock(true);

        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        product.setMainImage("image-thumbnail.png");

        Product savedProduct = repository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllProducts() {
        Iterable<Product> productList = repository.findAll();

        productList.forEach(System.out::println);
    }

    @Test
    public void testGetProduct() {
        Product product = repository.findById(1).orElse(null);
        System.out.println(product);

        assertThat(product).isNotNull();
    }

    @Test
    public void testUpdateProduct() {
        Product product = repository.findById(1).orElse(null);
        if (product != null) {
            product.setPrice(499);
            repository.save(product);
        }

        Product updatedProduct = entityManager.find(Product.class, 1);

        assertThat(updatedProduct.getPrice()).isEqualTo(499);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testDeleteProduct() {
        repository.deleteById(1);

        Optional<Product> result = repository.findById(1);

        assertThat(result.isEmpty());
    }

    @Test
    public void testSaveProductWithImages() {
        Product product = repository.findById(1).orElse(null);

        if (product != null) {
            product.addExtraImage("extra-image-1.png");
            product.addExtraImage("extra-image-2.png");
            product.addExtraImage("extra-image-3.png");

            Product savedProduct = repository.save(product);

            assertThat(savedProduct.getImages().size()).isEqualTo(3);
        }
    }

    @Test
    public void testSaveProductWithDetails() {
        Integer productId = 1;
        Product product = repository.findById(productId).orElse(null);

        if (product != null) {
            product.addDetail("Device Memory", "128 GB");
            product.addDetail("CPU Model", "MediaTek");
            product.addDetail("OS", "Android 10");

            Product savedProduct = repository.save(product);
            assertThat(savedProduct.getDetails()).isNotEmpty();
        }
    }

    /*@Test
    public void testUpdateReviewCountAndAverageRating() {
        Integer productId = 100;
        repository.updateReviewCountAndAverageRating(productId);
    }*/


}
