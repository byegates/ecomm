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
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    @Transactional
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

    @Transactional
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

}
