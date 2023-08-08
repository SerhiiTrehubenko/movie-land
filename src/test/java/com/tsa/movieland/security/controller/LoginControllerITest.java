package com.tsa.movieland.security.controller;

import com.tsa.movieland.controller.ControllerBaseTest;
import com.tsa.movieland.service.CredentialsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerITest extends ControllerBaseTest {
    @Autowired
    private CredentialsService credentialsService;

    @Test
    void shouldAuthenticateUser() throws Exception {
        String requestBody = """
                {
                    "userEmail" : "ronald.reynolds66@example.com",
                    "password" : "password"
                }""";
        setCredentials(credentialsService);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists());
    }

    @Test
    void shouldResponse403HttpStatusWhenWrongPassword() throws Exception {
        String requestBody = """
                {
                    "userEmail" : "ronald.reynolds66@example.com",
                    "password" : "passwordddddd"
                }""";
        setCredentials(credentialsService);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldResponse403HttpStatusWhenWrongEmail() throws Exception {
        String requestBody = """
                {
                    "userEmail" : "ronald.eynolds66@example.com",
                    "password" : "password"
                }""";
        setCredentials(credentialsService);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldResponse403HttpStatusWhenWrongEmailAndPassword() throws Exception {
        String requestBody = """
                {
                    "userEmail" : "ronald.eynolds66@example.com",
                    "password" : "passwordssss"
                }""";
        setCredentials(credentialsService);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}