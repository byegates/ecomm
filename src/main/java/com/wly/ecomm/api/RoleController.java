package com.wly.ecomm.api;

import com.wly.ecomm.model.Role;
import com.wly.ecomm.service.RoleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@AllArgsConstructor
public class RoleController {
    private final RoleService service;

    @GetMapping()
    public List<Role> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Role findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public Role create(@Valid @RequestBody Role role) {
        return service.merge(role);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id) {
        service.deleteById(id);
    }
}
