package com.wly.ecomm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor @AllArgsConstructor @ToString @Getter @Setter
public class Role extends SimpleIdBasedEntity {
    @Column(length = 40, nullable = false, unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    @ToString.Exclude
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Set<User> users = new HashSet<>();

    @PrePersist
    private void setNameUpperCase() {
        name = name.toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Role role = (Role) o;
        return id != null && Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
