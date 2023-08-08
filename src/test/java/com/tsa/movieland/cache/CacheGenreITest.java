package com.tsa.movieland.cache;

import com.tsa.movieland.entity.Genre;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Disabled
public class CacheGenreITest {

    @Autowired
    private CachedGenreDao cachedGenreDao;

    @Test
    void shouldFillCacheWithGenresNames() {
        Iterable<Genre> genres = cachedGenreDao.findAll();

        assertNotNull(genres);
        long genreNamesNumber = StreamSupport.stream(genres.spliterator(), false).count();
        assertEquals(15, genreNamesNumber);
    }
}
