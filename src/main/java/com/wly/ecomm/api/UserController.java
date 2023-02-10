package com.wly.ecomm.api;


import com.wly.ecomm.dto.ReceiptDto;
import com.wly.ecomm.dto.UserDTO;
import com.wly.ecomm.model.CartItem;
import com.wly.ecomm.model.User;
import com.wly.ecomm.service.ShoppingCartService;
import com.wly.ecomm.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        return service.save(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable UUID id) {
        return shoppingCartService.findUserDtoById(id)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable UUID id) {
        service.deleteById(id);
    }

    @GetMapping("/{id}/cart")
    public ResponseEntity<List<CartItem>> findCartByUser(@PathVariable UUID id) {
        return service.findById(id)
                .map(user -> new ResponseEntity<>(shoppingCartService.findByUser(user), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>()));
    }

    @PostMapping("/{userId}/cart/add/{productId}/{quantity}")
    public CartItem addProductToCart(@PathVariable UUID userId,
                                   @PathVariable Integer productId,
                                   @PathVariable Integer quantity) {
        return shoppingCartService.addProduct(userId, productId, quantity);
    }

    @PatchMapping("/{userId}/cart/update/{productId}/{quantity}")
    public void updateQuantity(@PathVariable("userId") UUID userId,
                                     @PathVariable("productId") Integer productId,
                                     @PathVariable("quantity") Integer quantity) {
        shoppingCartService.updateQuantity(userId, productId, quantity);
    }

    @DeleteMapping("/{userId}/cart/remove/{productId}")
    public void removeProduct(@PathVariable UUID userId, @PathVariable Integer productId) {

        shoppingCartService.deleteByUserAndProduct(userId, productId);
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<ReceiptDto> viewReceipt(@PathVariable UUID id) {
        return service.findById(id)
                .map(user -> new ResponseEntity<>(shoppingCartService.viewReceipt(user), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ReceiptDto.builder().build()))
                ;
    }

}
