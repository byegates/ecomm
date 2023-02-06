package com.wly.ecomm.repository;

import com.wly.ecomm.model.Deal;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // enables h2 database directly
@ComponentScan(basePackages = "com.wly.ecomm.*")
class DealRepositoryTest {

    @Autowired private DealRepository repository;

    private Deal deal;

    private static final String name = "T-BOGO50", description = "BUY ONE GET ONE 50% OFF! - TEST";

    @BeforeEach
    void setUp() {
        deal = repository.save(new Deal(name, description));
    }

    @Test
    @Disabled
    void givenNA_whenStartOfApplication_then4DealsSaved() {
        assertEquals(4, repository.findAll().size());
    }

    @Test @DisplayName("Save A Deal")
    void givenDealObject_whenSave_thenSavedDealEqualsOriginalDeal() {
        assertNotNull(deal);
        assertNotNull(deal.getId());
        assertInstanceOf(Integer.class, deal.getId());
        assertTrue(deal.getId() > 0);
        assertEquals(name, deal.getName());
        assertEquals(description, deal.getDescription());
    }

    @Test @DisplayName("Find a deal by Id")
    void findById() {
        var deal = repository.save(new Deal("OFF30", "30% OFF FULL PRICE"));
        var maybeFoundDeal = repository.findById(deal.getId());

        assertTrue(maybeFoundDeal.isPresent());
        assertEquals(deal, maybeFoundDeal.get());
    }

    @Test @DisplayName("Update info of an existing deal")
    void updateDeal() {
        deal.setName("T-OFF45");
        deal.setDescription("45% OFF FULL PRICE - updated");

        var dealUpdated = repository.save(deal);
        assertNotNull(dealUpdated);
        assertEquals(deal, dealUpdated);
    }

    @Test @DisplayName("Delete a deal from DB")
    void deleteDeal() {
        repository.deleteById(deal.getId());
        assertTrue(repository.findById(deal.getId()).isEmpty());
    }

    private List<Deal> getDeals() {
        return repository.saveAll(List.of(
                new Deal("T-OFF21", "TEST Deals"),
                new Deal("T-OFF26", "TEST Deals"),
                new Deal("T-OFF31", "TEST Deals"),
                new Deal("T-OFF36", "TEST Deals"),
                new Deal("OFF36", "TEST Deals"),
                new Deal("BOGO25", "TEST Deals"),
                new Deal("BOGO50", "TEST Deals"),
                new Deal("BOGO75", "TEST Deals"),
                new Deal("BOGO100", "TEST Deals"),
                new Deal("T-OFF41", "TEST Deals")
        ));
    }

    @Test @DisplayName("Select Deals starts with OFF using JPQL")
    void partialSelectJPQL() {
        getDeals();
        String dealCode = "T-OFF";

        var foundDeals = repository.findPartiallyByNameJPQL(dealCode);
        assertEquals(5, foundDeals.size());
    }

    @Test @DisplayName("Select Deals starts with OFF using Native Query")
    void partialSelectNative() {
        getDeals();
        String dealCode = "T-OFF";

        var foundDeals = repository.findPartiallyByNameNative(dealCode);
        assertEquals(5, foundDeals.size());
    }

    @Test @DisplayName("Deal Description length 1 < 5")
    void dealDescriptionTooShort1() {
        assertThrows(ConstraintViolationException.class, () -> repository.save(new Deal("T-OFF30", "1")));
    }

    @Test @DisplayName("Deal Description length 4 < 5")
    void dealDescriptionTooShort4() {
        assertThrows(ConstraintViolationException.class, () -> repository.save(new Deal("T-OFF30", "1234")));
    }

    @Test @DisplayName("Deal Description length 5 = 5")
    void dealDescriptionTooShort5() {
        repository.save(new Deal("T-OFF30", "12345"));
    }

    @Test @DisplayName("Deal Name length 1 < 4")
    void dealNameTooShort1() {
        assertThrows(ConstraintViolationException.class, () -> repository.save(new Deal("1", description)));
    }

    @Test @DisplayName("Deal Name length 3 < 4")
    void dealNameTooShort3() {
        assertThrows(ConstraintViolationException.class, () -> repository.save(new Deal("123", description)));
    }

    @Test @DisplayName("Deal Name length 4 is OKAY")
    void dealNameTooShort4() {
        repository.save(new Deal("1234", description));
    }

    @Test @DisplayName("Deal Name length 25 is okay")
    void dealDescriptionLength25() {
        repository.save(new Deal("OFF25".repeat(5), description));
    }

    @Test @DisplayName("Deal Name length 26 > 25")
    void dealDescriptionLength26() {
        assertThrows(ConstraintViolationException.class, () -> repository.save(new Deal("12345".repeat(5) + "1", description)));
    }
}