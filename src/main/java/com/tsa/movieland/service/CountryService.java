package com.tsa.movieland.service;

import com.tsa.movieland.dto.CountryDto;

public interface CountryService {
    Iterable<CountryDto> findByMovieId(int movieId);

    Iterable<CountryDto> findAll();
}
