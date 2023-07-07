package com.tsa.movieland.cache;

import com.tsa.movieland.dao.GenreDao;
import com.tsa.movieland.entity.Genre;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;
@Slf4j
public class GenreCache extends TimerTask {

    private final GenreDao genreDao;
    private static List<String> GENRES;

    public GenreCache(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public void run() {
        List<Genre> genres = genreDao.findAll();
        List<String> genresString = genres.stream()
                        .map(Genre::name).toList();
        GENRES = Collections.synchronizedList(genresString);
        log.info("Cache of Genres has been refreshed at time:[%s]".formatted(LocalDateTime.now()));
    }

    public static List<String> getGENRES() {
        return GENRES;
    }
}
