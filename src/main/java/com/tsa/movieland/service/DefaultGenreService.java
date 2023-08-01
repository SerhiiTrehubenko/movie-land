package com.tsa.movieland.service;

import com.tsa.movieland.dao.GenreDao;
import com.tsa.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultGenreService implements GenreService {
    private final GenreDao genreDao;

    @Override
    public Iterable<Genre> findAll() {
        return genreDao.findAll();
    }

    @Override
    public Iterable<Genre> findByMovieId(int movieId) {
        return genreDao.findByMovieId(movieId);
    }
}
