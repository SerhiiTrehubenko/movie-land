package com.tsa.movieland.security.controller;

import com.tsa.movieland.SecurityContainer;
import com.tsa.movieland.service.CredentialsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerITest extends SecurityContainer {
    @Autowired
    private CredentialsService credentialsService;

    @Test
    void shouldAuthenticateUser() throws Exception {
        String requestBody = """
                {
                    "userEmail" : "ronald.reynolds66@example.com",
                    "password" : "password"
                }""";
        setCredentialsUserAndAdmin(credentialsService);

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
        setCredentialsUserAndAdmin(credentialsService);

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
        setCredentialsUserAndAdmin(credentialsService);

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
        setCredentialsUserAndAdmin(credentialsService);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}