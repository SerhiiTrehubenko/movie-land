package com.tsa.movieland.service;

import com.tsa.movieland.common.MovieRequest;
import com.tsa.movieland.common.RatingRequest;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.dto.MovieFindAllDto;

public interface MovieService {
    Iterable<MovieFindAllDto> findRandom();

    Iterable<MovieFindAllDto> findAll(MovieRequest movieRequest);

    Iterable<MovieFindAllDto> findByGenre(int genreId, MovieRequest movieRequest);

    MovieByIdDto getById(int movieId, MovieRequest movieRequest);

    int save(AddUpdateMovieDto movie);

    void update(int movieId, AddUpdateMovieDto movie);

    void addRating(RatingRequest ratingRequest);
}
