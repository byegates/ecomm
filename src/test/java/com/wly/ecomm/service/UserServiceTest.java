package com.wly.ecomm.service;

import com.wly.ecomm.exception.UserDefinedException;
import com.wly.ecomm.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
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
class UserServiceTest {

    @Autowired private UserService service;

    private User user;

    @BeforeEach
    void setUp() {
        user = service.save(new User("UserServiceTest_findAll", "TEST1", "LAST"));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
        assertTrue(service.findAll().size() > 0);
    }

    @Test
    void findById() {
        assertNotNull(service.findById(user.getId()));
        assertEquals(user.getId(), service.findById(user.getId()).getId());
    }

    @Test
    void deleteById() {
        service.deleteById(user.getId());
        assertThrows(UserDefinedException.class, () -> service.findById(user.getId()));
    }
}