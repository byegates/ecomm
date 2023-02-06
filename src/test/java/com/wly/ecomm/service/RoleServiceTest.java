package com.wly.ecomm.service;

import com.wly.ecomm.exception.UserDefinedException;
import com.wly.ecomm.model.Role;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ComponentScan(basePackages = "com.wly.ecomm.*")
@AutoConfigureTestDatabase
@Transactional
class RoleServiceTest {
    @Autowired private RoleService service;

    private Role role;
    private static final String roleName = "ROLEServiceTEST_findALL".substring(0, 20);

    @BeforeEach
    void setUp() {
        role = service.merge(new Role(roleName));
    }

    @Test
    void findAll() {
        assertTrue(service.findAll().size() > 0);
    }

    @Test
    void findById() {
        var foundRole = service.findById(role.getId());
        assertNotNull(foundRole);
        assertEquals(role.getId(), foundRole.getId());
    }

    @Test
    void create_1NewRole() {
        assertNotNull(role);
    }

    @Test
    void create_ignore_case() {
        assertEquals("RoleServiceTest_findALL".substring(0, 20).toUpperCase(), role.getName());
    }

    @Test
    void create_duplicateRole_Ignored() {
        var role2 = service.merge(new Role(roleName));
        assertEquals(role.getId(), role2.getId());
        assertEquals(role.getName(), role2.getName());
        assertEquals(roleName.toUpperCase(), role2.getName());
    }

    @Test
    void deleteById() {
        service.deleteById(role.getId());
        assertThrows(UserDefinedException.class, () -> service.findById(role.getId()));
    }
}