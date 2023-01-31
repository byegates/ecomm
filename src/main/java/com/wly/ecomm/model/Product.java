package com.wly.ecomm.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinTable(
            name = "products_deals",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "deal_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Set<Deal> deals = new TreeSet<>(Comparator.comparingInt(SimpleIdBasedEntity::getId)); // For demo purpose only

    public void addDeal(Deal deal) {
        deals.add(deal);
    }

    public void removeDeal(Deal deal) {
        deals.remove(deal);
    }

    public void addDeals(List<Deal> dealList) {
        deals.addAll(dealList);
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
