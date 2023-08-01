package com.tsa.movieland.cache;

import com.tsa.movieland.dao.GenreDao;
import com.tsa.movieland.context.Cache;
import com.tsa.movieland.entity.Genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Cache
@Primary
public class CachedGenreDao implements GenreDao {

    private final GenreDao genreDao;

    private Collection<Genre> genres;

    @Scheduled(fixedRateString = "${genre.cache.cycle.hours}", timeUnit = TimeUnit.HOURS)
    private void fillCache() {
        genres = (Collection<Genre>) genreDao.findAll();
        log.info("Cache of Genres has been refreshed");
    }
    @Override
    public Iterable<Genre> findAll() {
        return new ArrayList<>(genres);
    }

    @Override
    public Iterable<Genre> findByMovieId(int movieId) {
        return genreDao.findByMovieId(movieId);
    }
}
