package com.tsa.movieland.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.tsa.movieland.BaseITest;
import com.tsa.movieland.common.Role;
import com.tsa.movieland.entity.Credentials;
import com.tsa.movieland.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DBRider
@DataSet(value = {
        "datasets/users/dataset-users.json",
        "datasets/genres/dataset-genres.json",
        "datasets/movies/dataset-movies.json",
        "datasets/countries/dataset-countries.json",
        "datasets/jointable/dataset-movies_genres.json",
        "datasets/jointable/dataset-movies_ratings.json",
        "datasets/jointable/dataset-movies_countries.json",
        "datasets/jointable/dataset-movie_reviews.json",
        "datasets/posters/dataset-posters.json"
},
        cleanAfter = true, cleanBefore = true,
        skipCleaningFor = "flyway_schema_history",
        executeStatementsBefore = {
                "ALTER SEQUENCE IF EXISTS movies_id RESTART WITH 1126;"
        }
)
public abstract class ControllerBaseTest extends BaseITest {

    @Autowired
    protected MockMvc mockMvc;


    protected String getContent(String filePath) {
        try (InputStream file = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath)) {
            Objects.requireNonNull(file);
            return new String(file.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("File with path: [%s] was not found".formatted(filePath), e);
        }
    }

    protected void setCredentialsUserAndAdmin(CredentialsService credentialsService) {
        final Credentials credentialsFirst = Credentials.builder()
                .userId(1000002)
                .password("password")
                .role(Role.ADMIN)
                .build();
        credentialsService.save(credentialsFirst);

        final Credentials credentialsSecond = Credentials.builder()
                .userId(1000003)
                .password("password")
                .role(Role.USER)
                .build();
        credentialsService.save(credentialsSecond);
    }

    protected String getToken(String requestBody) throws Exception {
        String responseBody = mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        return responseBody.substring(13, responseBody.length() - 2);
    }
}
