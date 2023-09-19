package com.tsa.movieland.service;

import com.tsa.movieland.dto.GenreDto;

public interface GenreService {
    Iterable<GenreDto> findAll();

    Iterable<GenreDto> findByMovieId(int movieId);
}
