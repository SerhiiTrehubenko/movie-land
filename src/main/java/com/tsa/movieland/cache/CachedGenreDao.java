package com.tsa.movieland.cache;

import com.tsa.movieland.dao.GenreDao;
import com.tsa.movieland.context.Cache;
import com.tsa.movieland.entity.GenreEntity;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Cache
@Primary
public class CachedGenreDao implements GenreDao {

    private final GenreDao genreDao;

    private volatile Collection<GenreEntity> genres;

    @Scheduled(cron = "${genre.refresh-cron}")
    @PostConstruct
    private void fillCache() {
        genres = (Collection<GenreEntity>) genreDao.findAll();
        log.info("Cache of Genres has been refreshed");
    }
    @Override
    public Iterable<GenreEntity> findAll() {
        return new ArrayList<>(genres);
    }

    @Override
    public Iterable<GenreEntity> findByMovieId(int movieId) {
        return genreDao.findByMovieId(movieId);
    }
}
