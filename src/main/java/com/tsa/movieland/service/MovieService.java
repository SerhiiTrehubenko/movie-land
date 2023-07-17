package com.tsa.movieland.service;

import com.tsa.movieland.domain.MovieRequest;
import com.tsa.movieland.entity.Movie;

public interface MovieService {
    Iterable<Movie> findRandom();

    Iterable<Movie> findAllSorted(MovieRequest movieRequest);

    Iterable<Movie> findByGenreSorted(int genreId, MovieRequest movieRequest);
}
