package com.wly.ecomm.repository;

import com.wly.ecomm.model.CartItem;
import com.wly.ecomm.model.Product;
import com.wly.ecomm.model.User;
import com.wly.ecomm.service.ShoppingCartService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan(basePackages = "com.wly.ecomm.*")
class CartItemRepositoryTest {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ShoppingCartService shoppingCartService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private User user;

    private List<Product> products;

    private final static int INIT_QUANTITY = 5;

    @Autowired
    public CartItemRepositoryTest(CartItemRepository cartItemRepository, UserRepository userRepository,
                                  ProductRepository productRepository, ShoppingCartService shoppingCartService) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.shoppingCartService = shoppingCartService;
    }

    //    @DisplayName("CartItem DB Initialization and repository find all Test")
    @Test
    public void givenNA_whenStartOfApplication_then13CartItemsSaved() {
        assertEquals(13, cartItemRepository.findAll().size()); // 11 from init, 2 from beforeEach
    }

    @BeforeEach
    void prepareUserProductAndCartItem() {
        user = userRepository.save(new User("test1customer@junit.org", "test1", "customer"));
        products = productRepository.saveAll(List.of(
                new Product("PIXEL 7 PRO 128GB", 899.00),
                new Product("PIXEL 7 PRO 256GB", 999.00)
        ));
        products.forEach(product -> shoppingCartService.addProduct(product.getId(), INIT_QUANTITY, user));
    }

    @Test
    void GivenUser_WhenFindByUser_ThenReturnAllCartItemsForThatUser() {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        cartItems.forEach(cartItem -> verifyCardItemAndUser(cartItem, user, INIT_QUANTITY));

        cartItems.sort(Comparator.comparingDouble(a -> a.getProduct().getPrice()));

        assertEquals(products.get(0), cartItems.get(0).getProduct());
        assertEquals(products.get(1), cartItems.get(1).getProduct());
    }

    @Test
    void GivenUserAndProduct_WhenFindByUserAndProduct_ReturnCartItemBelongToUserAndProductOnly() {
        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, products.get(1));
        verifyCardItemAndUserAndProduct(cartItem, user, products.get(1), INIT_QUANTITY);
    }

    @Test
    void GivenUserAndProduct_WhenDelete_ThenCorrespondingCartItemIsDeleted() {
        assertEquals(1, cartItemRepository.deleteByUserAndProduct(user, products.get(0)));
    }

    @Test
    void GivenProductAndUser_WhenUpdateCartItemQuantity_ThenQuantityIsUpdatedSuccessfully() {
        var productToUpdateQuantity = products.get(0);
        var productNotToUpdateQuantity = products.get(1);
        cartItemRepository.updateQuantity(10, productToUpdateQuantity.getId(), user.getId());
        var carItemQuantityUpdated = cartItemRepository.findByUserAndProduct(user, productToUpdateQuantity);
        var carItemQuantityNotUpdated = cartItemRepository.findByUserAndProduct(user, productNotToUpdateQuantity);

        log.info("carItemQuantityUpdated : {}", carItemQuantityUpdated);
        log.info("carItemQuantityNotUpdated : {}", carItemQuantityNotUpdated);

        assertEquals(5, carItemQuantityNotUpdated.getQuantity());
        assertEquals(10, carItemQuantityUpdated.getQuantity());
    }

    private void verifyCardItemAndUserAndProduct(CartItem cartItem, User user, Product product, int quantity) {
        verifyCardItem(cartItem, quantity);
        assertEquals(product, cartItem.getProduct());
        assertEquals(user, cartItem.getUser());
    }

    private void verifyCardItemAndUser(CartItem cartItem, User user, int quantity) {
        verifyCardItem(cartItem, quantity);
        assertEquals(user, cartItem.getUser());
    }

    private void verifyCardItem(CartItem cartItem) {
        assertNotNull(cartItem);
        assertNotNull(cartItem.getId());
        assertInstanceOf(UUID.class, cartItem.getId());
    }

    private void verifyCardItem(CartItem cartItem, int quantity) {
        verifyCardItem(cartItem);
        assertEquals(quantity, cartItem.getQuantity());
    }

    @AfterEach
    void logger() {
        products.forEach(product -> cartItemRepository.deleteByUserAndProduct(user, product));
    }
}