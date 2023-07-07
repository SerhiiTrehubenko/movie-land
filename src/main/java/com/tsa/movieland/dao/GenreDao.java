package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> findAll();
}
