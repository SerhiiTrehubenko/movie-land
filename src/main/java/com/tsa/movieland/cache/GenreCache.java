package com.tsa.movieland.cache;

import com.tsa.movieland.dao.GenreDao;
import com.tsa.movieland.entity.Genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
public class GenreCache {
    private final GenreDao genreDao;
    private Iterable<String> genres;

    @Scheduled(fixedRateString = "${genre.cache.cycle.hours}", timeUnit = TimeUnit.HOURS)
    public void run() {
        Iterable<Genre> genres = genreDao.findAll();
        List<String> genresString = StreamSupport.stream(genres.spliterator(), false)
                .map(Genre::name).toList();
        this.genres = Collections.synchronizedList(genresString);
        log.info("Cache of Genres has been refreshed");
    }

    public Iterable<String> getGenres() {
        return genres;
    }
}
