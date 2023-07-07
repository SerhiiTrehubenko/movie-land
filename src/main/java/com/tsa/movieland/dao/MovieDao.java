package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Movie;

import java.util.List;

public interface MovieDao {
    List<Movie> findAll();

    List<Movie> findThreeRandom();

    List<Movie> findByGenreId(int genreId);
}
