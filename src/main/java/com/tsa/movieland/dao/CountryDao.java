package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Country;

public interface CountryDao {
    Iterable<Country> findByMovieId(int movieId);

    Iterable<Country> findAll();
}
