package com.wly.ecomm.service;

import com.wly.ecomm.exception.UserDefinedException;
import com.wly.ecomm.model.Deal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DealServiceTest {
    private final DealService dealService;

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

    private void assertEqualsWithoutId(Deal deal, Deal savedDeal) {
        assertNotNull(savedDeal);
        assertNotNull(savedDeal.getId());
        assertTrue(savedDeal.getId() > 0);

        assertEquals(deal.getName(), savedDeal.getName());
        assertEquals(deal.getDescription(), savedDeal.getDescription());
    }
}