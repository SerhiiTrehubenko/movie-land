package com.tsa.movieland.dao;

import com.tsa.movieland.entity.GenreEntity;

public interface GenreDao {
    Iterable<GenreEntity> findAll();
    Iterable<GenreEntity> findByMovieId(int movieId);
}
