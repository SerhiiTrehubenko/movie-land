package com.tsa.movieland.dao;

import com.tsa.movieland.dto.CountryDto;

public interface CountryDao {
    Iterable<CountryDto> findByMovieId(int movieId);

    Iterable<CountryDto> findAll();
}
