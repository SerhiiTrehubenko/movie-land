package com.tsa.movieland.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MovieControllerITest extends ControllerBaseTest {

    @Test
    public void shouldReturnAllMovies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movie")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-movies.json"), true));
    }

    @Test
    void shouldReturnRandomNumberMovies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movie/random")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(3));
    }

    @Test
    public void shouldReturnMoviesByGenre() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movie/genre/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movies-by-genre.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesSortByPriceAsc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movie")
                        .param("price", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-movie-sort-price-asc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesSortByPriceDesc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movie")
                        .param("price", "desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-movie-sort-price-desc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesSortByRatingAsc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movie")
                        .param("rating", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-movie-sort-rating-asc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesSortByRatingDesc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movie")
                        .param("rating", "desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-movie-sort-rating-desc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesByGenreSortByRatingAsc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movie/genre/3")
                        .param("rating", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movie-genre-sort-rating-asc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesByGenreSortByRatingDesc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movie/genre/3")
                        .param("rating", "desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movie-genre-sort-rating-desc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesByGenreSortByPriceAsc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movie/genre/3")
                        .param("price", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movie-genre-sort-price-asc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesByGenreSortByPriceDesc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movie/genre/3")
                        .param("price", "desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movie-genre-sort-price-desc.json"), true));
    }

    @Test
    void shouldReturnMovieByIdPriceUAH() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movie/1112")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movie-by-id.json"), true));
    }

    @Test
    void shouldReturnMovieByIdPriceUSD() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movie/1112")
                        .param("currency", "usd")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movie-by-id-usd.json"), true));
    }

    @Test
    void shouldReturnMovieByIdPriceEUR() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/movie/1112")
                        .param("currency", "eur")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movie-by-id-eur.json"), true));
    }
}
