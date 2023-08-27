package com.tsa.movieland;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.tsa.movieland.common.Role;
import com.tsa.movieland.entity.Credentials;
import com.tsa.movieland.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

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
                "ALTER SEQUENCE IF EXISTS users_id RESTART WITH 1000015;",
                "ALTER SEQUENCE IF EXISTS movies_id RESTART WITH 1126;"
        }
)
@Testcontainers
@SpringBootTest
@ActiveProfiles(value = {"test"})
@AutoConfigureMockMvc
public class SecurityContainer {

    @Autowired
    protected MockMvc mockMvc;
    private static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER;

    static {
        POSTGRES_SQL_CONTAINER =
                new PostgreSQLContainer<>("postgres:14.4")
                        .withDatabaseName("movie_land_dev")
                        .withExposedPorts(5432, 5433)
                        .withUsername("postgres")
                        .withPassword("password");
        POSTGRES_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER::getPassword);
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
