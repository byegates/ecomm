package com.wly.ecomm.repository;

import com.wly.ecomm.model.CartItem;
import com.wly.ecomm.model.Product;
import com.wly.ecomm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    List<CartItem> findByUser(User user);

    CartItem findByUserAndProduct(User user, Product product);

    int deleteByUserAndProduct(User user, Product product);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.product.id = ?2 AND c.user.id = ?3")
    void updateQuantity(Integer quantity, Integer productId, UUID userId);
}
