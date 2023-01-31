package com.wly.ecomm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Deal extends SimpleIdBasedEntity {
    @Column(nullable = false, length = 25)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    @JoinTable(
            name = "products_deals",
            joinColumns = @JoinColumn(name = "deal_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Set<Product> productSet = new HashSet<>();

    public void addProduct(Product product) {
        productSet.add(product);
    }

    public void removeProduct(Product product) {
        productSet.remove(product);
    }

    public void addProducts(List<Product> productList) {
        productSet.addAll(productList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Deal deal = (Deal) o;
        return id != null && Objects.equals(id, deal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
