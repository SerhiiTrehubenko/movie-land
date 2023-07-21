package com.tsa.movieland.cache;

import com.tsa.movieland.dao.GenreDao;
import com.tsa.movieland.context.Cache;
import com.tsa.movieland.entity.Genre;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Cache
public class CachedGenreDao implements GenreDao {

    private final GenreDao jdbcGenreDao;

    private Collection<Genre> genres;

    @Scheduled(fixedRateString = "${genre.cache.cycle.hours}", timeUnit = TimeUnit.HOURS)
    @PostConstruct
    protected void fillCache() {
        Collection<Genre> resultGenres = (Collection<Genre>) jdbcGenreDao.findAll();
        genres = Collections.synchronizedCollection(resultGenres);
        log.info("Cache of Genres has been refreshed");
    }
    @Override
    public Iterable<Genre> findAll() {
        isCacheFilled();
        return genres.stream()
                .map(Genre::clone)
                .toList();
    }

    private void isCacheFilled() {
        if (!genres.iterator().hasNext()) {
            throw new RuntimeException("Table of genres is empty");
        }
    }
}
