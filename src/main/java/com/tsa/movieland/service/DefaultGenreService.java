package com.tsa.movieland.service;

import com.tsa.movieland.cache.GenreCache;
import com.tsa.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultGenreService implements GenreService {
    private final GenreCache genreCache;

    @Override
    public Iterable<Genre> findAll() {
        return genreCache.getGenres();
    }
}
