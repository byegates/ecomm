package com.wly.ecomm.service;

import com.wly.ecomm.model.Product;
import com.wly.ecomm.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceWithMockitoTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("PIXEL 7 PRO 128GB", 899.00);
    }

    @Test
    void findById() {
        given(productRepository.findById(any(Integer.TYPE))).willReturn(Optional.ofNullable(product));
        Product foundProduct = productService.findById(1);
        assertNotNull(foundProduct);
    }

    @Test
    void save() {
        given(productRepository.save(product)).willReturn(product);
        Product savedProduct = productService.save(product);
        assertNotNull(savedProduct);
        assertEquals(product, savedProduct);
    }

    @Test
    void updateDeals() {
    }
}