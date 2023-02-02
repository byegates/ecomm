package com.wly.ecomm.service;

import com.wly.ecomm.dto.UserDTO;
import com.wly.ecomm.model.CartItem;
import com.wly.ecomm.model.Product;
import com.wly.ecomm.model.User;
import com.wly.ecomm.utils.MathUtils;
import com.wly.ecomm.utils.TestUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ComponentScan(basePackages = "com.wly.ecomm.*")
@AutoConfigureTestDatabase
class ShoppingCartServiceTest {
    @Autowired private ShoppingCartService cartService;

    @Autowired private TestUtil testUtil;

    private double parseDealCode(String dealCode) {
        int startIdx = dealCode.startsWith("BOGO") ? 4 : dealCode.startsWith("OFF") ? 3 : -1;

        return startIdx == -1 ? .0 : Double.parseDouble(dealCode.substring(startIdx)) / 100;
    }

    private void verifyCalculation(String name) {
        String[] arr = name.split("_");
        name = name.substring(0, Math.min(45, name.length()));
        User user = testUtil.getUser(name);
        double expectedAmount = .0;
        for (int i = 1; i+4 < arr.length; i+=4) {
            String dealCode = arr[i].toUpperCase();
            double price = Double.parseDouble(arr[i+1]) + Math.random();
            int quantity = Integer.parseInt(arr[i+2]);
            Product product = testUtil.getProductWithDeals(name, price, dealCode);
            cartService.addProduct(user.getId(), product.getId(), quantity);
            expectedAmount += expectedAmount(dealCode, quantity, price);
        }
        assertEquals(expectedAmount, cartService.viewReceipt(user).getTotalDue());
    }

    private double expectedAmount(String dealCode, int quantity, double price) {
        double off = parseDealCode(dealCode);
        if (dealCode.startsWith("BOGO")) {
            int half = quantity/2;
            return MathUtils.round(price*(half*(1-off)+quantity-half), 2);
        }

        if (dealCode.startsWith("OFF") || dealCode.equals("NODEAL")) {
            return MathUtils.round(price * quantity * (1-off), 2);
        }

        return .0;
    }

    private static void assertCartItem(Product product, User user, int quantity, CartItem cartItem) {
        assertNotNull(cartItem);
        assertNotNull(cartItem.getId());
        assertEquals(product, cartItem.getProduct());
        assertEquals(user.getId(), cartItem.getUser().getId());
        assertEquals(quantity, cartItem.getQuantity());
    }

    @Test @Transactional
    void receipt_BOGO50_899_2_items() {
        verifyCalculation(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test @Transactional
    void receipt_BOGO50_749_3_items() {
        verifyCalculation(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test @Transactional
    void receipt_BOGO50_649_5_items() {
        verifyCalculation(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test @Transactional
    void receipt_BOGO50_549_7_items() {
        verifyCalculation(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test @Transactional
    void receipt_BOGO100_1099_3_items() {
        verifyCalculation(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test @Transactional
    void receipt_BOGO100_999_5_items() {
        verifyCalculation(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test @Transactional
    void receipt_BOGO100_899_7_items() {
        verifyCalculation(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test @Transactional
    void receipt_BOGO100_799_9_items() {
        verifyCalculation(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test @Transactional
    void receipt_OFF37_1099_9_items() {
        verifyCalculation(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test @Transactional
    void receipt_OFF27_699_9_items() {
        verifyCalculation(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test @Transactional
    void receipt_OFF0_699_9_items() {
        verifyCalculation(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test @Transactional
    void receipt_BOGO0_739_9_items() {
        verifyCalculation(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test @Transactional
    void receipt_NODEAL_799_9_items() {
        verifyCalculation(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test @Transactional
    void receipt_OFF33_1099_4_items_BOGO50_899_5_items() {
        verifyCalculation(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test @Transactional
    void receipt_OFF27_379_5_items_BOGO75_482_5_items_BOGO50_731_7_items() {
        verifyCalculation(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test @Transactional
    void findByUser_3Products1User() {
        User user = testUtil.getUser(Thread.currentThread().getStackTrace()[1].getMethodName());
        int cartItemQuantity = 5, numOfProducts = 3;
        var deal = testUtil.getDealWithProducts(numOfProducts);

        var productList = deal.getProducts().stream().toList();
        productList.forEach(product -> cartService.addProduct(user.getId(), product.getId(), cartItemQuantity));

        List<CartItem> cartItems = cartService.findByUser(user);
        assertEquals(3, cartItems.size());
        cartItems.forEach(cartItem -> {
            assertEquals(user.getId(), cartItem.getUser().getId());
            assertEquals(cartItemQuantity, cartItem.getQuantity());
        });

        for (int i = 0; i < numOfProducts; i++) {
            assertEquals(productList.get(i), cartItems.get(i).getProduct());
        }

    }

    @Test @Transactional
    void addProduct_simple_quantity5() {
        Product product = testUtil.getProductWithDeals(0);
        User user = testUtil.getUser(Thread.currentThread().getStackTrace()[1].getMethodName());
        int quantity = 5;
        assertCartItem(product, user, quantity, cartService.addProduct(user.getId(), product.getId(), quantity));
    }

    @Test @Transactional
    void updateQuantity() {
        Product product = testUtil.getProductWithDeals(0);
        User user = testUtil.getUser(Thread.currentThread().getStackTrace()[1].getMethodName());
        int quantity = 5;
        assertCartItem(product, user, quantity, cartService.addProduct(user.getId(), product.getId(), quantity));

        quantity = 10;
        cartService.updateQuantity(user.getId(), product.getId(), quantity);
        assertCartItem(product, user, quantity, cartService.findByUserAndProduct(user, product));
    }

    @Test @Transactional
    void findByUserAndProduct() {
        Product product = testUtil.getProductWithDeals(0);
        User user = testUtil.getUser(Thread.currentThread().getStackTrace()[1].getMethodName());
        int quantity = 5;
        cartService.addProduct(user.getId(), product.getId(), quantity);
        assertCartItem(product, user, quantity, cartService.findByUserAndProduct(user, product));
    }

    @Test
    void deleteByUserAndProduct() {
        Product product = testUtil.getProductWithDeals(0);
        User user = testUtil.getUser(Thread.currentThread().getStackTrace()[1].getMethodName());
        int quantity = 5;

        cartService.addProduct(user.getId(), product.getId(), quantity);
        assertCartItem(product, user, quantity, cartService.findByUserAndProduct(user, product));

        assertEquals(1, cartService.deleteByUserAndProduct(user.getId(), product.getId()));
        assertNull(cartService.findByUserAndProduct(user, product));
    }

    @Test @Transactional
    void findUserDtoById() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        User user = testUtil.getUser(methodName);
        int cartItemQuantity = 5;

        cartService.addProduct(user.getId(), testUtil.getProductWithDeals(0).getId(), cartItemQuantity);
        cartService.addProduct(user.getId(), testUtil.getProductWithDeals(1).getId(), cartItemQuantity);
        UserDTO userDTO = cartService.findUserDtoById(user.getId());
        assertEquals(2, userDTO.getReceiptDto().getCartItems().size());
    }
}