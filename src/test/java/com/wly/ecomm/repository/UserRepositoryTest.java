package com.wly.ecomm.repository;

import com.wly.ecomm.model.Role;
import com.wly.ecomm.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // enables h2 database directly
@ComponentScan(basePackages = "com.wly.ecomm.*")
class UserRepositoryTest {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Test
    @Disabled
    public void givenNA_whenStartOfApplication_then7UsersSaved() {
        assertEquals(7, userRepository.findAll().size());
    }

    public Role createRole() {
        return roleRepository.save(new Role("TEST"));
    }

    @Test
    @DirtiesContext
    public void givenUserObject_whenSave_thenSavedUserEqualsOriginalUser() {
        User user = new User("TestCase@junit.org", "Test", "Case");
        user.addRole(createRole());
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertInstanceOf(UUID.class, savedUser.getId());

        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getFirstName(), savedUser.getFirstName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        assertEquals(user.getRoles(), savedUser.getRoles());
    }
}