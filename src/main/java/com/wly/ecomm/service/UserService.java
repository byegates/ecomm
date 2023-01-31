package com.wly.ecomm.service;

import com.wly.ecomm.exception.UserDefinedException;
import com.wly.ecomm.model.User;
import com.wly.ecomm.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;

    private final RoleService roleService;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User save(User user) {
        return repository.save(user);
    }

    public User findById(UUID id) {
        Optional<User> maybeUser = repository.findById(id);
        if (maybeUser.isEmpty()) {
            throw new UserDefinedException(String.format("User not found with id: %s", id));
        }
        return maybeUser.get();
    }

    /**
     * Initialize our h2 testing database with some starting admin and customer users for demo purpose
     * @return List of User (customers actually), for ShoppingCart service use to add some cart items for demo purpose
     */
    public List<User> initUser() {
        var roles = roleService.initRole();

        // add 4 customers
        var customers = List.of(
                new User("customer1@customer_domain.com", "Customer1", "Customer"),
                new User("customer2@customer_domain.com", "Customer2", "Customer"),
                new User("customer3@customer_domain.com", "Customer3", "Customer"),
                new User("customer4@customer_domain.com", "Customer4", "Customer"),
                new User("customer0@customer_domain.com", "Customer0", "Customer")
        );

        customers.forEach(user -> user.addRole(roles.get(1)));
        repository.saveAll(customers);

        // add 2 admin users
        var admins = List.of(
                new User("admin1@domain.com", "Admin1", "admin"),
                new User("admin@domain.com", "admin", "admin")
        );

        admins.forEach(user -> user.addRole(roles.get(0)));
        repository.saveAll(admins);

        return customers;
    }
}
