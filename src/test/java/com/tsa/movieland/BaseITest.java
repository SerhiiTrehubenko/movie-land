package com.tsa.movieland;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class BaseITest {
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
}
