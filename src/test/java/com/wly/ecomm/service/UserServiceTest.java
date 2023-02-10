package com.wly.ecomm.service;

import com.wly.ecomm.model.User;
import com.wly.ecomm.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/*
 * Auto mock injection with Mockito
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository repository;

    @InjectMocks private UserService service;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("test-user@junit_mockito.com", "TEST-USER1", "TEST-LAST");
        user.setId(new UUID(1,1));
    }

    @AfterEach
    void tearDown() {
    }

    @Test @DisplayName("find all method, no real logics right now")
    void findAll() {
        given(repository.findAll()).willReturn(List.of(new User()));
        assertTrue(service.findAll().size() > 0);
    }

    @Test @DisplayName("Find user by ID - success")
    void findById_good() {
        given(repository.findById(any(UUID.class))).willReturn(Optional.ofNullable(user));
        var maybeUser = service.findById(user.getId());
        assertTrue(maybeUser.isPresent());
        assertEquals(user, maybeUser.get());
    }

    @Test @DisplayName("Find user by ID - Exception")
    void findById_notFound() {
        given(repository.findById(any(UUID.class))).willReturn(Optional.empty());
        assertTrue(service.findById(user.getId()).isEmpty());
//        assertThrows(UserDefinedException.class, () -> service.findById(user.getId()));
    }

    @Test @DisplayName("delete a user by id")
    void deleteById() {
        service.deleteById(user.getId());
        given(repository.findById(any(UUID.class))).willReturn(Optional.empty());
        assertTrue(service.findById(user.getId()).isEmpty());
//        assertThrows(UserDefinedException.class, () -> service.findById(user.getId()));
    }
}