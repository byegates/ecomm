package com.wly.ecomm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

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
