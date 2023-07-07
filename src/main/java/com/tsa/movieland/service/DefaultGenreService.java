package com.tsa.movieland.service;

import com.tsa.movieland.dao.GenreDao;
import com.tsa.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultGenreService implements GenreService {
    private final GenreDao genreDao;

    @Override
    public List<Genre> findAll() {
        return genreDao.findAll();
    }
}
