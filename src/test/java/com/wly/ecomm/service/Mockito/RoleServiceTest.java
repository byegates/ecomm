package com.wly.ecomm.service.Mockito;

import com.wly.ecomm.exception.UserDefinedException;
import com.wly.ecomm.model.Role;
import com.wly.ecomm.repository.RoleRepository;
import com.wly.ecomm.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock private RoleRepository repository;
    @InjectMocks private RoleService service;

    private Role role;
    private static final String roleName = "ROLEServiceTEST_findALL".substring(0, 20);
    private static final int ID = 0x3f3f3f3f;

    @BeforeEach
    void setUp() {
        role = new Role(roleName);
        role.setId(ID);
    }

    @Test @DisplayName("Find all works fine")
    void findAll() {
        given(repository.findAll()).willReturn(List.of(role));
        assertTrue(service.findAll().size() > 0);
    }

    @Test @DisplayName("Find role by id")
    void findById() {
        given(repository.findById(ID)).willReturn(Optional.ofNullable(role));
        var foundRole = service.findById(role.getId());
        assertEquals(role, foundRole);
    }

    @Test @DisplayName("Save Role successfully")
    void create_1NewRole() {
        given(repository.save(role)).willReturn(role);
        assertEquals(role, service.merge(role));
    }

    @Test @DisplayName("delete a role by Id")
    void deleteById() {
        willDoNothing().given(repository).deleteById(ID); // optional
        service.deleteById(role.getId());
        verify(repository, times(1)).deleteById(ID);
        assertThrows(UserDefinedException.class, () -> service.findById(role.getId()));
    }

    @Test @DisplayName("Update name of saved Role")
    void updateRoleName () {
        role.setName("ROLEServiceTEST-U");
        given(repository.save(role)).willReturn(role);
        var updatedRole = service.merge(role);
        assertEquals(role, updatedRole);
    }
}