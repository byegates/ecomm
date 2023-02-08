package com.wly.ecomm.service;

import com.wly.ecomm.dto.CartItemDto;
import com.wly.ecomm.dto.ReceiptDto;
import com.wly.ecomm.dto.UserDTO;
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
@AllArgsConstructor
public class ShoppingCartService {

    private final CartItemRepository repository;
    private final ProductService productService;

    private final UserService userService;

    public List<CartItem> findByUser(User user) {
        return repository.findByUser(user);
    }

    public CartItem findByUserAndProduct(User user, Product product) {return repository.findByUserAndProduct(user, product);}

    @Transactional
    public CartItem addProduct(UUID userId, Integer productId, Integer quantity) {
        CartItem cartItem = repository.findByUserIdAndProductId(userId, productId);

        int updatedQuantity = quantity;
        if (cartItem == null) {
            cartItem = new CartItem(userService.findById(userId), productService.findById(productId));
        } else {
            updatedQuantity = cartItem.getQuantity() + quantity;
        }

        cartItem.setQuantity(updatedQuantity);
        repository.save(cartItem);

        return cartItem;
    }

    @Transactional
    public void updateQuantity(UUID userId, Integer productId, Integer quantity) {
        repository.updateQuantity(quantity, productId, userId);
    }

    @Transactional
    public int deleteByUserAndProduct(UUID userId, Integer productId) {
        return repository.deleteByUserIdAndProductId(userId, productId);
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
            totalPercentageOff = Double.parseDouble(dealApplied.substring(3)) / 100.0;
        } else if (dealApplied.startsWith("BOGO")) {
            double bogoPercentageOff = Double.parseDouble(dealApplied.substring(4)) / 100.0;
            int bogoAppliedQuantity = quantity / 2;
            totalPercentageOff = 1 - ((1-bogoPercentageOff) * bogoAppliedQuantity + quantity - bogoAppliedQuantity) / quantity;
        }

        return totalPercentageOff;
    }

    public UserDTO findUserDtoById(UUID id) {
        User user = userService.findById(id);
        return UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .receiptDto(viewReceipt(user))
                .build();
    }

}
