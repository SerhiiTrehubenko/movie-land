package com.tsa.movieland.service;

import com.tsa.movieland.entity.Genre;

public interface GenreService {
    Iterable<Genre> findAll();
}
