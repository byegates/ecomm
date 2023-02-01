package com.wly.ecomm.service;

import com.wly.ecomm.model.Role;
import com.wly.ecomm.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {
    private RoleRepository repository;

    public List<Role> listAllRoles() {
        return repository.findAll();
    }

    public List<Role> initRole() {
        return repository.saveAll(List.of(
                new Role("ADMIN"),
                new Role("CUSTOMER")
        ));
    }
}
