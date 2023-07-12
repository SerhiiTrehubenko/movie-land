package com.tsa.movieland.cache;

import com.tsa.movieland.dao.GenreDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CacheGenreTest {

    @Autowired
    GenreDao genreDao;

    @Test
    void shouldFillCacheWithGenresNames() {
        GenreCache genreCacheSut = new GenreCache(genreDao);
        genreCacheSut.run();
        Iterable<String> genres = genreCacheSut.getGenres();

        assertNotNull(genres);
        long genreNamesNumber = StreamSupport.stream(genres.spliterator(), false).count();
        assertEquals(15, genreNamesNumber);
    }
}
