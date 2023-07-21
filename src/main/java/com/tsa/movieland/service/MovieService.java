package com.tsa.movieland.service;

import com.tsa.movieland.entity.Movie;
import com.tsa.movieland.util.MovieRequest;

public interface MovieService {
    Iterable<Movie> findRandom();

    Iterable<Movie> findAllSorted(MovieRequest defaultMovieRequest);

    Iterable<Movie> findByGenreSorted(int genreId, MovieRequest defaultMovieRequest);
}
