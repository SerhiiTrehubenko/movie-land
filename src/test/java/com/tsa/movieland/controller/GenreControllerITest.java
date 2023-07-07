package com.tsa.movieland.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class GenreControllerITest extends ControllerBaseTest {
    private final String allGenres = "/api/v1/genre";

    @Test
    public void shouldReturnAllGenres() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(allGenres)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-genres.json")));
    }
}
