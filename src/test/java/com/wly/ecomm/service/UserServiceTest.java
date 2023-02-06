package com.wly.ecomm.service;

import com.wly.ecomm.exception.UserDefinedException;
import com.wly.ecomm.utils.TestUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ComponentScan(basePackages = "com.wly.ecomm.*")
@AutoConfigureTestDatabase
class UserServiceTest {

    @Autowired private UserService service;
    @Autowired private TestUtil testUtil;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test @Transactional
    void findAll() {
        var user = testUtil.getUser("UserServiceTest_FindAll");
        log.info("Users : {}", user);
        assertTrue(service.findAll().size() > 0);
    }

    @Test @Transactional
    void findById() {
        var user = testUtil.getUser("UserServiceTest_findById");
        assertNotNull(service.findById(user.getId()));
        assertEquals(user.getId(), service.findById(user.getId()).getId());
    }

    @Test @Transactional
    void deleteById() {
        var user = testUtil.getUser("UserServiceTest_deleteById");
        service.deleteById(user.getId());
        assertThrows(UserDefinedException.class, () -> service.findById(user.getId()));
    }
}