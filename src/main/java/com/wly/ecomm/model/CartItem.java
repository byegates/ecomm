package com.wly.ecomm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor @ToString @Getter @Setter
public class CartItem {

    @EmbeddedId
    private CartItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("productId")
    private Product product;

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
