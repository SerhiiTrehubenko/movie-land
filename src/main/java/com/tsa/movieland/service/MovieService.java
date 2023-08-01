package com.tsa.movieland.service;

import com.tsa.movieland.common.MovieRequest;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.Movie;

public interface MovieService {
    Iterable<Movie> findRandom();

    Iterable<Movie> findAll(MovieRequest defaultMovieRequest);

    Iterable<Movie> findByGenre(int genreId, MovieRequest defaultMovieRequest);

    MovieByIdDto getById(int movieId);
}
