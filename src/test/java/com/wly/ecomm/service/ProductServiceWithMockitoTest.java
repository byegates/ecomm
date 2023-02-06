package com.wly.ecomm.service;

import com.wly.ecomm.exception.UserDefinedException;
import com.wly.ecomm.model.Deal;
import com.wly.ecomm.model.Product;
import com.wly.ecomm.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;


/*
 * Using Mockito directly to mock dependencies of ProductService to test it
 * Dependencies Mocked: ProductRepository, DealService
 */

class ProductServiceWithMockitoTest {
    private ProductRepository repository;
    private DealService dealService;

    private ProductService service;

    private Product product;
    private static final int ID = 0x3f3f3f3f;

    @BeforeEach
    void setUp() {
        product = new Product("PIXEL 7 PRO 128GB", 899.00);
        product.setId(ID);
        repository = mock(ProductRepository.class);
        dealService = mock(DealService.class);
        service = new ProductService(repository, dealService);
    }

    @Test @DisplayName("Find By a non-existing ID")
    void findByIdNotFound() {
        int id = Integer.MAX_VALUE;
        given(repository.findById(id)).willReturn(Optional.empty());
        assertThrows(UserDefinedException.class, () -> service.findById(id));
    }

    @Test @DisplayName("Find By a good ID")
    void findByIdSuccess() {
        given(repository.findById(ID)).willReturn(Optional.ofNullable(product));
        var foundProduct = service.findById(ID);
        assertEquals(product, foundProduct);
    }

    @Test @DisplayName("Save a product")
    void save() {
        given(repository.save(product)).willReturn(product);
        Product savedProduct = service.save(product);
        assertEquals(product, savedProduct);
    }

    @Test @DisplayName("Add a deal to product")
    void updateDeals_add() {
        var deal = new Deal("T-OFF30", "30% OFF FULL PRICE - TEST");
        given(dealService.findById(any(Integer.class))).willReturn(deal);
        given(repository.findById(any(Integer.class))).willReturn(Optional.ofNullable(product));
        when(repository.save(any(Product.class))).then(AdditionalAnswers.returnsFirstArg());
        var updatedProduct = service.updateDeals(ID, "ADD", ID);
        assertEquals(product, updatedProduct);
        assertEquals(product.getDeals(), updatedProduct.getDeals());
    }

    @Test @DisplayName("Remove a deal from product")
    void updateDeals_Remove() {
        var deal = new Deal("T-OFF30", "30% OFF FULL PRICE - TEST");
        deal.setId(ID);
        product.addDeal(deal);
        given(dealService.findById(any(Integer.class))).willReturn(deal);
        given(repository.findById(any(Integer.class))).willReturn(Optional.ofNullable(product));
        when(repository.save(any(Product.class))).then(AdditionalAnswers.returnsFirstArg());
        var updatedProduct = service.updateDeals(ID, "REMOVE", ID);
        assertEquals(product, updatedProduct);
        assertEquals(product.getDeals(), updatedProduct.getDeals());
    }

    @Test @DisplayName("invalid action to updateDeals: you get a exception")
    void updateDeals_invalidAction() {
        var deal = new Deal("T-OFF30", "30% OFF FULL PRICE - TEST");
        deal.setId(ID);
        given(dealService.findById(any(Integer.class))).willReturn(deal);
        given(repository.findById(any(Integer.class))).willReturn(Optional.ofNullable(product));
        when(repository.save(any(Product.class))).then(AdditionalAnswers.returnsFirstArg());
        assertThrows(UserDefinedException.class, () -> service.updateDeals(ID, "INVALID-ACTION", ID));
    }
}