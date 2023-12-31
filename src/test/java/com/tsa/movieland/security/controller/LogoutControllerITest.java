package com.tsa.movieland.security.controller;

import com.tsa.movieland.SecurityContainer;
import com.tsa.movieland.service.CredentialsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LogoutControllerITest extends SecurityContainer {

    @Autowired
    private CredentialsService credentialsService;

    @Test
    void shouldLogoutUser() throws Exception {
        String requestBody = """
                {
                    "userEmail" : "ronald.reynolds66@example.com",
                    "password" : "password"
                }""";
        setCredentialsUserAndAdmin(credentialsService);

        String token = getToken(requestBody);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/logout")
                        .header(AUTHORIZATION, "Bearer: " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").value("You have been successfully logout"));
    }

    @Test
    void shouldReturn403HttpStatusWhenWithoutAuthorizationHeader() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturn400HttpStatusWhenUserIsAbsentAmongAuthorized() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb25hbGQucmV5bm9sZHM2NkBleGFtcGxlLmNvbSIsImlhdCI6MTY5MTQxNDM2OSwiZXhwIjoxNjkxNDIxNTY5fQ.slq_kFmobsz-XU1qtaBZIZEJ0rm1CLROtQSjUntGF00";
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/logout")
                        .header(AUTHORIZATION, "Bearer: " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}