package com.tsa.movieland.security.controller;

import com.tsa.movieland.controller.ControllerBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RegistrationControllerITest extends ControllerBaseTest {

    @Test
    void shouldRegisterNewUser() throws Exception {
        String requestBody = """
                {
                    "firstName" : "Serhii",
                    "lastName" : "Trehubenko",
                    "nickname" : "Trehubenko",
                    "email" : "tsa85.ca@gmail.com",
                    "password" : "888"
                }""";


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/registration")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists());
    }
}