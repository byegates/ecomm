package com.wly.ecomm.service;

import com.wly.ecomm.exception.UserDefinedException;
import com.wly.ecomm.model.Deal;
import com.wly.ecomm.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ComponentScan(basePackages = "com.wly.ecomm.*")
class ProductServiceTest {

    private final ProductService productService;
    private final DealService dealService;

    @Autowired
    public ProductServiceTest(ProductService productService, DealService dealService) {
        this.productService = productService;
        this.dealService = dealService;
    }

    @Test
    void deleteById() {
        Product product = productService.save(new Product("PIXEL 7 PRO 128GB", 899.00));
        assertNotNull(productService.findById(product.getId()));
        productService.deleteById(product.getId());
        assertThrows(UserDefinedException.class, () -> productService.findById(product.getId()));
    }

    @Test
    void findById_IdNotFoundException() {
        assertThrows(UserDefinedException.class, () -> productService.findById(Integer.MAX_VALUE));
    }

    @Test
    void findById() {
        Product product = productService.save(new Product("PIXEL 7 PRO 128GB", 899.00));
        Product foundProduct = productService.findById(product.getId());
        assertEquals(product, foundProduct);

    }

    @Test
    void save() {
        Product product = new Product("PIXEL 7 PRO 128GB", 899.00);
        Product savedProduct = productService.save(product);
        assertNotNull(savedProduct);
        assertNotNull(savedProduct.getId());
        assertTrue(savedProduct.getId() > 0);

        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice());
    }

    @Test
    void updateDeals() {
        Deal deal1 = dealService.save(new Deal("45OFF", "45% OFF FULL PRICE"));
        Deal deal2 = dealService.save(new Deal("BOGO50", "BUY ONE GET SECOND ONE 50% OFF"));
        Product product = productService.save(new Product("PIXEL 7 PRO 128GB", 899.00));
        product = productService.updateDeals(product.getId(), "add", deal1.getId());
        assertEquals(1, product.getDeals().size());
        product = productService.updateDeals(product.getId(), "add", deal2.getId());
        assertEquals(2, product.getDeals().size());
        product = productService.updateDeals(product.getId(), "add", deal2.getId());
        assertEquals(2, product.getDeals().size());
        product = productService.updateDeals(product.getId(), "remove", deal1.getId());
        assertEquals(1, product.getDeals().size());
    }

    @Test
    void updateDeals_ThrowExceptionOnInvalidActionCode() {
        Deal deal1 = dealService.save(new Deal("45OFF", "45% OFF FULL PRICE"));
        Product product = productService.save(new Product("PIXEL 7 PRO 128GB", 899.00));
        product = productService.updateDeals(product.getId(), "add", deal1.getId());
        assertEquals(1, product.getDeals().size());

        int productId = product.getId();
        assertThrows(UserDefinedException.class, () -> productService.updateDeals(productId, "update", deal1.getId()));
    }

    @Test
    void updateDeals_ThrowExceptionOnInvalidDealId() {
        Product product = productService.save(new Product("PIXEL 7 PRO 128GB", 899.00));
        int productId = product.getId();
        assertThrows(UserDefinedException.class, () -> productService.updateDeals(productId, "update", Integer.MAX_VALUE));
    }

    @Test
    void updateDeals_ThrowExceptionOnInvalidProductId() {
        Deal deal1 = dealService.save(new Deal("45OFF", "45% OFF FULL PRICE"));
        assertThrows(UserDefinedException.class, () -> productService.updateDeals(Integer.MAX_VALUE, "update", deal1.getId()));
    }
}