package com.wly.ecomm.utils;

import com.wly.ecomm.model.Deal;
import com.wly.ecomm.model.Product;
import com.wly.ecomm.model.User;
import com.wly.ecomm.service.DealService;
import com.wly.ecomm.service.ProductService;
import com.wly.ecomm.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class TestUtil {

    private final ProductService productService;
    private final DealService dealService;

    private final UserService userService;

    private final JdbcTemplate jdbcTemplate;

    public Deal getDealWithProducts(int numberOfProducts) {
        Deal deal = new Deal("OFF37", "TestUtil.prepareDealWithProducts - 37% OFF ORIGINAL PRICE");
        deal.addProducts(getProductList(numberOfProducts));
        return dealService.save(deal);
    }

    public Product getProductWithDeals(int numberOfDeals) {
        Product product = new Product("TestUtil.prepareProductWithDeals-PIXEL 7 PRO 128GB", 899.00);
        product.addDeals(getDealList(numberOfDeals));
        return productService.save(product);
    }

    public Product getProductWithDeals(String name, Double price, String dealName) {
        Product product = new Product(name, price);
        if (dealName.startsWith("OFF") || dealName.startsWith("BOGO"))
            product.addDeal(new Deal(dealName, name));

        return productService.save(product);
    }

    public List<Product> getProductList(int numberOfProducts) {
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < numberOfProducts; i++) {
            Product product = new Product(String.format("TestUtil.prepareProducts-PIXEL 7 PRO 128G-%2d", i), 799.00 + i);
            productList.add(product);
        }
        return productList;
    }

    public List<Deal> getDealList(int numberOfDeals) {
        List<Deal> dealList = new ArrayList<>();

        for (int i = 0; i < numberOfDeals; i++) {
            dealList.add(new Deal("OFF36", "36% OFF ORIGINAL PRICE - TestUtil.prepareDeals"));
        }
        return dealList;
    }

    public int getDealCount(Deal deal) {
        Integer count = jdbcTemplate.queryForObject("select count(*) from deal d where d.id = ?", Integer.class, deal.getId());
        if (count == null) return 0;
        return count;
    }

    public int getProductCount(Product product) {
        Integer count = jdbcTemplate.queryForObject("select count(*) from product p where p.id = ?", Integer.class, product.getId());
        if (count == null) return 0;
        return count;
    }

    public int getProductDealCountByProduct(Product product) {
        Integer count = jdbcTemplate.queryForObject("select count(*) from products_deals pd where pd.product_id = ?", Integer.class, product.getId());
        if (count == null) return 0;
        return count;
    }

    public int getProductDealCountByDeal(Deal deal) {
        Integer count = jdbcTemplate.queryForObject("select count(*) from products_deals pd where pd.Deal_id = ?", Integer.class, deal.getId());
        if (count == null) return 0;
        return count;
    }

    public int getProductDealCountByBoth(Deal deal, Product product) {
        Integer count = jdbcTemplate.queryForObject("select count(*) from products_deals pd where pd.deal_id = ? and pd.product_id = ?", Integer.class, deal.getId(), product.getId());
        if (count == null) return 0;
        return count;
    }

    public User getUser(String name) {
        return userService.save(new User(String.format("%s@test-email.org", name), name, "Last"));
    }
}
