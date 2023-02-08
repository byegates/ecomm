package com.wly.ecomm.api;

import com.wly.ecomm.model.Role;
import com.wly.ecomm.service.RoleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Role> findById(@PathVariable("id") Integer id) {
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Role create(@Valid @RequestBody Role role) {
        return service.merge(role);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Integer id) {
        service.deleteById(id);
        return new ResponseEntity<>("Role successfully deleted by ID!", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateById(@PathVariable("id") Integer id, @RequestBody Role role) {
        return service.findById(id).map(
                updatedRole -> {
                    updatedRole.setName(role.getName().toUpperCase());
                    return new ResponseEntity<>(service.merge(updatedRole), HttpStatus.OK);
                }
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
