package com.tsa.movieland.controller;

import com.tsa.movieland.SecurityContainer;
import com.tsa.movieland.service.CredentialsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerITest extends SecurityContainer {

    @Autowired
    private CredentialsService credentialsService;

    @Test
    void ShouldAddReviewRoleUser() throws Exception {
        setCredentialsUserAndAdmin(credentialsService);

        String requestBodyUser = """
                {
                    "userEmail" : "ronald.reynolds66@example.com",
                    "password" : "password"
                }
                """;
        String requestBodyReview = """
                {
                    "movieId" : 1112,
                    "text" : "review TEST"
                }
                """;

        String token = getToken(requestBodyUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/review")
                        .header(AUTHORIZATION, "Bearer: " + token)
                        .content(requestBodyReview)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void ShouldAddReviewRoleAdmin() throws Exception {
        setCredentialsUserAndAdmin(credentialsService);

        String requestBodyAdmin = """
                {
                    "userEmail" : "darlene.edwards15@example.com",
                    "password" : "password"
                }
                """;
        String requestBodyReview = """
                {
                    "movieId" : 1112,
                    "text" : "review TEST"
                }
                """;

        String token = getToken(requestBodyAdmin);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/review")
                        .header(AUTHORIZATION, "Bearer: " + token)
                        .content(requestBodyReview)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}