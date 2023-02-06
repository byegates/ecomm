package com.wly.ecomm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @ToString @Getter @Setter
public class Deal extends SimpleIdBasedEntity {

    @Size(max = 25, message = "Deal Name can't have more than 25 characters")
    @Size(min = 4, message = "Deal Name can't have less than 4 characters")
    @Column(nullable = false, length = 25)
    private String name;

    @Size(min = 5, message = "Deal description can't have less than 5 characters")
    @Column(nullable = false)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "products_deals",
            joinColumns = @JoinColumn(name = "deal_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @ToString.Exclude
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Set<Product> products = new HashSet<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public void addProducts(Collection<Product> products) {
        this.products.addAll(products);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Deal deal = (Deal) o;
        return id != null && id.equals(deal.id) && name.equals(deal.name) && description.equals(deal.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
