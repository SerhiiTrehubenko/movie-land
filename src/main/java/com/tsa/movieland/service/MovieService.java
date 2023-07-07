package com.tsa.movieland.service;

import com.tsa.movieland.entity.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> findAll();
    List<Movie> findAll(String rating, String price);
    List<Movie> findThreeRandom();
    List<Movie> findByGenre(int genreId, String rating, String price);
}
