package com.tsa.movieland.controller;

import com.tsa.movieland.CommonContainer;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("no-secure")
class CountryControllerITest extends CommonContainer {

    @Test
    void shouldGetAllCountries() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/country")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-countries.json"), true));
    }
}