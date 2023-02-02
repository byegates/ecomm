package com.wly.ecomm.api;


import com.wly.ecomm.dto.ReceiptDto;
import com.wly.ecomm.dto.UserDTO;
import com.wly.ecomm.model.CartItem;
import com.wly.ecomm.model.User;
import com.wly.ecomm.service.ShoppingCartService;
import com.wly.ecomm.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService service;
    private final ShoppingCartService shoppingCartService;

    @GetMapping()
    public List<User> findAll() {
        return service.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return service.save(user);
    }

    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable UUID id) {
        return shoppingCartService.findUserDtoById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable UUID id) {
        service.deleteById(id);
    }

    @GetMapping("/{id}/cart")
    public List<CartItem> findByUser(@PathVariable UUID id) {
        return shoppingCartService.findByUser(service.findById(id));
    }

    @PostMapping("/{userId}/cart/add/{productId}/{quantity}")
    public CartItem addProductToCart(@PathVariable UUID userId,
                                   @PathVariable Integer productId,
                                   @PathVariable Integer quantity) {

        return shoppingCartService.addProduct(userId, productId, quantity);
    }

    @PostMapping("/{userId}/cart/update/{productId}/{quantity}")
    public void updateQuantity(@PathVariable UUID userId,
                                     @PathVariable Integer productId,
                                     @PathVariable Integer quantity) {
        shoppingCartService.updateQuantity(userId, productId, quantity);
    }

    @DeleteMapping("/{userId}/cart/remove/{productId}")
    public void updateQuantity(@PathVariable UUID userId, @PathVariable Integer productId) {

        shoppingCartService.deleteByUserAndProduct(userId, productId);
    }

    @GetMapping("/{id}/receipt")
    public ReceiptDto viewReceipt(@PathVariable UUID id) {
        return shoppingCartService.viewReceipt(service.findById(id));
    }

}
