package com.wly.ecomm.repository;

import com.wly.ecomm.model.Deal;
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
class DealRepositoryTest {

    private final DealRepository dealRepository;

    @Autowired
    public DealRepositoryTest(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    @Test
    @Disabled
    public void givenNA_whenStartOfApplication_then4DealsSaved() {
        assertEquals(4, dealRepository.findAll().size());
    }

    @Test
    public void givenDealObject_whenSave_thenSavedDealEqualsOriginalDeal() {
        Deal deal = new Deal("OFF30", "30% OFF FULL PRICE");
        Deal savedDeal = dealRepository.save(deal);

        assertNotNull(savedDeal);
        assertNotNull(savedDeal.getId());
        assertInstanceOf(Integer.class, savedDeal.getId());
        assertTrue(savedDeal.getId() > 0);
        assertEquals(deal.getName(), savedDeal.getName());
        assertEquals(deal.getDescription(), savedDeal.getDescription());
    }
}