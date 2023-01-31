package com.wly.ecomm.utils;

import com.wly.ecomm.model.Deal;
import com.wly.ecomm.model.Product;
import com.wly.ecomm.service.DealService;
import com.wly.ecomm.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class TestUtil {

    private final ProductService productService;
    private final DealService dealService;

    public List<Product> prepareProducts(int numberOfProducts) {
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < numberOfProducts; i++) {
            productList.add(new Product(String.format("TEST-PIXEL 7 PRO 128G-%2d", i), 799.00+i));
        }
        return productService.saveALl(productList);
    }

    public List<Deal> prepareDeals(int numberOfDeals) {
        List<Deal> dealList = new ArrayList<>();

        for (int i = 0; i < numberOfDeals; i++) {
            dealList.add(new Deal("OFF36", "36% OFF ORIGINAL PRICE - TEST"));
        }
        return dealService.saveAll(dealList);
    }
}
