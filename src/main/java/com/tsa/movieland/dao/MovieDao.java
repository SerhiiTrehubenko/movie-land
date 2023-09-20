package com.tsa.movieland.dao;

import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.dto.MovieFindAllDto;

public interface MovieDao {
    Iterable<MovieFindAllDto> findAll();

    Iterable<MovieFindAllDto> findAll(String column, String direction);

    Iterable<MovieFindAllDto> findRandom();

    Iterable<MovieFindAllDto> findByGenreId(int genreId);

    Iterable<MovieFindAllDto> findByGenreId(int genreId, String column, String direction);

    MovieByIdDto findById(int movieId);

    int save(AddUpdateMovieDto movie);
    void update(int movieId, AddUpdateMovieDto movie);
}
