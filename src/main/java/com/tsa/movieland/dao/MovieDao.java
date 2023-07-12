package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Movie;

public interface MovieDao {
    Iterable<Movie> findAll();
    Iterable<Movie> findAllSortPrice(String direction);
    Iterable<Movie> findAllSortRating(String direction);

    Iterable<Movie> findRandom();
    Iterable<Movie> findByGenreId(int genreId);

    Iterable<Movie> findByGenreIdSortPrice(int genreId, String direction);
    Iterable<Movie> findByGenreIdSortRating(int genreId, String direction);
}
