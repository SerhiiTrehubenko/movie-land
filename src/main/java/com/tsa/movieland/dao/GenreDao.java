package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Genre;

public interface GenreDao {
    Iterable<Genre> findAll();
    Iterable<Genre> findByMovieId(int movieId);
}
