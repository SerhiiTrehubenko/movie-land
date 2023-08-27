package com.tsa.movieland.controller;

import com.tsa.movieland.CommonContainer;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GenreControllerITest extends CommonContainer {

    @Test
    public void shouldReturnAllGenres() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/genre")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-genres.json")));
    }
}
