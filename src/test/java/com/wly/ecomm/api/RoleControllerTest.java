package com.wly.ecomm.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wly.ecomm.model.Role;
import com.wly.ecomm.service.RoleService;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoleController.class)
class RoleControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean RoleService service;

    private static final String roleName = "T-ADMIN";

    @Test @DisplayName("Find all roles")
    void findAll() throws Exception {

        // given: pre-condition or setup
        var roles = List.of(new Role(roleName), new Role("T-CUSTOMER"));
        given(service.findAll()).willReturn(roles);

        // when: action or behavior we are going to test
        ResultActions response = mockMvc.perform(get("/roles"));

        // then: verify the result or output using assert statement
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(roles.size())))
                .andDo(print())
                ;

    }

    @Test @DisplayName("Find a role by id")
    void findById_found() throws Exception {

        // given: pre-condition or setup
        int id = 1;
        given(service.findById(id)).willReturn(Optional.of(new Role(roleName)));

        // when: action or behavior we are going to test
        ResultActions response = mockMvc.perform(get("/roles/{id}", id));

        // then: verify the result or output using assert statement
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(roleName)))
        ;

    }

    @Test @DisplayName("Fail to find a role by id")
    void findById_notFound() throws Exception {

        // given: pre-condition or setup
        int id = 1;
        given(service.findById(id)).willReturn(Optional.empty());

        // when: action or behavior we are going to test
        ResultActions response = mockMvc.perform(get("/roles/{id}", id));

        // then: verify the result or output using assert statement
        response.andExpect(status().isNotFound())
                .andDo(print())
        ;

    }

    @Test @DisplayName("Create a role")
    void create_a_newRole() throws Exception {

        // given: pre-condition or setup
        var role = new Role(roleName);
        when(service.merge(any(Role.class))).then(AdditionalAnswers.returnsFirstArg());

        String json = objectMapper.writeValueAsString(role);

        // when: action or behavior we are going to test
        ResultActions response = mockMvc.perform(
                post("/roles").contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        // then: verify the result or output using assert statement
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(role.getName())))
                .andExpect(jsonPath("$.id", is(IsNull.nullValue())))
        ;

    }

    @Test @DisplayName("Update an existing role")
    void updateById() throws Exception {
        // given: pre-condition or setup
        int id = 1;
        var role = new Role(roleName);
        given(service.findById(id)).willReturn(Optional.of(role));
        given(service.merge(any(Role.class))).willReturn(role);

        // when: action or behavior we are going to test
        ResultActions response = mockMvc.perform(
                put("/roles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role))
        );

        // then: verify the result or output using assert statement
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(roleName)))
        ;

    }

    @Test @DisplayName("Update an employee, but the id is not found")
    void updateById_notFound() throws Exception {

        // given: pre-condition or setup
        int id = 1;
        var role = new Role(roleName);
        given(service.findById(id)).willReturn(Optional.empty());

        // when: action or behavior we are going to test
        ResultActions response = mockMvc.perform(
                put("/roles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role))
        );

        // then: verify the result or output using assert statement
        response.andExpect(status().isNotFound())
                .andDo(print())
        ;

    }

    @Test @DisplayName("Delete by id, get a 200 response")
    void deleteById() throws Exception {

        // given: pre-condition or setup
        int id = 1;
        willDoNothing().given(service).deleteById(id);

        // when: action or behavior we are going to test
        ResultActions response = mockMvc.perform(delete("/roles/{id}", id));

        // then: verify the result or output using assert statement
        response.andExpect(status().isOk())
                .andDo(print())
        ;

    }
}