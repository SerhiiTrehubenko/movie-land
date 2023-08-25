package com.tsa.movieland.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("no-secure")
@Disabled
public class GenreControllerITest extends ControllerBaseTest {

    @Test
    public void shouldReturnAllGenres() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/genre")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-genres.json")));
    }
}
