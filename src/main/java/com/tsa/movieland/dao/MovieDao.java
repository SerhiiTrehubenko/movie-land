package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Movie;

public interface MovieDao {
    Iterable<Movie> findAll();

    Iterable<Movie> findAllSorted(String column, String direction);

    Iterable<Movie> findRandom();

    Iterable<Movie> findByGenreId(int genreId);

    Iterable<Movie> findByGenreIdSorted(int genreId, String column, String direction);
}
