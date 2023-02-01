package com.wly.ecomm.service;

import com.wly.ecomm.dto.CartItemDto;
import com.wly.ecomm.dto.ReceiptDto;
import com.wly.ecomm.model.*;
import com.wly.ecomm.repository.CartItemRepository;
import com.wly.ecomm.utils.MathUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class ShoppingCartService {

    private final CartItemRepository repository;
    private final ProductService productService;

    public List<CartItem> findByUser(User user) {
        return repository.findByUser(user);
    }

    public CartItem findByUserAndProduct(User user, Product product) {return repository.findByUserAndProduct(user, product);}

    public CartItem addProduct(Integer productId, Integer quantity, User user) {
        Product product = productService.findById(productId);
        CartItem cartItem = repository.findByUserAndProduct(user, product);

        int updatedQuantity = quantity;
        if (cartItem == null) {
            cartItem = new CartItem(user, product);
        } else {
            updatedQuantity = cartItem.getQuantity() + quantity;
        }

        cartItem.setQuantity(updatedQuantity);
        repository.save(cartItem);

        return cartItem;
    }

    public void updateQuantity(Integer productId, Integer quantity, UUID userId) {
        repository.updateQuantity(quantity, productId, userId);
    }

    public int deleteByUserAndProduct(User user, Integer productId) {
        return repository.deleteByUserAndProduct(user, productService.findById(productId));
    }

    public ReceiptDto viewReceipt(User user) {
        List<CartItem> cartItems = repository.findByUser(user);

        List<CartItemDto> calculatedItems = cartItems.stream().map(this::buildCartItemDto).toList();
        return ReceiptDto.builder()
                .cartItems(calculatedItems)
                .totalDue(calculatedItems.stream().mapToDouble(CartItemDto::getSubtotal).sum())
                .build();
    }

    private CartItemDto buildCartItemDto(CartItem cartItem) {
        double percentageOff = .0;
        Product product = cartItem.getProduct();
        Double price = product.getPrice();
        String dealAppled = "NONE";
        Optional<Deal> maybeDeal = product.getDeals().stream().findFirst();

        if (maybeDeal.isPresent()) {
            Deal deal = maybeDeal.get();
            dealAppled = deal.getName().toUpperCase();
            percentageOff = applyDeals(dealAppled, cartItem.getQuantity());
        }

        double subtotal = MathUtils.round((1 - percentageOff) * price * cartItem.getQuantity(), 2);
        double priceAfterDiscount = MathUtils.round(subtotal / cartItem.getQuantity(), 2);

        return CartItemDto.builder()
                .productName(product.getName())
                .dealApplied(dealAppled)
                .price(price)
                .priceAfterDiscount(priceAfterDiscount)
                .quantity(cartItem.getQuantity())
                .subtotal(subtotal)
                .build();
    }

    private Double applyDeals(String dealApplied, int quantity) {
        double totalPercentageOff = .0;
        if (dealApplied.startsWith("OFF")) {
            totalPercentageOff = Integer.parseInt(dealApplied.substring(3)) / 100.0;
        } else if (dealApplied.startsWith("BOGO")) {
            double bogoPercentageOff = Integer.parseInt(dealApplied.substring(4)) / 100.0;
            int bogoAppliedQuantity = quantity / 2;
            totalPercentageOff = 1 - ((1-bogoPercentageOff) * bogoAppliedQuantity + quantity - bogoAppliedQuantity) / quantity;
        }

        return totalPercentageOff;
    }

    public void initCartItem(List<User> customers, List<Product> products) {
        var product1_BOGO50 = products.get(0);
        var product2_BOGO50 = products.get(1);
        var product3_OFF35 = products.get(2);
        var product4_OFF35 = products.get(3);
        var product5_BOGO100 = products.get(4);
        var product6_OFF20 = products.get(5);
        var product7_NODEAL = products.get(6);
        var product8_NODEAL = products.get(7);

        addProduct(product1_BOGO50.getId(), 2, customers.get(0));
        addProduct(product3_OFF35.getId(), 1, customers.get(0));

        addProduct(product2_BOGO50.getId(), 3, customers.get(1));
        addProduct(product4_OFF35.getId(), 2, customers.get(1));

        addProduct(product5_BOGO100.getId(), 4, customers.get(2));
        addProduct(product6_OFF20.getId(), 1, customers.get(2));

        addProduct(product5_BOGO100.getId(), 5, customers.get(3));
        addProduct(product6_OFF20.getId(), 3, customers.get(3));
        addProduct(product7_NODEAL.getId(), 2, customers.get(3));

        addProduct(product7_NODEAL.getId(), 1, customers.get(4));
        addProduct(product8_NODEAL.getId(), 2, customers.get(4));
    }
}
