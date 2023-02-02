package com.wly.ecomm.repository;

import com.wly.ecomm.model.Role;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // enables h2 database directly
@ComponentScan(basePackages = "com.wly.ecomm.*")
@Transactional
class RoleRepositoryTest {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleRepositoryTest(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Test
    @Disabled
    public void givenNA_whenStartOfApplication_then2RolesSaved() {
        assertEquals(2, roleRepository.findAll().size());
    }

    @Test
    public void givenRoleObject_whenSave_thenSavedRoleEqualsOriginalRole() {
        Role role = new Role("TEST");
        Role savedRole = roleRepository.save(role);

        assertNotNull(savedRole);
        assertNotNull(savedRole.getId());
        assertInstanceOf(Integer.class, savedRole.getId());
        assertTrue(savedRole.getId() > 0);

        assertEquals(role.getName(), savedRole.getName());
    }
}