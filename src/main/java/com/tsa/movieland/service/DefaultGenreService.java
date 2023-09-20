package com.tsa.movieland.service;

import com.tsa.movieland.dao.GenreDao;
import com.tsa.movieland.dto.GenreDto;
import com.tsa.movieland.entity.Genre;
import com.tsa.movieland.mapper.GenreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class DefaultGenreService implements GenreService {
    private final GenreDao genreDao;
    private final GenreMapper genreMapper;

    @Override
    public Iterable<GenreDto> findAll() {
        return toDtos(genreDao.findAll());
    }

    @Override
    public Iterable<GenreDto> findByMovieId(int movieId) {
        return toDtos(genreDao.findByMovieId(movieId));
    }

    private Iterable<GenreDto> toDtos(Iterable<Genre> genres) {
        return StreamSupport.stream(genres.spliterator(), false)
                .map(genreMapper::toGenreDto).toList();
    }
}
