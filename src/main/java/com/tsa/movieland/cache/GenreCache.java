package com.tsa.movieland.cache;

import com.tsa.movieland.dao.GenreDao;
import com.tsa.movieland.domain.RepositoryService;
import com.tsa.movieland.entity.Genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@RepositoryService
public class GenreCache {
    private final GenreDao genreDao;
    private Iterable<Genre> genres;

    @Scheduled(fixedRateString = "${genre.cache.cycle.hours}", timeUnit = TimeUnit.HOURS)
    protected void fillCache() {
        genres = genreDao.findAll();
        log.info("Cache of Genres has been refreshed");
    }

    public Iterable<Genre> getGenres() {
        return genres;
    }
}
