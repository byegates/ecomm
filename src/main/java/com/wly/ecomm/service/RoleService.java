package com.wly.ecomm.service;

import com.wly.ecomm.model.Role;
import com.wly.ecomm.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleService {
    private RoleRepository repository;

    public List<Role> findAll() {
        return repository.findAll();
    }

    public Optional<Role> findById(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public Role merge(Role role) {
        var existingRole = repository.findByName(role.getName().toUpperCase());
        if (existingRole != null) return existingRole;
        return repository.save(role);
    }

    @Transactional
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

}
