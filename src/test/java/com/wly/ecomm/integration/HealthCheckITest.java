package com.wly.ecomm.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class HealthCheckITest {
    @Autowired private MockMvc mockMvc;

    @Test @DisplayName("Health Check is returning 200 status and string 'OK'")
    void healthCheck() throws Exception {
        // given pre-condition or set
        // NA

        // when - action or behavior that we are going to test
        var response = mockMvc.perform(get("/"));

        // then - verify the result or output using assert statement
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("OK")))
                ;

    }
}
