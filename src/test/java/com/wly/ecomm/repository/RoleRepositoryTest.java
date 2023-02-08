package com.wly.ecomm.repository;

import com.wly.ecomm.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest // enables h2 database directly
@ComponentScan(basePackages = "com.wly.ecomm.*")
class RoleRepositoryTest {
    @Autowired private RoleRepository repository;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role("TEST-ROLE");
    }

    @Test @Disabled
    public void givenNA_whenStartOfApplication_then2RolesSaved() {
        assertEquals(2, repository.findAll().size());
    }

    @Test @DisplayName("Save an object")
    public void givenRoleObject_whenSave_thenSavedRoleEqualsOriginalRole() {
        assertEquals(role, repository.save(role));
    }

    @Test @DisplayName("Update name of an saved role")
    public void givenRoleObject_whenUpdate_thenSameObjectIsUpdated() {
        var savedRole = repository.save(role);
        assertEquals(role, savedRole);
        role.setName("TEST-UPDATED");
        var updatedRole = repository.save(role);
        assertEquals(savedRole, updatedRole);
    }
}