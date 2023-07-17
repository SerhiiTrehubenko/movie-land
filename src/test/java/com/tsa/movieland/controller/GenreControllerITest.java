package com.tsa.movieland.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class GenreControllerITest extends ControllerBaseTest {

    @Test
    // @DBRider initialize DB after <GenreCache.class> fetch Data From DB as result empty list of GENRES
    // When using <JdbcGenreDao.class directly> all works fine
    @Disabled
    public void shouldReturnAllGenres() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/genre")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-genres.json")));
    }
}
