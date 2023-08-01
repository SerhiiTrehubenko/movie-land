package com.tsa.movieland.dao;

import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.Movie;

public interface MovieDao {
    Iterable<Movie> findAll();

    Iterable<Movie> findAll(String column, String direction);

    Iterable<Movie> findRandom();

    Iterable<Movie> findByGenreId(int genreId);

    Iterable<Movie> findByGenreId(int genreId, String column, String direction);

    MovieByIdDto findById(int movieId);
}
