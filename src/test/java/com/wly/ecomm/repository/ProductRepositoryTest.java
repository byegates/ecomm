package com.wly.ecomm.repository;

import com.wly.ecomm.model.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // enables h2 database directly
@ComponentScan(basePackages = "com.wly.ecomm.*")
@Transactional
class ProductRepositoryTest {
    private final ProductRepository productRepository;

    @Autowired
    public ProductRepositoryTest(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Test
    @Disabled
    public void givenNA_whenStartOfApplication_then8ProductsSaved() {
        assertEquals(8, productRepository.findAll().size());
    }

    @Test
    public void givenProductObject_whenSave_thenSavedProductEqualsOriginalProduct() {
        Product product = new Product("PIXEL 7 PRO 128GB", 899.00);
        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct);
        assertNotNull(savedProduct.getId());
        assertInstanceOf(Integer.class, savedProduct.getId());
        assertTrue(savedProduct.getId() > 0);

        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice());
    }
}