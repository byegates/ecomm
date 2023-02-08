package com.wly.ecomm.service;

import com.wly.ecomm.exception.UserDefinedException;
import com.wly.ecomm.model.Deal;
import com.wly.ecomm.model.Product;
import com.wly.ecomm.utils.TestUtil;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class ProductServiceTest {

    @Autowired private ProductService productService;
    @Autowired private TestUtil testUtil;

    @Autowired private EntityManager em;

    @Test @Transactional
    void deleteById_delete1_normal_product() {
        Product product = productService.save(new Product("PIXEL 7 PRO 128GB", 899.00));
        assertNotNull(productService.findById(product.getId()));
        productService.deleteById(product.getId());
        assertThrows(UserDefinedException.class, () -> productService.findById(product.getId()));
    }

    @Test @Transactional
    void deleteById_delete1_product_with_2deals() {
        int numberOfDeals = 2;
        Product product = testUtil.getProductWithDeals(numberOfDeals);
        assertNotNull(product.getId());
        assertEquals(numberOfDeals, product.getDeals().size());
        productService.deleteById(product.getId());
        assertThrows(UserDefinedException.class, () -> productService.findById(product.getId()));
    }

    @Test @Transactional
    void deleteById_delete1_product_with_10deals() {
        int numberOfDeals = 10;
        Product product = testUtil.getProductWithDeals(numberOfDeals);
        assertNotNull(product.getId());
        assertEquals(numberOfDeals, product.getDeals().size());
        productService.deleteById(product.getId());
        assertThrows(UserDefinedException.class, () -> productService.findById(product.getId()));
    }

    @Test @Transactional
    void deleteById_delete1_product_with_5deals() {
        int numberOfDeals = 5;
        Product product = testUtil.getProductWithDeals(numberOfDeals);
        assertNotNull(product.getId());
        assertEquals(numberOfDeals, product.getDeals().size());
        productService.deleteById(product.getId());
        assertThrows(UserDefinedException.class, () -> productService.findById(product.getId()));
    }

    @Test @Transactional
    void deleteById_delete1_product_with_20deals() {
        int numberOfDeals = 20;
        Product product = testUtil.getProductWithDeals(numberOfDeals);
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

    @Test @Transactional
    void save_1product() {
        Product product = new Product("PIXEL 7 PRO 128GB", 899.00);
        Product savedProduct = productService.save(product);
        assertNotNull(savedProduct);
        assertNotNull(savedProduct.getId());
        assertTrue(savedProduct.getId() > 0);

        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice());
    }

    @Test @Transactional
    void saveAll_5Products() {
        int numOfProducts = 5;
        List<Product> productList = testUtil.getProductList(numOfProducts);
        assertEquals(numOfProducts, productList.size());
    }

    @Test @Transactional
    void updateDeals() {
        Deal deal1 = testUtil.getDealWithProducts(0);
        Deal deal2 = testUtil.getDealWithProducts(0);
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

    @Test @Transactional
    void updateDeals_ThrowExceptionOnInvalidActionCode() {
        Deal deal = testUtil.getDealWithProducts(1);
        Product product = productService.save(new Product("PIXEL 7 PRO 128GB", 899.00));
        product = productService.updateDeals(product.getId(), "add", deal.getId());
        assertEquals(1, product.getDeals().size());

        int productId = product.getId();
        assertThrows(UserDefinedException.class, () -> productService.updateDeals(productId, "update", deal.getId()));
    }

    @Test @Transactional
    void updateDeals_ThrowExceptionOnInvalidDealId() {
        Product product = productService.save(new Product("PIXEL 7 PRO 128GB", 899.00));
        int productId = product.getId();
        assertThrows(UserDefinedException.class, () -> productService.updateDeals(productId, "update", Integer.MAX_VALUE));
    }

    @Test @Transactional
    void updateDeals_ThrowExceptionOnInvalidProductId() {
        Deal deal = testUtil.getDealWithProducts(0);
        assertThrows(UserDefinedException.class, () -> productService.updateDeals(Integer.MAX_VALUE, "update", deal.getId()));
    }

    /*
     * relationships are inserted after method completion by default, therefore em.flush() is used to accelerate the process
     * Besides, testUtil.getDealCount, testUtil.getProductCount and testUtil.getProductDealCountByDeal
     * uses jdbcTemplate with native sql to get accurate count
     */
    @Test @Transactional
    void OnDeleteCascade_delete_product_with2Deals_also_delete_relationship() {
        int numOfDeals = 2;
        var product = testUtil.getProductWithDeals(numOfDeals);
        em.flush();
        assertEquals(numOfDeals, testUtil.getProductDealCountByProduct(product));
        productService.deleteById(product.getId());
        em.flush();
        assertEquals(0, testUtil.getProductDealCountByProduct(product));
    }

    @Test @Transactional
    void OnDeleteCascade_delete_product_with20Deals_also_delete_relationship() {
        int numOfDeals = 20;
        var product = testUtil.getProductWithDeals(numOfDeals);
        em.flush();
        assertEquals(numOfDeals, testUtil.getProductDealCountByProduct(product));
        productService.deleteById(product.getId());
        em.flush();
        assertEquals(0, testUtil.getProductDealCountByProduct(product));
    }

}