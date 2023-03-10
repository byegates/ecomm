package com.wly.ecomm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor @ToString @Getter @Setter
public class CartItem {

    @EmbeddedId
    @JsonIgnore
    private CartItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("productId")
    private Product product;

    @Positive(message = "Item quantity must be positive.")
    private int quantity;

    public CartItem(User user, Product product) {
        this.id = new CartItemId(user.getId(), product.getId());
        this.user = user;
        this.product = product;
    }

    public CartItem(User user, Product product, int quantity) {
        this.id = new CartItemId(user.getId(), product.getId());
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }
}
