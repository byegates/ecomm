package com.wly.ecomm.service;

import com.wly.ecomm.model.*;
import com.wly.ecomm.repository.DealRepository;
import com.wly.ecomm.repository.ProductRepository;
import com.wly.ecomm.repository.RoleRepository;
import com.wly.ecomm.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
@ComponentScan(basePackages = "com.wly.ecomm.*")
@Transactional
class ShoppingCartServiceTest {
    private final ShoppingCartService cartService;
    private final ProductRepository productRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;
    private final DealRepository dealRepository;

    @Autowired
    public ShoppingCartServiceTest(ShoppingCartService cartService, ProductRepository productRepository,
                                   RoleRepository roleRepository, UserRepository userRepository,
                                   DealRepository dealRepository) {
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.dealRepository = dealRepository;
    }

    private Role role;
    private List<User> users;
    private List<Product> products;
    private List<Deal> deals;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @BeforeEach
    void setUp() {
        role = roleRepository.save(new Role("TESTCART"));
        setUpUser();
        setUpDeals();
        setUpProduct();
    }

    void setUpProduct() {
        products = List.of(
                new Product("PIXEL 7 PRO 128GB", 899.00),
                new Product("PIXEL 7 PRO 256GB", 999.00),
                new Product("PIXEL 7 PRO 512GB", 1099.00),
                new Product("PIXEL 7 128GB", 699.00),
                new Product("PIXEL 7 256GB", 799.00)
        );

        // two deals added only first one counts
        products.get(0).addDeals(List.of(deals.get(0), deals.get(1)));

        // two deals added only first one counts
        products.get(1).addDeals(List.of(deals.get(1), deals.get(2)));

        products.get(2).addDeal(deals.get(2));
        products.get(3).addDeal(deals.get(3));

        products = productRepository.saveAll(products);
    }

    void setUpDeals() {
        deals = dealRepository.saveAll(Stream.of(
                new Deal("BOGO50", "BUY ONE GET ONE 50% OFF"),
                new Deal("BOGO100", "BUY ONE GET ONE 100% OFF"),
                new Deal("OFF35", "35% OFF FULL PRICE"),
                new Deal("OFF25", "25% OFF FULL PRICE")
        ).toList());
    }

