package com.tsa.movieland.service;

import com.tsa.movieland.entity.Country;

public interface CountryService {
    Iterable<Country> findByMovieId(int movieId);

    Iterable<Country> findAll();
}
