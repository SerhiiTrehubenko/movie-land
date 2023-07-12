package com.tsa.movieland.service;

import com.tsa.movieland.domain.SortDirection;
import com.tsa.movieland.entity.Movie;

public interface MovieService {
    Iterable<Movie> findAll(SortDirection sortDirection);
    Iterable<Movie> findThreeRandom();
    Iterable<Movie> findByGenre(int genreId, SortDirection sortDirection);
}
