package com.tsa.movieland.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class ControllerBaseTest {

    @Autowired
    protected MockMvc mockMvc;


    String getContent(String filePath) {
        try (InputStream file = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath)) {
            Objects.requireNonNull(file);
            return  new String(file.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("File with path: [%s] was not found".formatted(filePath),e);
        }
    }

}
