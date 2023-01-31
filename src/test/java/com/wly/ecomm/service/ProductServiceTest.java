package com.wly.ecomm.service;

import com.wly.ecomm.exception.UserDefinedException;
import com.wly.ecomm.model.Deal;
import com.wly.ecomm.model.Product;
import com.wly.ecomm.utils.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ComponentScan(basePackages = "com.wly.ecomm.*")
class ProductServiceTest {

    private final ProductService productService;
    private final DealService dealService;

    private final TestUtil testUtil;

    @Autowired
    public ProductServiceTest(ProductService productService, DealService dealService, TestUtil testUtil) {
        this.productService = productService;
        this.dealService = dealService;
        this.testUtil = testUtil;
    }

    @Test
    void deleteById_delete1_normal_product() {
        Product product = productService.save(new Product("PIXEL 7 PRO 128GB", 899.00));
        assertNotNull(productService.findById(product.getId()));
        productService.deleteById(product.getId());
        assertThrows(UserDefinedException.class, () -> productService.findById(product.getId()));
    }

    @Test
    void deleteById_delete1_product_with_2deals() {
        int numberOfDeals = 2;
        Product product = prepare_1_product_with_deals(numberOfDeals);
        assertNotNull(product.getId());
        assertEquals(numberOfDeals, product.getDeals().size());
        productService.deleteById(product.getId());
        assertThrows(UserDefinedException.class, () -> productService.findById(product.getId()));
    }

    @Test
    void deleteById_delete1_product_with_10deals() {
        int numberOfDeals = 10;
        Product product = prepare_1_product_with_deals(numberOfDeals);
        assertNotNull(product.getId());
        assertEquals(numberOfDeals, product.getDeals().size());
        productService.deleteById(product.getId());
        assertThrows(UserDefinedException.class, () -> productService.findById(product.getId()));
    }

    @Test
    void deleteById_delete1_product_with_5deals() {
        int numberOfDeals = 5;
        Product product = prepare_1_product_with_deals(numberOfDeals);
        assertNotNull(product.getId());
        assertEquals(numberOfDeals, product.getDeals().size());
        productService.deleteById(product.getId());
        assertThrows(UserDefinedException.class, () -> productService.findById(product.getId()));
    }

    @Test
    void deleteById_delete1_product_with_20deals() {
        int numberOfDeals = 20;
        Product product = prepare_1_product_with_deals(numberOfDeals);
        assertNotNull(product.getId());
        assertEquals(numberOfDeals, product.getDeals().size());
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
    void save_1product() {
        Product product = new Product("PIXEL 7 PRO 128GB", 899.00);
        Product savedProduct = productService.save(product);
        assertNotNull(savedProduct);
        assertNotNull(savedProduct.getId());
        assertTrue(savedProduct.getId() > 0);

        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice());
    }

    @Test
    void saveAll_5Products() {
        int numOfProducts = 5;
        List<Product> productList = testUtil.prepareProducts(numOfProducts);
        assertEquals(numOfProducts, productList.size());
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

    Product prepare_1_product_with_deals(int numberOfDeals) {
        Product product = productService.save(new Product("TEST-PIXEL 7 PRO 128GB", 899.00));

        product.addDeals(dealService.saveAll(testUtil.prepareDeals(numberOfDeals)));
        return productService.save(product);
    }
}