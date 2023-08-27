package com.tsa.movieland;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test", "no-secure"})
public class BaseTest {
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
}
