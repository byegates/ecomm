package com.wly.ecomm.service;

import com.wly.ecomm.exception.UserDefinedException;
import com.wly.ecomm.model.Deal;
import com.wly.ecomm.model.Product;
import com.wly.ecomm.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DealServiceTest {
    private final DealService dealService;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    public DealServiceTest(DealService dealService) {
        this.dealService = dealService;
    }

    // To-DO add exception handling for all services for deleteById not found
    @Test
    void deleteById_ButNotFound() {
        assertThrows(EmptyResultDataAccessException.class, () -> dealService.deleteById(Integer.MAX_VALUE));
    }

    @Test
    @DirtiesContext
    void deleteById() {
        Deal deal1 = dealService.save(new Deal("45OFF", "45% OFF FULL PRICE"));
        Deal deal2 = dealService.save(new Deal("BOGO50", "BUY ONE GET SECOND ONE 50% OFF"));
        Deal foundDeal1 = dealService.findById(deal1.getId());
        Deal foundDeal2 = dealService.findById(deal2.getId());

        // this asserts id equals only
        assertEquals(deal1, foundDeal1);
        assertEquals(deal2, foundDeal2);

        // asserts all other fields are equal
        assertEqualsWithoutId(deal1, foundDeal1);
        assertEqualsWithoutId(deal2, foundDeal2);

        dealService.deleteById(deal1.getId());
        assertThrows(UserDefinedException.class, () -> dealService.findById(deal1.getId()));

        dealService.deleteById(deal2.getId());
        assertThrows(UserDefinedException.class, () -> dealService.findById(deal2.getId()));
    }

    @Test
    @DirtiesContext
    void deleteById_1deal_with_2products() {
        int numberOfProducts = 2;
        Deal deal = prepareDealWithProducts(numberOfProducts);
        assertNotNull(deal.getId());
        assertEquals(numberOfProducts, deal.getProductSet().size());
        dealService.deleteById(deal.getId());
        assertThrows(UserDefinedException.class, () -> dealService.findById(deal.getId()));
    }

    @Test
    @DirtiesContext
    void deleteById_1deal_with_20products() {
        int numberOfProducts = 20;
        Deal deal = prepareDealWithProducts(numberOfProducts);
        assertNotNull(deal.getId());
        assertEquals(numberOfProducts, deal.getProductSet().size());
        dealService.deleteById(deal.getId());
        assertThrows(UserDefinedException.class, () -> dealService.findById(deal.getId()));
    }

    @Test
    @DirtiesContext
    void deleteById_1deal_with_5products() {
        int numberOfProducts = 5;
        Deal deal = prepareDealWithProducts(numberOfProducts);
        assertNotNull(deal.getId());
        assertEquals(numberOfProducts, deal.getProductSet().size());
        dealService.deleteById(deal.getId());
        assertThrows(UserDefinedException.class, () -> dealService.findById(deal.getId()));
    }

    @Test
    void findById_ExceptionOnNotFound() {
        assertThrows(UserDefinedException.class, () -> dealService.findById(Integer.MAX_VALUE));
    }

    @Test
    void findById() {
        Deal deal1 = dealService.save(new Deal("45OFF", "45% OFF FULL PRICE"));
        Deal deal2 = dealService.save(new Deal("BOGO50", "BUY ONE GET SECOND ONE 50% OFF"));
        Deal foundDeal1 = dealService.findById(deal1.getId());
        Deal foundDeal2 = dealService.findById(deal2.getId());

        // this asserts id equals only
        assertEquals(deal1, foundDeal1);
        assertEquals(deal2, foundDeal2);

        // asserts all other fields are equal
        assertEqualsWithoutId(deal1, foundDeal1);
        assertEqualsWithoutId(deal2, foundDeal2);
    }

    @Test
    void save() {
        Deal deal1 = new Deal("45OFF", "45% OFF FULL PRICE");
        Deal deal2 = new Deal("BOGO50", "BUY ONE GET SECOND ONE 50% OFF");
        Deal saveDeal1 = dealService.save(deal1);
        Deal saveDeal2 = dealService.save(deal2);
        assertEqualsWithoutId(deal1, saveDeal1);
        assertEqualsWithoutId(deal2, saveDeal2);
    }

    private Deal prepareDealWithProducts(int numberOfProducts) {
        Deal deal = new Deal("OFF37", "TEST - 37% OFF ORIGINAL PRICE");
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < numberOfProducts; i++) {
            productList.add(new Product(String.format("TEST-PIXEL 7 PRO 128G-%2d", i), 799.00+i));
        }
        deal.addProducts(productRepository.saveAll(productList));
        return dealService.save(deal);
    }

    private void assertEqualsWithoutId(Deal deal, Deal savedDeal) {
        assertNotNull(savedDeal);
        assertNotNull(savedDeal.getId());
        assertTrue(savedDeal.getId() > 0);

        assertEquals(deal.getName(), savedDeal.getName());
        assertEquals(deal.getDescription(), savedDeal.getDescription());
    }
}