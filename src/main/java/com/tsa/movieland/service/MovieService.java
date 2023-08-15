package com.tsa.movieland.service;

import com.tsa.movieland.common.MovieRequest;
import com.tsa.movieland.dto.AddUpdateMovieDto;
import com.tsa.movieland.dto.MovieByIdDto;
import com.tsa.movieland.entity.MovieFindAllDto;

public interface MovieService {
    Iterable<MovieFindAllDto> findRandom();

    Iterable<MovieFindAllDto> findAll(MovieRequest defaultMovieRequest);

    Iterable<MovieFindAllDto> findByGenre(int genreId, MovieRequest defaultMovieRequest);

    MovieByIdDto getById(int movieId, MovieRequest movieRequest);

    int save(AddUpdateMovieDto movie);
}
