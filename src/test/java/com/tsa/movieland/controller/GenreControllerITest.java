package com.tsa.movieland.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("no-secure")
public class GenreControllerITest extends ControllerBaseTest {

    @Test
    // @DBRider initialize DB after <CachedGenreDao.class> fetch Data From DB as result empty list of GENRES
    // When using <JdbcGenreDao.class directly> all works fine
    // I tried BeanPostProcessor and EventListener approaches, but they do not work too;
    // I tried to use @ExtendWith(DBUnitExtension.class) and it failed ether;

    public void shouldReturnAllGenres() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/genre")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-genres.json")));
    }
}
