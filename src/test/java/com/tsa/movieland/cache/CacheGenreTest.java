package com.tsa.movieland.cache;

import com.tsa.movieland.dao.GenreDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Timer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CacheGenreTest {

    @Autowired
    GenreDao genreDao;

    @Test
    void shouldExecuteTaskEveryFiveSeconds() throws InterruptedException {

        GenreCache genreCacheSut = new GenreCache(genreDao);

        Timer timer = new Timer("Timer");
        long delay = 5000L;

        timer.schedule(genreCacheSut,1, delay);

        List<String> genres = GenreCache.getGENRES();

        Thread.sleep(11000);

        assertEquals(15, genres.size());
    }
}
