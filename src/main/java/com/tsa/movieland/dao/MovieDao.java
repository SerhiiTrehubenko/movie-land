package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Movie;

import java.util.List;

public interface MovieDao {
    List<Movie> findAll();

    List<Movie> findRandom();

    List<Movie> findByGenreId(int genreId);

    Movie findById(int movieId);

    Movie findByIdForUpdate(int movieId);

    Movie save(Movie movie);
}
