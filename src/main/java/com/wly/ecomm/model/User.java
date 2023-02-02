package com.wly.ecomm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor @AllArgsConstructor @ToString @Getter @Setter
@Table(name = "users") // user is a keyword in h2, using users as table name
public class User extends UuidBasedEntity {

    @Column(length = 254, nullable = false, unique = true)
    private String email;

    @Column(length = 45, nullable = false)
    private String firstName;

    @Column(length = 45, nullable = false)
    private String lastName;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private final Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private final Set<CartItem> cartItems = new HashSet<>();


    public boolean addRole(Role role) {
        return roles.add(role);
    }
    public boolean addRoles(Collection<Role> rolesCollection) {
        return roles.addAll(rolesCollection);
    }

    public boolean removeRole(Role role) {
        return roles.remove(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email) && firstName.equals(user.firstName) && lastName.equals(user.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
