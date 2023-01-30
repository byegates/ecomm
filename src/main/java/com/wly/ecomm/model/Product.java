package com.wly.ecomm.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Product extends SimpleIdBasedEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "products_deals",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "deal_id")
    )
    private final Set<Deal> deals = new TreeSet<>(Comparator.comparingInt(SimpleIdBasedEntity::getId));

    public boolean addDeal(Deal deal) {
        return deals.add(deal);
    }

    public boolean removeDeal(Deal deal) {
        return deals.remove(deal);
    }

    public void clearDeal() {
        deals.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return name.equals(product.name) && price.equals(product.price) && id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
