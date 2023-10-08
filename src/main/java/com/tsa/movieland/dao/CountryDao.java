package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Country;

import java.util.List;

public interface CountryDao {
    List<Country> findByMovieId(int movieId);

    List<Country> findAll();
}
