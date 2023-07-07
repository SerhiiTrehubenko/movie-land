package com.tsa.movieland.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MovieControllerITest extends ControllerBaseTest {
    private final String allMovie = "/api/v1/movie";
    private final String byGenre = "/api/v1/movie/genre/3";

    private final String allMovieSortPriceAsc = allMovie + "?price=asc";
    private final String allMovieSortPriceDesc = allMovie + "?price=desc";

    private final String allMovieSortRatingAsc = allMovie + "?rating=asc";
    private final String allMovieSortRatingDesc = allMovie + "?rating=desc";

    private final String movieByGenreSortRatingAsc = byGenre + "?rating=asc";
    private final String movieByGenreSortRatingDesc = byGenre + "?rating=desc";
    private final String movieByGenreSortPriceAsc = byGenre + "?price=asc";
    private final String movieByGenreSortPriceDesc = byGenre + "?price=desc";

    @Test
    public void shouldReturnAllMovies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(allMovie)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-movies.json"), true));
    }

    @Test
    public void shouldReturnMoviesByGenre() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(byGenre)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movies-by-genre.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesSortByPriceAsc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(allMovieSortPriceAsc)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-movie-sort-price-asc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesSortByPriceDesc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(allMovieSortPriceDesc)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-movie-sort-price-desc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesSortByRatingAsc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(allMovieSortRatingAsc)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-movie-sort-rating-asc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesSortByRatingDesc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(allMovieSortRatingDesc)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/all-movie-sort-rating-desc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesByGenreSortByRatingAsc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(movieByGenreSortRatingAsc)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movie-genre-sort-rating-asc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesByGenreSortByRatingDesc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(movieByGenreSortRatingDesc)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movie-genre-sort-rating-desc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesByGenreSortByPriceAsc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(movieByGenreSortPriceAsc)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movie-genre-sort-price-asc.json"), true));
    }

    @Test
    public void shouldReturnAllMoviesByGenreSortByPriceDesc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(movieByGenreSortPriceDesc)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getContent("mock/movie-genre-sort-price-desc.json"), true));
    }
}
