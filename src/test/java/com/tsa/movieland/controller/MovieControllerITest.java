package com.tsa.movieland.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MovieControllerITest extends ControllerBaseTest {

    @Test
    public void shouldReturnAllMovies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movie")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-movies.json"), true));
    }

    @Test
    public void shouldReturnMoviesByGenre() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movie/genre/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movies-by-genre.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesSortByPriceAsc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movie")
                        .param("price", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-movie-sort-price-asc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesSortByPriceDesc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movie")
                        .param("price", "desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-movie-sort-price-desc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesSortByRatingAsc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movie")
                        .param("rating", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-movie-sort-rating-asc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesSortByRatingDesc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movie")
                        .param("rating", "desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-movie-sort-rating-desc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesByGenreSortByRatingAsc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movie/genre/3")
                        .param("rating", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movie-genre-sort-rating-asc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesByGenreSortByRatingDesc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movie/genre/3")
                        .param("rating", "desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movie-genre-sort-rating-desc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesByGenreSortByPriceAsc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movie/genre/3")
                        .param("price", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movie-genre-sort-price-asc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesByGenreSortByPriceDesc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/movie/genre/3")
                        .param("price", "desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movie-genre-sort-price-desc.json"), true));
    }
}
