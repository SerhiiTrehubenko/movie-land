package com.tsa.movieland.service;

import com.tsa.movieland.dao.GenreDao;
import com.tsa.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultGenreService implements GenreService {
    private final GenreDao cachedGenreDao;

    @Override
    public Iterable<Genre> findAll() {
        return cachedGenreDao.findAll();
    }
}
