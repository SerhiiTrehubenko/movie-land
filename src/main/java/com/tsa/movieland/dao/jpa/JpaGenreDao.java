package com.tsa.movieland.dao.jpa;

import com.tsa.movieland.context.JpaDao;
import com.tsa.movieland.dao.GenreDao;
import com.tsa.movieland.entity.Genre;
import com.tsa.movieland.repository.GenreRepository;
import lombok.RequiredArgsConstructor;

@JpaDao
@RequiredArgsConstructor
public class JpaGenreDao implements GenreDao {

    private final GenreRepository genreRepository;

    @Override
    public Iterable<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Iterable<Genre> findByMovieId(int movieId) {
        return genreRepository.findByMovieId(movieId);
    }
}
