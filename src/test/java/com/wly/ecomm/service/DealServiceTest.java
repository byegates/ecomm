package com.wly.ecomm.service;

import com.wly.ecomm.exception.UserDefinedException;
import com.wly.ecomm.model.Deal;
import com.wly.ecomm.utils.TestUtil;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class DealServiceTest {

    @Autowired private DealService dealService;
    @Autowired private TestUtil testUtil;

    @Autowired private EntityManager em;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void cleanUp() {
    }

    // To-DO add exception handling for all services for deleteById not found
    @Test
    void deleteById_ButNotFound() {
        assertThrows(EmptyResultDataAccessException.class, () -> dealService.deleteById(Integer.MAX_VALUE));
    }

    @Test
    @Transactional
    void deleteById() {
        Deal deal1 = dealService.save(new Deal("45OFF", "45% OFF FULL PRICE"));
        Deal deal2 = dealService.save(new Deal("BOGO50", "BUY ONE GET SECOND ONE 50% OFF"));
        Deal foundDeal1 = dealService.findById(deal1.getId());
        Deal foundDeal2 = dealService.findById(deal2.getId());

        // this asserts id equals only
        assertEquals(deal1, foundDeal1);
        assertEquals(deal2, foundDeal2);

        // asserts all other fields are equal
        assertDealEqualsWithoutId(deal1, foundDeal1);
        assertDealEqualsWithoutId(deal2, foundDeal2);

        dealService.deleteById(deal1.getId());
        assertThrows(UserDefinedException.class, () -> dealService.findById(deal1.getId()));

        dealService.deleteById(deal2.getId());
        assertThrows(UserDefinedException.class, () -> dealService.findById(deal2.getId()));
    }

    @Test
    @Transactional
    void deleteById_1deal_with_2products() {
        int numberOfProducts = 2;
        Deal deal = testUtil.getDealWithProducts(numberOfProducts);
        assertNotNull(deal.getId());
        assertEquals(numberOfProducts, deal.getProducts().size());
        dealService.deleteById(deal.getId());
        assertThrows(UserDefinedException.class, () -> dealService.findById(deal.getId()));
    }

    @Test
    @Transactional
    void deleteById_1deal_with_20products() {
        int numberOfProducts = 20;
        Deal deal = testUtil.getDealWithProducts(numberOfProducts);
        assertNotNull(deal.getId());
        assertEquals(numberOfProducts, deal.getProducts().size());
        dealService.deleteById(deal.getId());
        assertThrows(UserDefinedException.class, () -> dealService.findById(deal.getId()));
    }

    @Test
    @Transactional
    void deleteById_1deal_with_5products() {
        int numberOfProducts = 5;
        Deal deal = testUtil.getDealWithProducts(numberOfProducts);
        assertNotNull(deal.getId());
        assertEquals(numberOfProducts, deal.getProducts().size());
        dealService.deleteById(deal.getId());
        assertThrows(UserDefinedException.class, () -> dealService.findById(deal.getId()));
    }

    @Test
    @Transactional
    void findById_ExceptionOnNotFound() {
        assertThrows(UserDefinedException.class, () -> dealService.findById(Integer.MAX_VALUE));
    }

    @Test
    @Transactional
    void findById() {
        Deal deal1 = dealService.save(new Deal("45OFF", "45% OFF FULL PRICE"));
        Deal deal2 = dealService.save(new Deal("BOGO50", "BUY ONE GET SECOND ONE 50% OFF"));
        Deal foundDeal1 = dealService.findById(deal1.getId());
        Deal foundDeal2 = dealService.findById(deal2.getId());

        // this asserts id equals only
        assertEquals(deal1, foundDeal1);
        assertEquals(deal2, foundDeal2);

        // asserts all other fields are equal
        assertDealEqualsWithoutId(deal1, foundDeal1);
        assertDealEqualsWithoutId(deal2, foundDeal2);
    }

    @Test
    @Transactional
    void save_2_deals_1by1() {
        Deal deal1 = new Deal("45OFF", "45% OFF FULL PRICE");
        Deal deal2 = new Deal("BOGO50", "BUY ONE GET SECOND ONE 50% OFF");
        Deal saveDeal1 = dealService.save(deal1);
        Deal saveDeal2 = dealService.save(deal2);
        assertDealEqualsWithoutId(deal1, saveDeal1);
        assertDealEqualsWithoutId(deal2, saveDeal2);
    }

    @Test
    @Transactional
    void saveAll_5Deal() {
        int numOfDeals = 5;
        List<Deal> dealList = testUtil.getDealList(numOfDeals);
        assertEquals(numOfDeals, dealList.size());
    }

    /*
     * relationships are inserted after method completion by default, therefore em.flush() is used to accelerate the process
     * Besides, testUtil.getDealCount, testUtil.getProductCount and testUtil.getProductDealCountByDeal
     * uses jdbcTemplate with native sql to get accurate count
     */
    @Test
    @Transactional
    void OnDeleteCascade_delete_deal_with2Products_also_delete_relationship() {
        int numOfProducts = 2;
        var deal = testUtil.getDealWithProducts(numOfProducts);
        em.flush();
        assertEquals(numOfProducts, testUtil.getProductDealCountByDeal(deal));
        dealService.deleteById(deal.getId());
        em.flush();
        assertEquals(0, testUtil.getProductDealCountByDeal(deal));
    }

    @Test
    @Transactional
    void OnDeleteCascade_delete_deal_with20Products_also_delete_relationship() {
        int numOfProducts = 20;
        var deal = testUtil.getDealWithProducts(numOfProducts);
        em.flush();
        assertEquals(numOfProducts, testUtil.getProductDealCountByDeal(deal));
        dealService.deleteById(deal.getId());
        em.flush();
        assertEquals(0, testUtil.getProductDealCountByDeal(deal));
    }

    private void assertDealEqualsWithoutId(Deal deal, Deal savedDeal) {
        assertNotNull(savedDeal);
        assertNotNull(savedDeal.getId());
        assertTrue(savedDeal.getId() > 0);

        assertEquals(deal.getName(), savedDeal.getName());
        assertEquals(deal.getDescription(), savedDeal.getDescription());
    }
}