    void setUpUser() {
        users = userRepository.saveAll(Stream.of(
                new User("tester1@tester_domain.com", "tester1", "tester"),
//                new User("tester2@tester_domain.com", "tester2", "tester"),
//                new User("tester3@tester_domain.com", "tester3", "tester"),
//                new User("tester4@tester_domain.com", "tester4", "tester"),
                new User("tester0@tester_domain.com", "tester0", "tester")
        ).peek(user -> user.addRole(role)).toList());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DirtiesContext
    void viewReceipt_twoDeals_Only_CountOne_BOGO50() {
        User user = users.get(1);
        Product product1 = products.get(0); // 899*1.5
        cartService.addProduct(product1.getId(), 2, user);
        assertEquals(1348.5, cartService.viewReceipt(user).getTotalDue());
    }

    @Test
    @DirtiesContext
    void viewReceipt_twoDeals_Only_CountOne_BOGO100() {
        User user = users.get(0);
        Product product1 = products.get(1); // 999 2nd 100% off
        cartService.addProduct(product1.getId(), 2, user);
        assertEquals(999.0, cartService.viewReceipt(user).getTotalDue());
    }

    @Test
    @DirtiesContext
    void viewReceipt_BOGO50_3Items() {
        User user = users.get(0);
        Product product1 = products.get(0); // 899*2.5
        cartService.addProduct(product1.getId(), 3, user);
        assertEquals(2247.5, cartService.viewReceipt(user).getTotalDue());
    }

    @Test
    @DirtiesContext
    void viewReceipt_BOGO50_5Items() {
        User user = users.get(0);
        Product product1 = products.get(0); // 899*3
        cartService.addProduct(product1.getId(), 5, user);
        assertEquals(3596.0, cartService.viewReceipt(user).getTotalDue());
    }

    @Test
    @DirtiesContext
    void viewReceipt_BOGO100_5Items() {
        User user = users.get(0);
        Product product1 = products.get(1); // 999*3
        cartService.addProduct(product1.getId(), 5, user);
        assertEquals(2997.0, cartService.viewReceipt(user).getTotalDue());
    }

    @Test
    @DirtiesContext
    void viewReceipt_BOGO100_9Items() {
        User user = users.get(0);
        Product product1 = products.get(1); // 999*5
        cartService.addProduct(product1.getId(), 9, user);
        assertEquals(4995.0, cartService.viewReceipt(user).getTotalDue());
    }

    @Test
    @DirtiesContext
    void viewReceipt_product2_OFF35_9Items_1099() {
        User user = users.get(0);
        Product product1 = products.get(2);
        cartService.addProduct(product1.getId(), 9, user);
        assertEquals(6429.15, cartService.viewReceipt(user).getTotalDue());
    }

    @Test
    @DirtiesContext
    void viewReceipt_product3_OFF25_9Items_699() {
        User user = users.get(0);
        Product product1 = products.get(3);
        cartService.addProduct(product1.getId(), 9, user);
        assertEquals(4718.25, cartService.viewReceipt(user).getTotalDue());
    }

    @Test
    @DirtiesContext
    void viewReceipt_product4_NoDeal_9Items_799() {
        User user = users.get(0);
        Product product1 = products.get(4);
        cartService.addProduct(product1.getId(), 9, user);
        assertEquals(7191, cartService.viewReceipt(user).getTotalDue());
    }

    @Test
    @DirtiesContext
    void viewReceipt__product2_OFF35_4Items_1099_product0_BOGO50_5Items_899() {
        User user = users.get(0);
        cartService.addProduct(products.get(2).getId(), 4, user);
        cartService.addProduct(products.get(0).getId(), 5, user);
        assertEquals(6453.4, cartService.viewReceipt(user).getTotalDue());
    }

    @Test
    void findByUser() {
        User user = users.get(1);
        Product product1 = products.get(0), product2 = products.get(1), product3 = products.get(2);
        int cartItemQuantity = 5;

        cartService.addProduct(product1.getId(), cartItemQuantity, user);
        cartService.addProduct(product2.getId(), cartItemQuantity, user);
        cartService.addProduct(product3.getId(), cartItemQuantity, user);

        List<CartItem> cartItems = cartService.findByUser(user);
        assertEquals(3, cartItems.size());
        cartItems.forEach(cartItem -> {
            assertEquals(user, cartItem.getUser());
            assertEquals(cartItemQuantity, cartItem.getQuantity());
        });

        assertEquals(product1, cartItems.get(0).getProduct());
        assertEquals(product2, cartItems.get(1).getProduct());
        assertEquals(product3, cartItems.get(2).getProduct());
    }

    @Test
    @DirtiesContext
    void addProduct() {
        Product product = products.get(0);
        User user = users.get(0);
        int cartItemQuantity = 5;

        CartItem cartItem = cartService.addProduct(product.getId(), cartItemQuantity, user);
        assertNotNull(cartItem);
        assertNotNull(cartItem.getId());
        assertEquals(product, cartItem.getProduct());
        assertEquals(user, cartItem.getUser());
        assertEquals(cartItemQuantity, cartItem.getQuantity());
    }

    @Test
    @DirtiesContext
    void updateQuantity() {
        Product product = products.get(1);
        User user = users.get(0);
        int cartItemQuantity = 5;

        CartItem cartItem = cartService.addProduct(product.getId(), cartItemQuantity, user);
        assertNotNull(cartItem);
        assertNotNull(cartItem.getId());
        assertEquals(product, cartItem.getProduct());
        assertEquals(user, cartItem.getUser());
        assertEquals(cartItemQuantity, cartItem.getQuantity());

        cartItemQuantity = 10;
        cartService.updateQuantity(product.getId(), cartItemQuantity, user.getId());
        CartItem cartItemFound = cartService.findByUserAndProduct(user, product);

        assertNotNull(cartItemFound);
        assertNotNull(cartItemFound.getId());
        assertEquals(product, cartItemFound.getProduct());
        assertUserFieldEqual(user, cartItemFound.getUser());
        assertEquals(cartItemQuantity, cartItemFound.getQuantity());
    }

    @Test
    @DirtiesContext
    void findByUserAndProduct() {
        Product product = products.get(2);
        User user = users.get(1);
        int cartItemQuantity = 5;

        cartService.addProduct(product.getId(), cartItemQuantity, user);
        CartItem cartItemFound = cartService.findByUserAndProduct(user, product);

        assertNotNull(cartItemFound);
        assertNotNull(cartItemFound.getId());
        assertEquals(product, cartItemFound.getProduct());
        assertEquals(user, cartItemFound.getUser());
        assertEquals(cartItemQuantity, cartItemFound.getQuantity());
    }

    @Test
    @DirtiesContext
    void deleteByUserAndProduct() {
        Product product = products.get(2);
        User user = users.get(1);
        int cartItemQuantity = 5;

        cartService.addProduct(product.getId(), cartItemQuantity, user);
        CartItem cartItemFound = cartService.findByUserAndProduct(user, product);

        assertNotNull(cartItemFound);
        assertNotNull(cartItemFound.getId());
        assertEquals(product, cartItemFound.getProduct());
        assertEquals(user, cartItemFound.getUser());
        assertEquals(cartItemQuantity, cartItemFound.getQuantity());

        assertEquals(1, cartService.deleteByUserAndProduct(user, product.getId()));
        cartItemFound = cartService.findByUserAndProduct(user, product);
        assertNull(cartItemFound);
    }

    void assertUserFieldEqual(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getEmail(), user2.getEmail());
        assertEquals(user1.getRoles(), user2.getRoles());
        assertEquals(user1.getFirstName(), user2.getFirstName());
        assertEquals(user1.getLastName(), user2.getLastName());
    }
}